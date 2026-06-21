package com.digital_wallet_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TxnRequestDto {

    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    @NotNull
    private Double amount;

    private String comment;
}

