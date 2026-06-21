package com.digital_wallet_app.controller;

import com.digital_wallet_app.dto.AddMoneyRequest;
import com.digital_wallet_app.dto.AddMoneyResponse;
import com.digital_wallet_app.dto.WalletBalanceDto;
import com.digital_wallet_app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/wallet-service")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${initialize-payment-url}")
    private String INITIALIZE_PAYMENT_URL;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<WalletBalanceDto> getBalance(@PathVariable Long userId) {
        WalletBalanceDto walletBalanceDto = walletService.walletBalance(userId);
        return ResponseEntity.ok(walletBalanceDto);
    }

    @PostMapping("/add-money")
    public ResponseEntity<AddMoneyResponse> addMoney(@RequestBody AddMoneyRequest addMoneyRequest) {

        AddMoneyResponse addMoneyResponse = walletService.addMoney(addMoneyRequest);
        return ResponseEntity.ok(addMoneyResponse);
    }

   /* @GetMapping("/add-money-status/{pgTxnId}")
    public ResponseEntity<String> addMoneyStatus(@PathVariable String pgTxnId) {
        return ResponseEntity.ok(walletService.processPgTxnId(pgTxnId));
    }*/

    @DeleteMapping("/delete-wallet/{userId}")
    public void delete(@PathVariable Long userId) {
        System.out.println("DELETE WALLET CALLED: " + userId);
        walletService.deleteWallet(userId);
    }
}

