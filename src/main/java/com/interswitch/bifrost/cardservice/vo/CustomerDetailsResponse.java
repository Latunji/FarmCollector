/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.vo;


import com.interswitch.bifrost.commons.vo.ServiceResponse;


/**
 *
 * @author chidiebere.onyeagba
 */
public class CustomerDetailsResponse extends ServiceResponse{
    
    public CustomerDetailsResponse(int code) {
        super(code,"");
        name = "";
        phoneNumber = "";
        email = "";
        bvn = "";
        custNo = "";
        branchCode = "";
        dateOfBirth = "";
        
    }
    
    public CustomerDetailsResponse(int code, String description) {
        super(code, description);
        name = "";
        phoneNumber = "";
        email = "";
        bvn = "";
        custNo = "";
        branchCode = "";
        dateOfBirth = "";
    }
    
    private String name;
    private String phoneNumber;
    private String email;
    private String bvn;
    private String dob;
    private String custNo;
    private String branchCode;
    private String dateOfBirth;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    
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




    