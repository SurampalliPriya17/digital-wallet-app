package com.digital_wallet_app.config;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.digital_wallet_app.dto.TxnCompletedPayload;
import com.digital_wallet_app.entity.Transaction;
import com.digital_wallet_app.entity.TxnStatusEnum;
import com.digital_wallet_app.repo.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TransactionKafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionKafkaConsumerConfig.class);

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private TransactionRepo transactionRepo ;

    @KafkaListener(topics = "${txn.completed.topic}", groupId = "txn")
    public void consumeTxnInitTopic(ConsumerRecord payload) throws JsonProcessingException, ExecutionException, InterruptedException {
        TxnCompletedPayload txnCompletedPayload = OBJECT_MAPPER.readValue(payload.value().toString(), TxnCompletedPayload.class);
        MDC.put("requestId", txnCompletedPayload.getRequestId());
        LOGGER.info("Read from kafka : {}", txnCompletedPayload);
        Transaction transaction = transactionRepo.findById(txnCompletedPayload.getId()).get();
        if(!txnCompletedPayload.getSuccess()){
            transaction.setStatus(TxnStatusEnum.FAILED);
            transaction.setReason(txnCompletedPayload.getReason());
        }
        else{
            transaction.setStatus(TxnStatusEnum.SUCCESS);
        }
        transactionRepo.save(transaction);
        MDC.clear();;
    }


}

