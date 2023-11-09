/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.bankws.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.bifrost.cardservice.request.ProvidusCardRequest;
import com.interswitch.bifrost.cardservice.service.bankws.CardWS;
import com.interswitch.bifrost.cardservice.service.bankws.request.*;
import com.interswitch.bifrost.cardservice.util.ConfigProperties;
import com.interswitch.bifrost.commons.security.InterServiceSecurityUtil;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//import okhttp3.HttpUrl;
//import com.vanso.proxy.commons.annotations.Mock;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
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

    public String getPtmfbCards(String accountNumber, String customerNo, String institutionCD) {
        LOGGER.log(Level.SEVERE, String.format("%s - %s ", "GET PTMFB CARD ", configx.getBankBaseUrl(institutionCD)), configx.getBankGatewayUrl(institutionCD));
        try {
            String token = configx.getToken(institutionCD);

            PtmfbCardApiRequest acctReq = new PtmfbCardApiRequest();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setAccountNo(accountNumber);
            acctReq.setClientUrl(configx.getBankCardBaseUrl(institutionCD)+"Cards/RetrieveCustomerCards");
            acctReq.setCustomerID(customerNo);
            acctReq.setToken(token);

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "ptmfbGetCards"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Get PTMFB Cards Request", ss));
            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "ptmfbGetCards")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "Bank response for PTMFB get card", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARD", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }
    }

    
    @Override
    public String getProvidusCards(String customerNo, String institutionCD) throws Exception {
        LOGGER.log(Level.INFO, String.format("%s - %s ", "GET CARD-TOKEN INITIALIZED", configx.getBankBaseUrl(institutionCD)), configx.getBankGatewayUrl(institutionCD));
        Response response;
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);
         
            ProvidusCardRequest cred = new ProvidusCardRequest();
            
            cred.setApiKey(apiKey);
            cred.setAuthId(authId);
            cred.setAppId(appId);
            cred.setInstitutionCD(institutionCD);
            cred.setCustNo(customerNo);
            cred.setClientUrl(configx.getBankBaseUrl(institutionCD));
            
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");  
            String ss = mapper.writeValueAsString(cred);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s", "Providus getCard request", ss));   
