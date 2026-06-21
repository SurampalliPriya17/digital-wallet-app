package com.digital_wallet_app.client;


import com.digital_wallet_app.dto.TxnRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "transaction-service", url = "http://localhost:8083")
public interface TransactionServiceClient {

    @PostMapping ("/transaction-service/txn")
    ResponseEntity<String> initTransaction(@RequestBody TxnRequestDto txnRequestDto  );



}

