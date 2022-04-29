/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.request;

import java.math.BigInteger;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
