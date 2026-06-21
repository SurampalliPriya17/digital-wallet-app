package com.digital_wallet_app.service;

import com.digital_wallet_app.dto.TxnInitPayload;
import com.digital_wallet_app.dto.TxnRequestDto;
import com.digital_wallet_app.dto.TxnStatusDto;
import com.digital_wallet_app.entity.Transaction;
import com.digital_wallet_app.entity.TxnStatusEnum;
import com.digital_wallet_app.repo.TransactionRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TransactionService {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${txn.init.topic}")
    private String txnInitTopic;

    @Transactional
    public String initTransaction(TxnRequestDto txnRequestDto) throws ExecutionException, InterruptedException {
        Transaction transaction = new Transaction();
        transaction.setFromUserId(txnRequestDto.getFromUserId());
        transaction.setToUserId(txnRequestDto.getToUserId());
        transaction.setAmount(txnRequestDto.getAmount());
        transaction.setComment(txnRequestDto.getComment());
        transaction.setTxnId(UUID.randomUUID().toString());
        transaction.setStatus(TxnStatusEnum.PENDING);
        transactionRepo.save(transaction);

        //publish to kafka
        TxnInitPayload txnInitPayload = new TxnInitPayload();
        txnInitPayload.setId(transaction.getId());
        txnInitPayload.setFromUserId(transaction.getFromUserId());
        txnInitPayload.setToUserId(transaction.getToUserId());
        txnInitPayload.setAmount(transaction.getAmount());
        txnInitPayload.setRequestId(MDC.get("requestId"));
        Future<SendResult<String,Object>> future  = kafkaTemplate.send(txnInitTopic,transaction.getFromUserId().toString(),txnInitPayload);
        LOGGER.info("Pushed txnInitPayload to kafka: {}",future.get());

        return transaction.getTxnId();

    }

    public TxnStatusDto getStatus(String txnId){
        Transaction transaction = transactionRepo.findByTxnId(txnId);
        TxnStatusDto txnStatusDto = new TxnStatusDto();
        if(transaction != null){
            txnStatusDto.setReason(transaction.getReason());
            txnStatusDto.setStatus(transaction.getStatus().toString());
        }
        return txnStatusDto;
    }
}

