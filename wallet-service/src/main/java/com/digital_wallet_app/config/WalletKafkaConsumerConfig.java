package com.digital_wallet_app.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.digital_wallet_app.service.WalletService;
import com.digital_wallet_app.dto.TxnInitPayload;
import com.digital_wallet_app.dto.UserCreatedPayload;
import com.digital_wallet_app.dto.UserUpdatedPayLoad;
import com.digital_wallet_app.entity.Wallet;
import com.digital_wallet_app.repo.WalletRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;


import java.util.concurrent.ExecutionException;

@Configuration
public class WalletKafkaConsumerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(WalletKafkaConsumerConfig.class);

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private WalletService walletService;

    @KafkaListener(topics = "${user.created.topic}", groupId = "wallet")
    public void consumeUserCreateTopic(ConsumerRecord payload) throws JsonProcessingException {
        UserCreatedPayload userCreatedPayload = OBJECT_MAPPER.readValue(payload.value().toString(),
                UserCreatedPayload.class);
        MDC.put("requestId", userCreatedPayload.getRequestId());
        LOGGER.info("Read from kafka : {}", userCreatedPayload);
        Wallet wallet = new Wallet();
        wallet.setBalance(100.00);
        wallet.setUserId(userCreatedPayload.getUserId());
        wallet.setUserEmail(userCreatedPayload.getUserEmail());
        LOGGER.info("logs are working");
        walletRepo.save(wallet);
        MDC.clear();
    }

    @KafkaListener(topics = "${user.updated.topic}", groupId = "wallet")
    public void consumeUserUpdateTopic(ConsumerRecord payload) throws JsonProcessingException {
        UserUpdatedPayLoad userUpdatedPayLoad = OBJECT_MAPPER.readValue(payload.value().toString(),
                UserUpdatedPayLoad.class);

        Wallet wallet = walletRepo.findByUserId(userUpdatedPayLoad.getUserId());

        if (wallet == null) {
            LOGGER.error("Wallet not found for userId={}", userUpdatedPayLoad.getUserId());
            return;
        }

        MDC.put("requestId", userUpdatedPayLoad.getRequestId());
        LOGGER.info("Read from kafka : {}", userUpdatedPayLoad);
        wallet = walletRepo.findByUserId(userUpdatedPayLoad.getUserId());

        wallet.setUserId(userUpdatedPayLoad.getUserId());
        wallet.setUserEmail(userUpdatedPayLoad.getUserEmail());
        walletRepo.save(wallet);
        MDC.clear();
    }

    @KafkaListener(topics = "${txn.init.topic}", groupId = "wallet")
    public void consumeTxnInitTopic(ConsumerRecord payload)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        TxnInitPayload txnInitPayload = OBJECT_MAPPER.readValue(payload.value().toString(), TxnInitPayload.class);
        MDC.put("requestId", txnInitPayload.getRequestId());
        LOGGER.info("Read from kafka : {}", txnInitPayload);
        walletService.walletTxn(txnInitPayload);
        MDC.clear();
    }

}

