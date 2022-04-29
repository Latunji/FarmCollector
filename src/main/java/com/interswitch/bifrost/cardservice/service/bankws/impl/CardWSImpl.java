/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.bankws.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.bifrost.cardservice.response.CustomerDetailsResponse;
import com.interswitch.bifrost.cardservice.service.bankws.CardWS;
import com.interswitch.bifrost.cardservice.service.bankws.request.CustDetailsRequest;
import com.interswitch.bifrost.cardservice.service.bankws.request.GetQuickCustomerDetails;
import com.interswitch.bifrost.cardservice.service.bankws.request.HotlistCardRequest;
import com.interswitch.bifrost.cardservice.service.bankws.request.ReplaceCard;
import com.interswitch.bifrost.cardservice.service.bankws.request.RequestCard;
import com.interswitch.bifrost.cardservice.service.bankws.request.UnblockCardRequest;
import com.interswitch.bifrost.cardservice.util.ConfigProperties;
import com.interswitch.bifrost.commons.security.InterServiceSecurityUtil;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//import okhttp3.HttpUrl;
import java.io.IOException;
//import com.vanso.proxy.commons.annotations.Mock;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chidiebere.onyeagba
 */
//@Mock
@Component
public class CardWSImpl implements CardWS {

    @Autowired
    private ConfigProperties configx;
    @Autowired
    private InterServiceSecurityUtil interService;

    private static final Logger LOGGER = Logger.getLogger(CardWSImpl.class.getName());

    @Override
    public String validateCustomer(String deviceId, String institutionCD) {
        LOGGER.log(Level.SEVERE, String.format("%s - %s ", "validate Customer ", configx.getBankGatewayUrl(institutionCD)), configx.getBankBaseUrl(institutionCD));
        String result = "";
        try {
            GetQuickCustomerDetails acctReq = new GetQuickCustomerDetails();
            ObjectMapper mapper = new ObjectMapper();
            acctReq.setDeviceId(deviceId);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            //http://84.200.29.248:8885/customer/quick/customerdetails
            Request request = new Request.Builder()
                    //.url(configx.getBankGatewayBaseUrl()+"customer/quick/customerdetails")
                    .url(configx.getBankGatewayUrl(institutionCD) + "customer/validatecustomer?deviceId=" + deviceId)
                    //.post(body)
                    .get()
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.info(String.format(" %s- %s ", "VALIDATE CUSTOMER FROM CUSTOMER SERVICE", rBody));
            return rBody;

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CUSTOMER DETAILS EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"descriotion\":\"Error Occured\",\n"
                    + "   \"code\":10"
                    + "}";
            //nameVO.setResponseMessage(ResponseCode.GENERAL_ERROR_MESSAGE);
        }

        // return result;
    }

    @Override
    public String activateCustomerTransaction(String deviceId, String accountNumber, String institutionCD) {
        LOGGER.log(Level.SEVERE, String.format("%s - %s", "activateCustomer EXCEPTION", configx.getBankBaseUrl(institutionCD)), configx.getBankGatewayUrl(institutionCD));
        String result = "";
        try {
            ServiceResponse acctReq = new ServiceResponse();
//            ObjectMapper mapper = new ObjectMapper();
//            acctReq.setDeviceId(deviceId);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
//            String ss = mapper.writeValueAsString(acctReq);
//            RequestBody body = RequestBody.create(mediaType, ss);
            //http://84.200.29.248:8885/customer/quick/customerdetails
            Request request = new Request.Builder()
                    .url(configx.getBankGatewayUrl(institutionCD) + "customer/activateCustomer?deviceId=" + deviceId + "&accountNumber=" + accountNumber)
                    .get()
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            return rBody;

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "ACTIVATE CUSTOMER  EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"descriotion\":\"Error Occured\",\n"
                    + "   \"code\":10"
                    + "}";
            //nameVO.setResponseMessage(ResponseCode.GENERAL_ERROR_MESSAGE);
        }

        // return result;
    }

