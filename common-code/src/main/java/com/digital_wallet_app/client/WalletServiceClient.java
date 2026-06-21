package com.digital_wallet_app.client;

import com.digital_wallet_app.dto.AddMoneyRequest;
import com.digital_wallet_app.dto.AddMoneyResponse;
import com.digital_wallet_app.dto.WalletBalanceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "wallet-service", url = "http://localhost:8081")
public interface WalletServiceClient {

    @GetMapping("/wallet-service/balance/{userId}")
    ResponseEntity<WalletBalanceDto> getBalance(@PathVariable Long userId);

    @DeleteMapping("/wallet-service/delete-wallet/{userId}")
    void deleteWallet(@PathVariable Long userId);

    @PostMapping ("/wallet-service/add-money")
    ResponseEntity<AddMoneyResponse> addMoney(@RequestBody AddMoneyRequest addMoneyRequest);

}
