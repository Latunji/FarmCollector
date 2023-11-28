/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.bankws.request;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ahmed.Oladele
 */
@Getter
@Setter
public class HotlistCardRequest {
    
    private String custNo;  
    private String pan;
    private String accountNumber; 
    private String clientUrl;
    private String reference;
    private String institutionCode;

    private String currency;
    private String apiKey;
    private String authId;
    private String appId;


        
}
