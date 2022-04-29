package com.interswitch.bifrost.cardservice.vo;

/**
 *
 * @author olise
 */
public class ResponseCode
{
    public static final int SUCCESS = 0;
    public static final int ERROR = 10;
    public static final int ERROR_INTERNAL = 11; // Failure within proxy processing before sending request either to bank or to third party
    public static final int ERROR_INTERNAL_PARSING_RESPONSE = 12; // Failure within proxy parsing response from bank
    public static final int ERROR_INTERNAL_PARSING_RESPONSE_FROM_THIRDPARTY = 13; // Failure within proxy parsing response from third party
    public static final int ENROLLMENT_FOUND = 14;
    public static final int ERROR_THIRDPARTY = 15; // Failure from external service eg. QT or MBPP
    public static final int ERROR_EXT_CONNECT = 35; // After successful debit of the customer account, Proxy couldn't connect to external bills service provider
    public static final int REVERSED = 50; // If a bills payment transaction fails and was reversed successfully

    public static final int SESSION_EXPIRED = 103;
    
    public static final int PENDING = 40; // Proxy could not get response from external service after successful debit of the customer account
    public static final int PENDING_THIRDPARTY = 45; // Proxy got a pending response from external service
    
    public static final int PENDING_MBPP = 2;
    public static final String ERROR_MBPP = "0_0";
    public static final int OK_MBPP = 1;
    public static final int FAILED_MBPP = 0;
    
    public static final String GENERAL_ERROR_MESSAGE = "Request processing error";
    public static final String GENERAL_SUCCESS_MESSAGE = "Operation Successful";
    public static String ERROR_PIN_FAILED = "MPIN VERIFICATION FAILED";
}
