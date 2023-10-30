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
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 *
 * @author Ahmed.Oladele
 */
@Getter
@Setter
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

    @JsonProperty("deliveryOption")
    private String deliveryOption;

    @JsonProperty("bin")
    private String bin;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("requestType")
    private String requestType;

    private String hotlistReason;

    @JsonProperty("serialNo")
    private String serialNo;

    @JsonProperty("reason")
    private String reason;

    private Boolean block;

}
