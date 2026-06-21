package com.digital_wallet_app.service;

import com.digital_wallet_app.dto.*;
import org.example.dto.*;
import com.digital_wallet_app.entity.Wallet;
import com.digital_wallet_app.repo.WalletRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class WalletService {

	private static Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

	@Autowired
	private WalletRepo walletRepo;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${txn.completed.topic}")
	private String txnCompletedTopic;

	@Value("${wallet.updated.topic}")
	private String walletUpdatedTopic;

	@Value("${initialize-payment-url}")
	private String INITIALIZE_PAYMENT_URL;

	@Autowired
	private RestTemplate restTemplate;

	public WalletBalanceDto walletBalance(Long userId) {

		Wallet wallet = walletRepo.findByUserId(userId);
		WalletBalanceDto walletBalanceDto = new WalletBalanceDto();
		walletBalanceDto.setBalance(wallet.getBalance());
		return walletBalanceDto;
	}

	@Transactional
	public void walletTxn(TxnInitPayload txnInitPayload) throws ExecutionException, InterruptedException {
		Wallet fromWallet = walletRepo.findByUserId(txnInitPayload.getFromUserId());

		TxnCompletedPayload txnCompletedPayload = new TxnCompletedPayload();
		txnCompletedPayload.setRequestId(txnInitPayload.getRequestId());
		txnCompletedPayload.setId(txnInitPayload.getId());
		if (fromWallet.getBalance() < txnInitPayload.getAmount()) {
			txnCompletedPayload.setSuccess(false);
			txnCompletedPayload.setReason("Low Balance");
		} else {
			Wallet toWallet = walletRepo.findByUserId(txnInitPayload.getToUserId());
			fromWallet.setBalance(fromWallet.getBalance() - txnInitPayload.getAmount());
			toWallet.setBalance(toWallet.getBalance() + txnInitPayload.getAmount());
			txnCompletedPayload.setSuccess(true);

			WalletUpdatedPayload walletUpdatedPayload1 = new WalletUpdatedPayload(fromWallet.getUserEmail(),
					fromWallet.getBalance(), txnInitPayload.getRequestId());

			WalletUpdatedPayload walletUpdatedPayload2 = new WalletUpdatedPayload(toWallet.getUserEmail(),
					toWallet.getBalance(), txnInitPayload.getRequestId());

			walletRepo.save(fromWallet);
			walletRepo.save(toWallet);

		}
		Future<SendResult<String, Object>> future = kafkaTemplate.send(txnCompletedTopic,
				txnInitPayload.getFromUserId().toString(), txnCompletedPayload);
		LOGGER.info("Pushed TxnCompleted to kafka: {}", future.get());
	}

	public String processPgTxnId(String pgTxnId) {
		PGPaymentStatusDTO pgPaymentStatusDTO = restTemplate
				.getForObject("http://localhost:9090/pg-service/payment-status/" + pgTxnId, PGPaymentStatusDTO.class);
		if (pgPaymentStatusDTO.getStatus().equalsIgnoreCase("SUCCESS")) {
			Wallet wallet = walletRepo.findByUserId(pgPaymentStatusDTO.getUserId());
			wallet.setBalance(wallet.getBalance() + pgPaymentStatusDTO.getAmount());
			walletRepo.save(wallet);
			return "success";
		} else {
			return "failed";
		}
	}

	public AddMoneyResponse addMoney(AddMoneyRequest addMoneyRequest) {
		addMoneyRequest.setMerchantId(1l);
		AddMoneyResponse addMoneyResponse = restTemplate.postForObject(INITIALIZE_PAYMENT_URL, addMoneyRequest,
				AddMoneyResponse.class);
		return addMoneyResponse;
	}

	public void deleteWallet(Long userId) {
		Wallet wallet = walletRepo.findByUserId(userId);

		if (wallet == null) {
			throw new RuntimeException("Wallet not found");
		}

		walletRepo.delete(wallet);

	}

}