    @Override
    public String validateCustomerWithAccount(String deviceId, String accountNumber, String institutionCD) {
        String header = interService.getInterServiceHeaderAuthKey();

        LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "validateCustomerwithAccount ", configx.getBankGatewayUrl(institutionCD), institutionCD));
        String result = "";
        try {
            ServiceResponse acctReq = new ServiceResponse();
//            ObjectMapper mapper = new ObjectMapper();
//            acctReq.setDeviceId(deviceId);

            ObjectMapper mapper = new ObjectMapper();
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getBankGatewayUrl(institutionCD) + "customer/validatecustomerForInstitution").newBuilder();
            urlBuilder.addQueryParameter("deviceId", deviceId);
            urlBuilder.addQueryParameter("accountNumber", accountNumber);
            urlBuilder.addQueryParameter("institutionCD", institutionCD);
            String url = urlBuilder.build().toString();

            MediaType mediaType = MediaType.parse("application/json");
            Request request = new Request.Builder()
                    //.url(configx.getBankGatewayUrl(institutionCD)+"customer/validatecustomerForInstitution?deviceId="+deviceId+"&accountNumber="+accountNumber+"&institutionCD="+institutionCD)
                    //.url(configx.getCustomerUrl()+"customer/validatecustomerForInstitution?deviceId="+deviceId+"&accountNumber="+accountNumber+"&institutionCD="+institutionCD)
                    .url(url)
                    .get()
                    .header(InterServiceSecurityUtil.AUTH_HEADER_KEY, header)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();
            LOGGER.info(String.format(" %s- %s ", "VALIDATE CUSTOMER FROM CUSTOMER SERVICE request", request.toString()));
            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.info(String.format(" %s- %s ", "VALIDATE CUSTOMER RESPONSE FROM CUSTOMER SERVICE", rBody));
            return rBody;

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "VALIDATE CARDS EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"descriotion\":\"Error Occured\",\n"
                    + "   \"code\":10"
                    + "}";
            //nameVO.setResponseMessage(ResponseCode.GENERAL_ERROR_MESSAGE);
        }

        // return result;
    }

    public String getCards(String accountNumber, String customerNo, String institutionCD) throws Exception {
        LOGGER.log(Level.SEVERE, String.format("%s - %s ", "GET CARD ", configx.getBankBaseUrl(institutionCD)), configx.getBankGatewayUrl(institutionCD));
        Response response = null;
        try {

            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getVersionedUrl(institutionCD) + "getCards").newBuilder();
            urlBuilder.addQueryParameter("custNo", customerNo);
            urlBuilder.addQueryParameter("accountNumber", accountNumber);
            urlBuilder.addQueryParameter("apiKey", apiKey);
            urlBuilder.addQueryParameter("authId", authId);
            urlBuilder.addQueryParameter("appId", appId);
            urlBuilder.addQueryParameter("clientUrl", configx.getBankBaseUrl(institutionCD) + "card");
            String url = urlBuilder.build().toString();

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for bank ", configx.getBankBaseUrl(institutionCD) + "card"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "getCards"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for get cards", url));

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            LOGGER.log(Level.SEVERE, String.format("\n %s - %s", "bank response", responseBody));
            return responseBody;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARD EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }

    @Override
    public String hotlistCard(String accountNumber, String cardPan, String custNo, String institutionCD) throws Exception {
        try {
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            HotlistCardRequest acctReq = new HotlistCardRequest();
            ObjectMapper mapper = new ObjectMapper();
            acctReq.setApiKey(apiKey);
            acctReq.setAuthId(authId);
            acctReq.setAppId(appId);
            acctReq.setCustNo(custNo);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setCardPan(cardPan);
            acctReq.setClientUrl(configx.getBankBaseUrl(institutionCD) + "card/hotlist");
            //clientUrl = "";
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "hotlistCard"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for hotlist card", ss));

            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "hotlistCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "bank response", rBody));
            return rBody;

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "HOTLIST CARD EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
            //nameVO.setResponseMessage(ResponseCode.GENERAL_ERROR_MESSAGE);
        }
        //return nameVO;

    }

    @Override
    public String replaceCard(String accountNumber, String cardPan, String custNo, String deliveryAddress, String deliveryType, String institutionCD) throws Exception {
        LOGGER.log(Level.SEVERE, String.format("%s - %s", "replace CARD EXCEPTION", configx.getBankBaseUrl(institutionCD)), configx.getBankGatewayUrl(institutionCD));
        try {
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            ReplaceCard acctReq = new ReplaceCard();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setApiKey(apiKey);
            acctReq.setAuthId(authId);
            acctReq.setAppId(appId);

            acctReq.setCustNo(custNo);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setCardPan(cardPan);
            //acctReq.setNameOnCard(nameOnCard);
            acctReq.setDeliveryType(deliveryType);
            acctReq.setDeliveryAddress(deliveryAddress);
            acctReq.setClientUrl(configx.getBankBaseUrl(institutionCD) + "card/replacement");

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "replaceCard"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for replace card", ss));

            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "replaceCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "bank response", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "REPLACE CARD EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }

    @Override
    public String requestCard(String accountNumber, String cardType, String custNo, String nameOnCard, String institutionCD, String branchCode) throws Exception {
        try {
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            RequestCard acctReq = new RequestCard();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setApiKey(apiKey);
            acctReq.setAuthId(authId);
            acctReq.setAppId(appId);

            acctReq.setCustNo(custNo);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setCardType(cardType);
            acctReq.setNameOnCard(nameOnCard);
            acctReq.setClientUrl(configx.getBankBaseUrl(institutionCD) + "card/request");
            acctReq.setBranchCode(branchCode);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "requestCard"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for request card", ss));
            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "requestCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "bank response", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "REQUEST CARD", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }

    public String getCustomerDetails(String cardPan, String cardPin, String institutionCD) throws Exception {
        RequestCard acctReq = new RequestCard();
        try {
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            ObjectMapper mapper = new ObjectMapper();
            CustDetailsRequest custRequest = new CustDetailsRequest();

//            custRequest.setApiKey(apiKey);
//            custRequest.setAuthId(authId);
//            custRequest.setAppId(appId);
//            
//            
//            
            custRequest.setCardPan(cardPan);
            custRequest.setCardPin(cardPin);

            custRequest.setClientUrl(configx.getBankBaseUrl(institutionCD) + "card/accountDetails");

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "card/accountDetails"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for get customer details", ss));

            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "card/accountDetails")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "bank response", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "CARD GET ACCOUNT DETAILS", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }
    }

    @Override
    public String unblockCard(String accountNumber, String custNo, String cardPan, String institutionCD) throws Exception {
        try {
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            UnblockCardRequest acctReq = new UnblockCardRequest();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setApiKey(apiKey);
            acctReq.setAuthId(authId);
            acctReq.setAppId(appId);

            acctReq.setCustNo(custNo);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setCardPan(cardPan);
            acctReq.setClientUrl(configx.getBankBaseUrl(institutionCD) + "card/unblock");

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "unblockCard"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for unblock card", ss));
            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "unblockCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "bank response", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "UNBLOCK CARD", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }
}