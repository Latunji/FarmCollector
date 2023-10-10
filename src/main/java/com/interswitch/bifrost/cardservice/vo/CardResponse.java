package com.interswitch.bifrost.cardservice.vo;

import com.interswitch.bifrost.cardservice.response.GetCardResponse;
import com.interswitch.bifrost.cardservice.response.GetPtmfbCardResponse;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class CardResponse {

    private boolean successful;
    private String responseMessage;
    private String batchNo;
    private String identifier;
    private String serialNo;
    private String transactionReference;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
