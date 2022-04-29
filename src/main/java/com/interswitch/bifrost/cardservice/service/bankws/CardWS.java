/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.bankws;

/**
 *
 * @author chidiebere.onyeagba
 */
public interface CardWS {
    public String getCards(String accountNumber, String custNo,String institutionCode) throws Exception ;
    public String hotlistCard(String accountNumber,String cardPan, String custNo,String institutionCode) throws Exception;
    public String replaceCard(String accountNumber,String cardPan,String custNo,String deliveryAddress,String deliveryType,String institutionCode) throws Exception;
    public String requestCard(String accountNumber,String cardType,String custNo,String nameOnCard,String institutionCode, String branchCode) throws Exception;
    public String validateCustomer(String deviceId,String institutionCode) throws Exception;
    public String validateCustomerWithAccount(String deviceId,String accountNumber,String institutionCD) throws Exception;
    public String activateCustomerTransaction(String deviceId,String accountNumber,String institutionCode) throws Exception;
    public String getCustomerDetails(String cardPan,String cardPin,String institutionCode) throws Exception;
    public String unblockCard(String accountNumber,String custNo,String cardPan,String institutionCD) throws Exception;
}