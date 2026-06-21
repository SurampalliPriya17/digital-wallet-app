package com.digital_wallet_app.repo;

import com.digital_wallet_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    Transaction findByTxnId(String txnId);
}

