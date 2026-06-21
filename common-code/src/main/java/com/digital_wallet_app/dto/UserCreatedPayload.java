package com.digital_wallet_app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCreatedPayload implements Serializable {

    private static final long serialVersionUID = 1l;

    private Long userId;
    private String userName;
    private String userEmail;
    private String requestId;
}

