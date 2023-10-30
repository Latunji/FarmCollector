package com.interswitch.bifrost.cardservice.service.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PtmfbCard {
    private String cardPAN;
    private String accountNumber;
    private String linkedDate;
    private String serialNo;
    private String expiryDate;
    private String nameOnCard;
}
