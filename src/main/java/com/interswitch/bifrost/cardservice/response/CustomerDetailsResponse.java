/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.response;

import com.interswitch.bifrost.commons.vo.ServiceResponse;


/**
 *
 * @author chidiebere.onyeagba
 */
public class CustomerDetailsResponse extends ServiceResponse{
    
    public CustomerDetailsResponse(int code) {
        super(code);
    }
    
    public CustomerDetailsResponse(int code, String description) {
        super(code, description);
    }
    
    private String name;
    private String phoneNumber;
    private String email;
    private String bvn;
    private String dob;
    private String custNo;
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email ;
    }
    
    public String getBvn()
    {
        return bvn;
    }
    public void setBvn(String bvn)
    {
        this.bvn = bvn;
    }
    
    public String getDob()
    {
        return dob;
    }
    public void setDob(String dob)
    {
        this.dob = dob;
    }
    public String getCustNo()
    {
        return custNo;
    }
    public void setCustNo(String custNo)
    {
        this.custNo = custNo;
    }
}




    