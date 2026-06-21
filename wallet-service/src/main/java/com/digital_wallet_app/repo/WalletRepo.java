package com.digital_wallet_app.repo;

import com.digital_wallet_app.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {

    Wallet findByUserId(Long userId);

}
