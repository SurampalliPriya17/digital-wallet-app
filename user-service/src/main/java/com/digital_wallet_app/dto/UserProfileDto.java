package com.digital_wallet_app.dto;



import lombok.*;


import java.io.Serializable;

@Setter
@Getter
@ToString
public class UserProfileDto implements Serializable {

    private static final long serialVersionUID = 1l;

    private UserDto userDetail;
    private Double walletBalance;
}
