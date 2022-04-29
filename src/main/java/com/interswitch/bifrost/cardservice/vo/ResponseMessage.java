/*
 * 
 */
package com.interswitch.bifrost.cardservice.vo;

/**
 *
 * @author Oladele.Olaore
 */
public class ResponseMessage {
    public static final String GENERAL_SUCCESS_MESSAGE = "Operation successful";
    public static final String GENERAL_ERROR_MESSAGE = "Operation processing failed";
    public static final String WEB_SERVICE_ERROR_MESSAGE = "Operation processing failed or timed out from third-party service";
    public static final String OPERATION_TIMEOUT_ERROR_MESSAGE = "Operation processing timed out";
    
    public static final String UNSUPPORTED_PASSWORDTYPE_ERROR_MESSAGE = "Password type not supported";
    public static final String UNKNOWN_PASSWORDTYPE_ERROR_MESSAGE = "Unknown password type";
    public static final String PASSWORDVALIDATION_ERROR_MESSAGE = "Password validation failed";
    
    public static final String OPERATION_NOT_SUPPORTED = "Operation not supported";
}
