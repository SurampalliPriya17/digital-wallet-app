package com.digital_wallet_app.repo;

import com.digital_wallet_app.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepo extends JpaRepository<Merchant, Long> {
}

