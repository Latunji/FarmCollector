/*
 * 
 */
package com.interswitch.bifrost.cardservice.vo;

/**
 *
 * @author Oladele.Olaore
 */
import org.springframework.stereotype.Component;

@Component
public class BaseResponse {
    private String responseCode;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
