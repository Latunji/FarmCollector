package com.interswitch.bifrost.cardservice.service.bankws.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PtmfbCardApiRequest {

    private String custNo;
    private String accountNumber;
    private String requestType;
    private String nameOnCard;
    private String clientUrl;
    private String institutionCD;
    private String bin;
    private String deliveryOption;
    private String identifier;
    private String token;
    private String reason;
    private String reference;
    private String serialNo;
    private String customerID;
    private String accountNo;
    private boolean block;
}
