package com.interswitch.bifrost.cardservice.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCardResponse {
    private boolean IsSuccessful;
    private String responseMessage;
    private String batchNo;
    private String identifier;
    private String serialNo;
    private String transactionReference;
}
