/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.exception;

/**
 *
 * @author chidiebere.onyeagba
 */
public class DeactivatedDeviceException extends Exception {
    
    String errorMessage;
    public DeactivatedDeviceException(String stackTraceMessage, String errorMessage)
    {
        super(stackTraceMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
