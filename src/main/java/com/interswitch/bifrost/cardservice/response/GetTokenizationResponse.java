/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.response;

/**
 *
 * @author Benjamin.Abegunde
 */
public class GetTokenizationResponse {
    private String responseCode;
    private String text;
    
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }  
    
}