//
            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getVersionedUrl(institutionCD) + "providusGetCards").newBuilder();
            
            String url = urlBuilder.build().toString();
            LOGGER.log(Level.INFO, "REQUEST {0} : ", ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for bank ", configx.getBankBaseUrl(institutionCD) + "card"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "providusGetCards"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request url for get cards", url));

            Request request = new Request.Builder()
                   .url(url)
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();
         
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            LOGGER.log(Level.SEVERE, String.format("\n %s - %s", "Gateway2 response", responseBody));
            return responseBody;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET Tokenization EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }

    @Override
    public String viewProvidusCardStatus(String cardPan, String institutionCD) throws Exception {
        LOGGER.log(Level.INFO, String.format("%s - %s ", "VIEW CARD-STATUS INITIALIZED",
                configx.getBankBaseUrl(institutionCD)), configx.getBankCardBaseUrl(institutionCD));
        Response response;
        try {
            ObjectMapper mapper = new ObjectMapper();

            String apiKey = configx.getAPIKey(institutionCD);
            String authId = configx.getAuthID(institutionCD);
            String appId = configx.getAppID(institutionCD);

            ProvidusCardRequest cred = new ProvidusCardRequest();

            cred.setApiKey(apiKey);
            cred.setAuthId(authId);
            cred.setAppId(appId);
            cred.setInstitutionCD(institutionCD);

            cred.setClientUrl(configx.getBankCardBaseUrl(institutionCD)+"status/pan/"+cardPan);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(cred);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s", "Providus view Card request", ss));
//
            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getVersionedUrl(institutionCD) + "providus/viewCardStatus").newBuilder();

            String url = urlBuilder.build().toString();
            LOGGER.log(Level.INFO, "REQUEST {0} : ", ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for bank ", cred.getClientUrl()));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "providus/viewCardStatus"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request url for view card status", url));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            LOGGER.log(Level.SEVERE, String.format("\n %s - %s", "Gateway2 response", responseBody));
            return responseBody;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "VIEW CARD STATUS EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }

    }

    @Override
    public String providusHotlistCard(String cardPan, String currency, String institutionCode) throws Exception {
        LOGGER.log(Level.INFO, String.format("%s - %s ", "HOTLIST CARD INITIALIZED",
                configx.getBankBaseUrl(institutionCode)), configx.getBankCardBaseUrl(institutionCode));
        Response response;
        try {
            ObjectMapper mapper = new ObjectMapper();

            String apiKey = configx.getAPIKey(institutionCode);
            String authId = configx.getAuthID(institutionCode);
            String appId = configx.getAppID(institutionCode);
            String clientUrl = configx.getBankCardBaseUrl(institutionCode);

            HotlistCardRequest cred = new HotlistCardRequest();
            cred.setApiKey(apiKey);
            cred.setAppId(appId);
            cred.setAuthId(authId);
            cred.setInstitutionCD(institutionCode);
            cred.setCardPan(cardPan);
            cred.setCurrency(currency);
            cred.setClientUrl(clientUrl);


            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(cred);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s", "Providus hotlist request", ss));

            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getVersionedUrl(institutionCode) + "providus/hotlistCard").newBuilder();

            String url = urlBuilder.build().toString();
            LOGGER.log(Level.INFO, "REQUEST {0} : ", ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for bank ", clientUrl));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCode) + "providus/hotlistCard"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request url for hotlistCard", url));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            LOGGER.log(Level.SEVERE, String.format("\n %s - %s", "Gateway2 response", responseBody));
            return responseBody;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "HOTLIST CARD EXCEPTION", ""), ex);
            return "{\n"
                    + "   \"responseTxt\":\"Error Occured\",\n"
                    + "   \"responseCode\":10"
                    + "}";
        }
    }

    @Override
    public String providusDehotlistCard(String cardPan, String currency, String institutionCode) throws Exception {
        LOGGER.log(Level.INFO, String.format("%s - %s ", "DEHOTLIST CARD INITIALIZED",
                configx.getBankBaseUrl(institutionCode)), configx.getBankCardBaseUrl(institutionCode));
        Response response;
        try {
            ObjectMapper mapper = new ObjectMapper();

            String apiKey = configx.getAPIKey(institutionCode);
            String authId = configx.getAuthID(institutionCode);
            String appId = configx.getAppID(institutionCode);
            String clientUrl = configx.getBankCardBaseUrl(institutionCode);

            HotlistCardRequest cred = new HotlistCardRequest();
            cred.setApiKey(apiKey);
            cred.setAuthId(authId);
            cred.setAppId(appId);
            cred.setInstitutionCD(institutionCode);
            cred.setCardPan(cardPan);
            cred.setCurrency(currency);
            cred.setClientUrl(clientUrl);


            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(cred);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s", "Providus dehotlist request", ss));
//
            HttpUrl.Builder urlBuilder = HttpUrl.parse(configx.getVersionedUrl(institutionCode) + "providus/dehotlistCard").newBuilder();

            String url = urlBuilder.build().toString();
            LOGGER.log(Level.INFO, "REQUEST {0} : ", ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for bank ", clientUrl));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCode) + "providus/dehotlistCard"));
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request url for dehotlistCard", url));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            LOGGER.log(Level.SEVERE, String.format("\n %s - %s", "Gateway2 response", responseBody));
            return responseBody;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "DEHOTLIST CARD EXCEPTION", ""), ex);
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

    @Override
    public String requestPtmfbCard(String accountNumber, String requestType, String custNo, String nameOnCard, String institutionCD, String bin, String deliveryOption) throws Exception {
        try {
            String token = configx.getToken(institutionCD);

            PtmfbCardApiRequest acctReq = new PtmfbCardApiRequest();
            ObjectMapper mapper = new ObjectMapper();


            acctReq.setCustNo(custNo);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setRequestType(requestType);
            acctReq.setNameOnCard(nameOnCard);
            acctReq.setClientUrl(configx.getBankCardBaseUrl(institutionCD) + "Cards/RequestCard");
            acctReq.setBin(bin);
            acctReq.setDeliveryOption(deliveryOption);
            acctReq.setIdentifier("REQ00904930");
            acctReq.setToken(token);

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "requestPtmfbCard"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Request for PTMFB request card", ss));
            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "requestPtmfbCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "Bank response for PTMFB request card", rBody));
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

    @Override
    public String hotlistPtmfbCard(String accountNumber, String serialNo, String reason, String reference, String institutionCD) {
        try {

            String token = configx.getToken(institutionCD);

            PtmfbCardApiRequest acctReq = new PtmfbCardApiRequest();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setReason(reason);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setSerialNo(serialNo);
            acctReq.setReference(reference);
            acctReq.setClientUrl(configx.getBankCardBaseUrl(institutionCD) + "Cards/HotlistCard");
            acctReq.setToken(token);

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", configx.getVersionedUrl(institutionCD) + "hotlistPtmfbCard"));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Hotlist PTMFB Card Request", ss));
            Request request = new Request.Builder()
                    .url(configx.getVersionedUrl(institutionCD) + "hotlistPtmfbCard")
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "Bank response for PTMFB hotlist card", rBody));
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

    @Override
    public String blockandUnblockPtmfbCard(String accountNumber, String serialNo, String reason, String reference, String institutionCD, Boolean block) {
        try {
            String token = configx.getToken(institutionCD);
            String url;

            PtmfbCardApiRequest acctReq = new PtmfbCardApiRequest();
            ObjectMapper mapper = new ObjectMapper();

            acctReq.setReason(reason);
            acctReq.setAccountNumber(accountNumber);
            acctReq.setSerialNo(serialNo);
            acctReq.setReference(reference);
            acctReq.setBlock(block);
            if(block.equals(true)) {
                acctReq.setClientUrl(configx.getBankCardBaseUrl(institutionCD) + "Cards/Freeze");
            }else{
                acctReq.setClientUrl(configx.getBankCardBaseUrl(institutionCD) + "Cards/UnFreeze");
            }
            acctReq.setToken(token);


            url = configx.getVersionedUrl(institutionCD)+"blockAndUnblockCard";
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            String ss = mapper.writeValueAsString(acctReq);
            RequestBody body = RequestBody.create(mediaType, ss);
            LOGGER.log(Level.INFO, String.format("%s - %s\n", "URL for gateway", url));

            LOGGER.log(Level.INFO, String.format("%s - %s\n", "Block/Unblock PTMFB Card Request", ss));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "access token")
                    .addHeader("content-type", "application/json")
                    .build();

            Response rsp = client.newCall(request).execute();
            String rBody = rsp.body().string();
            LOGGER.log(Level.INFO, String.format("\n %s - %s", "Bank response for PTMFB Block/Unblock card", rBody));
            return rBody;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "BLOCK/UNBLOCK CARD", ""), ex);
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
