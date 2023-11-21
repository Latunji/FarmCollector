package com.interswitch.bifrost.cardservice.service.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PtmfbCard {
    private String CardPAN;
    private String AccountNumber;
    private String LinkedDate;
    private String SerialNo;
    private String ExpiryDate;
    private String NameOnCard;
    private String Status;
}
