package com.digital_wallet_app.service;



import com.digital_wallet_app.dto.PaymentInitResponse;
import com.digital_wallet_app.dto.PaymentPageRequest;
import com.digital_wallet_app.dto.TransactionDetailDto;
import com.digital_wallet_app.entity.Merchant;
import com.digital_wallet_app.entity.Transaction;
import com.digital_wallet_app.repo.MerchantRepo;
import com.digital_wallet_app.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private MerchantRepo merchantRepo;

    public TransactionDetailDto getStatus(String txnId){
        Transaction transaction = transactionRepo.findByTxnId(txnId);
        TransactionDetailDto transactionDetailDto = TransactionDetailDto.builder()
                .userId(transaction.getUserId())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .build();
        return transactionDetailDto;

    }

    public Transaction getTransaction(String txnId){
        Transaction transaction = transactionRepo.findByTxnId(txnId);
        return transaction;
    }

    public String doPaymentAndRedirect(String txnId){
        Transaction transaction = transactionRepo.findByTxnId(txnId);
        transaction.setStatus("SUCCESS");
        transactionRepo.save(transaction);
        Merchant merchant = merchantRepo.findById(transaction.getMerchantId()).get();
       
        
        String url = merchant.getRedirectionUrl()+txnId;
        return url;
    }

    public PaymentInitResponse generatePaymentPage(PaymentPageRequest request){
        String txnId = UUID.randomUUID().toString();
        Transaction transaction = Transaction.builder()
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .status("PENDING")
                .txnId(txnId)
                .userId(request.getUserId())
                .build();
        transactionRepo.save(transaction);
        String url = "http://localhost:9090/payment-page/"+txnId;
        PaymentInitResponse paymentInitResponse = PaymentInitResponse.builder()
                .txnId(txnId)
                .url(url)
                .build();
        return paymentInitResponse;
    }


}

