package com.digital_wallet_app.repo;



import com.digital_wallet_app.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    User findByEmail(String email);

}

