package com.digital_wallet_app.dto;
import lombok.*;
@Setter
@Getter
@ToString
public class TxnInitPayload {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private Double amount;
    private String requestId;
}

