/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.bankws.request;
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
public class GetQuickCustomerDetails {
    //@RequestParam("deviceId") 
    @NotBlank(message="device ID cannot be blank")
    private String deviceId;
    
    //@RequestParam("accountNumber") 
    //@Pattern(regexp ="[0-9]+" ,message="Account number must be digits only") @Size(min=10,max=10, message="Account number must be ten digits")   
    private String accountNumber;
    
    //@RequestParam("phoneNumber") 
    //@Pattern(regexp ="[0-9]+" ,message="Phone number must be digits only") 
    //@Size(min=11, message="Phone number must be 11 digits") 
    //@NotBlank(message="phone number cannot be blank")
    private String phoneNumber;
    private String institutionCD;
    
    //@RequestParam("deviceModel")  
    //@NotBlank(message="device model cannot be blank")
    //private String deviceModel;
    
    //@RequestParam("institutionCD") 
    //@Size(min=1 ,message="invalid institution CD") 
    //@NotBlank(message="institution cd cannot be blank")
    //private String institutionCD;
    
    
}
