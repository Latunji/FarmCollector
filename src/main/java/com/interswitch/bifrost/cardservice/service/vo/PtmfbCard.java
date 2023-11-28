package com.interswitch.bifrost.cardservice.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PtmfbCard {
    private String cardPAN;
    private String accountNumber;
    private String linkedDate;
    private String serialNo;
    private String expiryDate;
    private String nameOnCard;
    private String status;
}
