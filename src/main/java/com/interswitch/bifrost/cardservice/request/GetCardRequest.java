package com.interswitch.bifrost.cardservice.request;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author chidiebere.onyeagba
 */
@Getter
@Setter
public class GetCardRequest {
    //private String cardNumber;
    //private BigInteger cardLimilt;
    private String accountNumber;
    private String custNo;
    
}
