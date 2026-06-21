package com.digital_wallet_app.dto;

import lombok.Data;

@Data
public class TxnCompletedPayload {
    private Long id; // ID for actual txn
    private Boolean success;
    private String reason;
    private String requestId;

}
