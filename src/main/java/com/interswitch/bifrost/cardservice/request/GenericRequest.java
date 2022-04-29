/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;


/**
 *
 * @author Ahmed.Oladele
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericRequest 
{
    
    @JsonProperty("custNo")
    private String custNo;
    @JsonProperty("accountNumber")
    private String accountNumber;
    
     @JsonProperty("deviceId")
    private String deviceId;
    
    @JsonProperty("clientUrl")
    private String clientUrl;
    
    @JsonProperty("cardPan")
    private String cardPan;
    @JsonProperty("cardType")
    private String cardType;
    @JsonProperty("nameOnCard")
    private String nameOnCard;
    @JsonProperty("deliveryType")
    private String deliveryType;
    @JsonProperty("deliveryAddress")
    private String deliveryAddress;
    
    @JsonProperty("missingDigit")
    private String  missingDigit;
    
    @JsonProperty("institutionCD")
    private String institutionCD;
    @JsonProperty("cardPin")
    private String cardPin;
    @JsonProperty("branchCode")
    private String branchCode;

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public String getInstitutionCD() {
        return institutionCD;
    }

    public void setInstitutionCD(String institutionCD) {
        this.institutionCD = institutionCD;
    }
    
    public String getCardPan() {
        return cardPan;
    }

    public void setCardPan(String cardPan) {
        this.cardPan = cardPan;
    } 
    
    public String getCardType() {
        return this.cardType;
    } 
    public void setCardType(String cardtype) {
        this.cardType = cardtype;
    } 
    
    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    } 
    
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    } 
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    } 

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }
                
    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMissingDigit() {
        return missingDigit;
    }

    public void setMissingDigit(String missingDigit) {
        this.missingDigit = missingDigit;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    
    
}
