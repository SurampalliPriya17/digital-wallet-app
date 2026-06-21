package com.digital_wallet_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMoneyRequest {

    private Double amount;
    private Long userId;

    // for internal use
    private Long merchantId;
}

