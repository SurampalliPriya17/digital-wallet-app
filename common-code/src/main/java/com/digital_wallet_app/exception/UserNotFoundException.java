package com.digital_wallet_app.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String Message) {
        super(Message);
    }

}
