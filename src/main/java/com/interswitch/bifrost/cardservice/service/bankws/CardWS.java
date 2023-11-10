package com.interswitch.bifrost.cardservice.service.bankws;

/**
 *
 * @author chidiebere.onyeagba
 */
public interface CardWS {
    public String getCards(String accountNumber, String custNo,String institutionCode) throws Exception ;
    public String getPtmfbCards(String accountNumber, String custNo,String institutionCode) throws Exception ;
    public String getProvidusCards(String custNo,String institutionCode) throws Exception ;
    public String viewProvidusCardStatus(String cardPan, String institutionCode) throws Exception;
    public String providusHotlistCard(String cardPan, String currency, String institutionCode) throws Exception;
    public String providusDehotlistCard(String cardPan, String currency, String institutionCode) throws Exception;
    public String hotlistCard(String accountNumber,String cardPan, String custNo,String institutionCode) throws Exception;
    public String hotlistPtmfbCard(String accountNumber, String serialNo, String reason, String reference, String institutionCD);
    public String blockandUnblockPtmfbCard(String accountNumber, String serialNo, String reason, String reference, String institutionCD, Boolean block);
    public String replaceCard(String accountNumber,String cardPan,String custNo,String deliveryAddress,String deliveryType,String institutionCode) throws Exception;
    public String requestCard(String accountNumber,String cardType,String custNo,String nameOnCard,String institutionCode, String branchCode) throws Exception;
    public String requestPtmfbCard(String accountNumber,String requestType,String custNo,String nameOnCard,String institutionCode, String bin, String deliveryOption) throws Exception;
    public String validateCustomer(String deviceId,String institutionCode) throws Exception;
    public String validateCustomerWithAccount(String deviceId,String accountNumber,String institutionCD) throws Exception;
    public String activateCustomerTransaction(String deviceId,String accountNumber,String institutionCode) throws Exception;
    public String getCustomerDetails(String cardPan,String cardPin,String institutionCode) throws Exception;
    public String unblockCard(String accountNumber,String custNo,String cardPan,String institutionCD) throws Exception;
}