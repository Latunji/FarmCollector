/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interswitch.bifrost.cardservice.exception.CustomException;
import com.interswitch.bifrost.cardservice.model.CardAction;
import com.interswitch.bifrost.cardservice.model.CardAudit;
import com.interswitch.bifrost.cardservice.model.Customer;
import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import com.interswitch.bifrost.cardservice.model.repo.CardRepository;
import com.interswitch.bifrost.cardservice.model.repo.CustomerRepository;
import com.interswitch.bifrost.cardservice.request.GenericRequest;
import com.interswitch.bifrost.cardservice.response.*;
import com.interswitch.bifrost.cardservice.service.bankws.CardWS;
import com.interswitch.bifrost.cardservice.service.vo.ProdInstitutionCode;
import com.interswitch.bifrost.cardservice.service.vo.TestInstitutionCode;
import com.interswitch.bifrost.cardservice.util.ConfigProperties;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import com.interswitch.bifrost.cardservice.vo.BaseResponse;
import com.interswitch.bifrost.cardservice.vo.BlockCardResponse;
import com.interswitch.bifrost.cardservice.vo.CardResponse;
import com.interswitch.bifrost.cardservice.vo.HotlistResponse;
import com.interswitch.bifrost.cardservice.vo.RequestCardResponse;
import com.interswitch.bifrost.cardservice.vo.ResponseCode;
import net.bytebuddy.pool.TypePool;
import org.springframework.stereotype.Component;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
//import com.interswitch.bifrost.commons.

/**
 *
 * @author chidiebere.onyeagba
 */
@Component
public class CardService {

    private static final Logger LOGGER = Logger.getLogger(CardService.class.getName());

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardWS cardWS;

    @Autowired
    private ConfigProperties configx;

    SecurityCipher cipher = new SecurityCipher();

    @Value("${isw.token.wallet}")
    String tokenWalletId;

    @Value("${isw.token.key}")
    String tokenKey;

    @Value("${isw.token.issuer}")
    String tokenIssuerId;


    private ServiceResponse activateTransaction(String deviceId, String accountNumber, String institutionCD) {
        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "error in processing");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("device is not valid ");
            return response;
        }
        try {

            String bankserviceResponseJSON = cardWS.activateCustomerTransaction(deviceId, accountNumber, institutionCD);
            Gson gs = new Gson();
            response = gs.fromJson(bankserviceResponseJSON, ServiceResponse.class);

            return response;
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "ACTIVATE CUSTOMER ERROR ", ex));
            return new ServiceResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public ServiceResponse validateCustomerWithAccount(String deviceId, String accountNumber, String institutionCD) {
        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "error in processing");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("device is not valid ");
            return response;
        }
        try {

            String bankserviceResponseJSON = cardWS.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service for validate card", bankserviceResponseJSON));
            Gson gs = new Gson();
            response = gs.fromJson(bankserviceResponseJSON, ServiceResponse.class);

            return response;
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "VALIDATE CUSTOMER WITH ACCOUNT  ERROR ", ex));
            return new ServiceResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public CardsResponse getMyCards(String accountNumber, String deviceId, String custNum, String institutionCD) {

        CardsResponse response = new CardsResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(custNum)) {
            response.setDescription("customer number is blank");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            String custNo;
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            if (resp != null) {
                if (resp.getCode() != 0) {
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                    return new CardsResponse(ResponseCode.ERROR, "ERROR VALIDATING CUSTOMER");
                }
            } else {
                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                return new CardsResponse(ResponseCode.ERROR_INTERNAL, "NO DATA FROM VALIDATION");
            }
            String bankserviceResponseJSON = "";

                bankserviceResponseJSON = cardWS.getCards(accountNumber, custNum, institutionCD);
                LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));
                // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
                Gson gs = new GsonBuilder()
                        .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                        .create();

                GetCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetCardResponse.class);
                LOGGER.log(Level.INFO, String.format("%s - %s", " response ", bankResp));
                if (bankResp != null || bankResp.getCards()  != null)
                {
                    response.setMaskedCards(accountNumber, bankResp.getCards());
                    response.setCode(0);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                }
                else{
                    response.setDescription("No Cards Found");
                }
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET CARDS ERROR ", ex));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
        return response;
    }


    public PtmfbCardsResponse ptmfbGetMyCards(String accountNumber, String deviceId, String custNum, String institutionCD) {

        PtmfbCardsResponse response = new PtmfbCardsResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(custNum)) {
            response.setDescription("customer number is blank");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            String custNo;
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            if (resp != null) {
                if (resp.getCode() != 0) {
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                    return new PtmfbCardsResponse(ResponseCode.ERROR, "ERROR VALIDATING CUSTOMER");
                }
            } else {
                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                return new PtmfbCardsResponse(ResponseCode.ERROR_INTERNAL, "NO DATA FROM VALIDATION");
            }
            String bankserviceResponseJSON = "";

                bankserviceResponseJSON = cardWS.getPtmfbCards(accountNumber, custNum, institutionCD);
                LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));
                // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
                Gson gs = new GsonBuilder()
                        .create();


                GetPtmfbCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetPtmfbCardResponse.class);
                if (bankResp != null && bankResp.getCards() != null)
                {
                    response.setCards(bankResp.getCards());
                    response.setCode(0);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response ", response.toString()));
                }else if(bankResp != null && !bankResp.isSuccessful()) {
                    response.setDescription(bankResp.getResponseDescription());
                    response.setCode(ResponseCode.ERROR);
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response ", response.toString()));
                }else{
                    response.setDescription(ResponseCode.GENERAL_ERROR_MESSAGE);
                    response.setCode(ResponseCode.ERROR);
                }
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET PTMFB CARDS ERROR ", ex));
            return new PtmfbCardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
        return response;
    }

    public static String decryptResponse(String algorithm, String cipherText, String key,
            String iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, originalKey, ivParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);

    }

    public CardPanDetailsResponse providusGetCards(String deviceId, String institutionCD) {
         LOGGER.log(Level.INFO, "GET PROVIDUS CARDS INITIALIZED");

        CardPanDetailsResponse response = new CardPanDetailsResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }

        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        
        deviceId = deviceId.trim();
        institutionCD = institutionCD.trim();
        
        LOGGER.log(Level.INFO, String.format("instititionCD: %s - deviceId: %s", institutionCD, deviceId));
        String custNum;
        try {

            System.out.print("Before customer repo");
            CustomerDevice customerDevice = customerRepo.findByCustomerDeviceAndInstitution(deviceId, institutionCD);

            if (customerDevice == null) {
                return new CardPanDetailsResponse(ResponseCode.ERROR, "Customer device does not exist");
            }

            Customer customer = customerDevice.getCustomer();

            if (customer == null) {

                return new CardPanDetailsResponse(ResponseCode.ERROR, "Customer does not exist");
            }

            custNum = customer.getCustNo();

            LOGGER.log(Level.INFO, String.format("customer Number - %s", custNum));

            if (custNum.isEmpty()) {
                return new CardPanDetailsResponse(ResponseCode.ERROR, "CustNo does not exist");
            }
        }catch (Exception e){
            LOGGER.log(Level.INFO, "Exception fetchimg customer details", e);
            return new CardPanDetailsResponse(ResponseCode.ERROR, "Unable to process your request, try again.");
        }
        try {
            LOGGER.log(Level.INFO, "Before gatewaycall");
            String bankserviceResponseJSON = cardWS.getProvidusCards(custNum, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));
 
            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetTokenizationResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetTokenizationResponse.class);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankResp));
            LOGGER.log(Level.INFO, "gateway responsecode  -----------" + bankResp.getResponseCode());
            if (bankResp != null && bankResp.getResponseCode().equalsIgnoreCase("0"))//&& bankResp.length > 0) 
            {
                LOGGER.log(Level.INFO, "gateway was successful");
                String cipherText = bankResp.getText();
                String mockCipherText = "ZzkfDVjhi392+Xk6tMNln6kNLg5nPkGkUQ1ICCjpagpzNB+e0nJde6z8sTN6W+4bTA5VseTcP04yeXkOOLS8vtmPuI+gf16Z2o6cQzCnWbIFr/nSV6yqMHn3IZAN++oeNkX3I4er2YrL0mu/91x6fAwgWEfVq7Vq6NqIdzlVZhYu7k2sqWxIZ1/J/kFBwyHwNc3OzzhH+3PzVA3pUO4WF9gKArf0knlMl1aYNViHYvCa/GL2DqZ3D5EVP3d4kxhsWbsL4hLOgyMxSK9m1gmuYkudCe54Hzd82cu/qpnox41vnvMhwUAHOYHJlQtwx9LnLtfGAxe6QItd5nvthmkJDWWLa+vH48FB0OqOU0/3xWs=";

                if (custNum.equals("65347")) {
                    cipherText = mockCipherText;
                }
                String algorithm = "AES/CBC/PKCS5Padding";
                String secretKey = configx.getSecretKeyProvidus(institutionCD);
                String iv = configx.getIvProvidus(institutionCD);

                String cardDetails = decryptResponse(algorithm, cipherText, secretKey, iv);

                CardPanDetailsResponse panDetails = gs.fromJson(cardDetails, CardPanDetailsResponse.class);

                response.setCode(0);
                response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                response.setPans(panDetails.getPans());

                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            response.setCode(10);
            return response;
        }
        catch (Exception ex) {
            LOGGER.log(Level.INFO, String.format(" %s- %s", "GET CARDS ERROR ", ex));
            return new CardPanDetailsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }
    public ViewCardStatusResponse providusViewCardStatus(String cardPan, String deviceId, String institutionCD) {
        LOGGER.log(Level.INFO, "VIEW PROVIDUS CARDS STATUS INITIALIZED");

        ViewCardStatusResponse response = new ViewCardStatusResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }

        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }

        if (StringUtils.isBlank(cardPan)){
            response.setDescription("Card pan cannot be blank");
        }

        String custNum;

        deviceId = deviceId.trim();
        institutionCD = institutionCD.trim();

        LOGGER.log(Level.INFO, String.format("instititionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {

            CustomerDevice customerDevice = customerRepo.findByCustomerDeviceAndInstitution(deviceId, institutionCD);

            if (customerDevice == null) {
                return new ViewCardStatusResponse(ResponseCode.ERROR, "Customer device does not exist");
            }

            Customer customer = customerDevice.getCustomer();

            if (customer == null) {
                return new ViewCardStatusResponse(ResponseCode.ERROR, "Customer does not exist");
            }

            custNum =  customer.getCustNo();

        }catch (Exception e){
            LOGGER.log(Level.INFO, "Exception fetching customer details", e);
            return new ViewCardStatusResponse(ResponseCode.ERROR, "Unable to process your request, try again.");
        }
        try {
            LOGGER.log(Level.INFO, "Before gatewaycall");
            String bankserviceResponseJSON = cardWS.viewProvidusCardStatus(cardPan, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));

            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetTokenizationResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetTokenizationResponse.class);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankResp));
            LOGGER.log(Level.INFO, "gateway responsecode  -----------" + bankResp.getResponseCode());
            if (bankResp != null && bankResp.getResponseCode().equalsIgnoreCase("0"))
            {
                LOGGER.log(Level.INFO, "gateway was successful");
                String cipherText = bankResp.getText();
                String mockCipherText = "ZzkfDVjhi392+Xk6tMNln6kNLg5nPkGkUQ1ICCjpagpzNB+e0nJde6z8sTN6W+4bTA5VseTcP04yeXkOOLS8vtmPuI+gf16Z2o6cQzCnWbIFr/nSV6yqMHn3IZAN++oeNkX3I4er2YrL0mu/91x6fAwgWEfVq7Vq6NqIdzlVZhYu7k2sqWxIZ1/J/kFBwyHwNc3OzzhH+3PzVA3pUO4WF9gKArf0knlMl1aYNViHYvCa/GL2DqZ3D5EVP3d4kxhsWbsL4hLOgyMxSK9m1gmuYkudCe54Hzd82cu/qpnox41vnvMhwUAHOYHJlQtwx9LnLtfGAxe6QItd5nvthmkJDWWLa+vH48FB0OqOU0/3xWs=";

                if (custNum.equals("65347")) {
                    cipherText = mockCipherText;
                }
                String algorithm = "AES/CBC/PKCS5Padding";
                String secretKey = configx.getSecretKeyProvidus(institutionCD);
                String iv = configx.getIvProvidus(institutionCD);

                String cardDetails = decryptResponse(algorithm, cipherText, secretKey, iv);

                ViewCardStatusResponse statusResponse = gs.fromJson(cardDetails, ViewCardStatusResponse.class);

                response.setPan(statusResponse.getPan());
                response.setCardStatus(statusResponse.getCardStatus());
                response.setHoldResponseCode(statusResponse.getHoldResponseCode());
                response.setExpiryDate(statusResponse.getExpiryDate());
                response.setSequenceNumber(statusResponse.getSequenceNumber());
                response.setCode(0);
                response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);

                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            response.setCode(10);
            return response;
        }
        catch (Exception ex) {
            LOGGER.log(Level.INFO, String.format(" %s- %s", "VIEW CARDS STATUS ERROR ", ex));
            return new ViewCardStatusResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public GetTokenizationResponse providusHotlistCard(String cardPan, String currency, String deviceId, String institutionCD) {
        LOGGER.log(Level.INFO, "PROVIDUS HOTLIST CARDS INITIALIZED");

        GetTokenizationResponse response = new GetTokenizationResponse();
        if (StringUtils.isBlank(deviceId)) {
            response.setText("Invalid device");
            response.setResponseCode("10");
            return response;
        }

        if (StringUtils.isBlank(institutionCD)) {
            response.setText("institution code is blank");
            response.setResponseCode("10");
            return response;
        }

        if (StringUtils.isBlank(cardPan)){
            response.setText("Card pan cannot be blank");
            response.setResponseCode("10");
        }
        if (StringUtils.isBlank(currency)){
            response.setText("Currency cannot be blank");
            response.setResponseCode("10");
        }

        deviceId = deviceId.trim();
        institutionCD = institutionCD.trim();

        LOGGER.log(Level.INFO, String.format("institutionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {

            CustomerDevice customerDevice = customerRepo.findByCustomerDeviceAndInstitution(deviceId, institutionCD);

            if (customerDevice == null) {
                response.setText("Customer device does not exist");
                response.setResponseCode("10");
                return response;
            }

            Customer customer = customerDevice.getCustomer();
            

            if (customer == null) {
                response.setText("Customer does not exist");
                response.setResponseCode("10");
                return response;
            }

        }catch (Exception e){
            LOGGER.log(Level.INFO, "Exception fetching customer details", e);
            response.setText("Customer does not exist");
            response.setResponseCode("10");
            return response;
        }
        try {
            LOGGER.log(Level.INFO, "Before gatewaycall");
            String bankserviceResponseJSON = cardWS.providusHotlistCard(cardPan, currency, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));

            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetTokenizationResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetTokenizationResponse.class);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankResp));
            LOGGER.log(Level.INFO, "gateway responsecode  -----------" + bankResp.getResponseCode());
            if (bankResp != null && bankResp.getResponseCode().equalsIgnoreCase("0"))
            {
                LOGGER.log(Level.INFO, "gateway was successful");
                response.setText(bankResp.getText());
                response.setResponseCode("0");
                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                return response;
            }
            response.setText("NO VALUE OBTAINED");
            response.setResponseCode("10");
            return response;
        }
        catch (Exception ex) {
            LOGGER.log(Level.INFO, String.format(" %s- %s", "HOTLIST CARD ERROR ", ex));
            response.setText(ResponseCode.GENERAL_ERROR_MESSAGE);
            response.setResponseCode("10");
        }

        return response;
    }

    public GetTokenizationResponse providusDeHotlistCard(String cardPan, String currency, String deviceId, String institutionCD) {
        LOGGER.log(Level.INFO, "PROVIDUS DEHOTLIST CARD INITIALIZED");

        GetTokenizationResponse response = new GetTokenizationResponse();
        if (StringUtils.isBlank(deviceId)) {
            response.setText("Invalid device");
            response.setResponseCode("10");
            return response;
        }

        if (StringUtils.isBlank(institutionCD)) {
            response.setText("institution code is blank");
            response.setResponseCode("10");
            return response;
        }

        if (StringUtils.isBlank(cardPan)){
            response.setText("Card pan cannot be blank");
            response.setResponseCode("10");
        }
        if (StringUtils.isBlank(currency)){
            response.setText("Currency cannot be blank");
            response.setResponseCode("10");
        }

        deviceId = deviceId.trim();
        institutionCD = institutionCD.trim();

        LOGGER.log(Level.INFO, String.format("institutionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {

            CustomerDevice customerDevice = customerRepo.findByCustomerDeviceAndInstitution(deviceId, institutionCD);

            if (customerDevice == null) {
                response.setText("Customer device does not exist");
                response.setResponseCode("10");
                return response;
            }

            Customer customer = customerDevice.getCustomer();

            if (customer == null) {
                response.setText("Customer does not exist");
                response.setResponseCode("10");
                return response;
            }

        }catch (Exception e){
            LOGGER.log(Level.INFO, "Exception fetching customer details", e);
            response.setText("Customer does not exist");
            response.setResponseCode("10");
            return response;
        }
        try {
            LOGGER.log(Level.INFO, "Before gatewaycall");
            String bankserviceResponseJSON = cardWS.providusDehotlistCard(cardPan, currency, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));

            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetTokenizationResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetTokenizationResponse.class);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankResp));
            LOGGER.log(Level.INFO, "gateway responsecode  -----------" + bankResp.getResponseCode());
            if (bankResp != null && bankResp.getResponseCode().equalsIgnoreCase("0"))
            {
                LOGGER.log(Level.INFO, "gateway was successful");
                response.setText(bankResp.getText());
                response.setResponseCode("0");
                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                return response;
            }
            response.setText("NO VALUE OBTAINED");
            response.setResponseCode("10");
            return response;
        }
        catch (Exception ex) {
            LOGGER.log(Level.INFO, String.format(" %s- %s", "DEHOTLIST CARD ERROR ", ex));
            response.setText(ResponseCode.GENERAL_ERROR_MESSAGE);
            response.setResponseCode("10");
        }

        return response;
    }




    public ServiceResponse activateAccountWithCard(String accountNumber, String deviceId, String missingDigits, String custNo, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "error");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(missingDigits)) {
            response.setDescription("please provide masked card pan");
            return response;
        }
        String bankserviceResponseJSON = "";
        try {

            bankserviceResponseJSON = cardWS.getCards(accountNumber, custNo, institutionCD);
            Gson gs = new Gson();

            GetCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetCardResponse.class);
            if (bankResp != null)//&& bankResp.length > 0) 
            {
                int code = Integer.parseInt(bankResp.getResponseCode());
                if (code != 0) {
                    return new ServiceResponse(ResponseCode.ERROR, "NO CARDS");
                }
                List<GetCardResponse.Cards> cards = bankResp.getCards();
                boolean isValidCard = this.validateCard(missingDigits, cards);
                //response.setCards(bankResp.getCards());
                if (isValidCard == false) {
                    return new ServiceResponse(ResponseCode.ERROR, "INVALID CARD");
                }

//                ServiceResponse activateTransaction = this.activateTransaction(deviceId,accountNumber);
//                
//                if(activateTransaction.getCode() != 0)
//                {
//                    return new ServiceResponse(ResponseCode.ERROR, "UNABLE TO ACTIVATE ACCOUNT TRY AGAIN LATER");
//                }
                response.setCode(0);
                response.setDescription("ACCOUNT ACTIVATION SUCCESSFUL");
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            return response;
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "ACTIVATE CUSTOMER WITH CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE + ex);
        }
    }

    private boolean validateCard(String missingValues, List<GetCardResponse.Cards> cards) {
        boolean validCard = false;
        List<GetCardResponse.Cards> cardList = cards;
        for (GetCardResponse.Cards card : cardList) {
            int length = card.getCardPan().length();
            int offset = length - 8;
            LOGGER.info(String.format(" %s- %s <--> %s", "VALIDATE CARD PAN", card.getCardPan().substring(offset, offset + 4), missingValues));
            //card.setCardPan(card.getCardPan().substring(0, offset).concat("****").concat(card.getCardPan().substring(length-4,length)));
            if (missingValues.equals(card.getCardPan().substring(offset, offset + 4))) {
                return true;
            }
        }
        return validCard;
    }

    private List<GetCardResponse.Cards> maskCard(List<GetCardResponse.Cards> cards) {
        List<GetCardResponse.Cards> cardList = cards;
        for (GetCardResponse.Cards card : cardList) {
            int length = card.getCardPan().length();
            int offset = length - 8;
            card.setCardPan(card.getCardPan().substring(0, offset).concat("****").concat(card.getCardPan().substring(length - 4, length)));
        }

        return cardList;
    }

    public CustomerDetailsResponse getCustomerDetails(String cardPan, String cardPin, String institutionCD) {
        CustomerDetailsResponse response = new CustomerDetailsResponse(ResponseCode.ERROR, "No card detail available");
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        if (StringUtils.isBlank(cardPan)) {
            response.setDescription("card pan is blank");
            return response;
        }
        if (StringUtils.isBlank(cardPin)) {
            response.setDescription("card pin is blank");
            return response;
        }

        try {
            Gson gs = new Gson();
            String bankserviceResponseJSON = cardWS.getCustomerDetails(cardPan, cardPin, institutionCD);
            response = gs.fromJson(bankserviceResponseJSON, CustomerDetailsResponse.class);

        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET CARD CUSTOMER DETAILS ERROR ", ex));
            return new CustomerDetailsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
        return response;
    }

    public ServiceResponse hotlistCards(GenericRequest payload, String deviceId, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");
        String cardPan;
        if (StringUtils.isBlank(payload.getAccountNumber())) {
            response.setDescription("account number is blank");
            return response;
        }
        if(institutionCD.equalsIgnoreCase(TestInstitutionCode.PTMFB.getInstitutionCD()) || institutionCD.equalsIgnoreCase(ProdInstitutionCode.PTMFB.getInstitutionCD())) {
            if(StringUtils.isBlank(payload.getSerialNo())){
                response.setDescription("serial no is blank");
                return response;
            }
            if(StringUtils.isBlank(payload.getReason())){
                response.setDescription("reason is blank");
                return response;
            }
        }else{
            if (StringUtils.isBlank(payload.getCardPan())) {
                response.setDescription("cardpan is blank");
                return response;
            }
        }

        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("Invalid institution code");
            return response;
        }
        String bankserviceResponseJSON = "";

        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, payload.getAccountNumber(), institutionCD);
            String custNo;
            if (resp.getCode() == 0) {
                custNo = resp.getDescription();
            } else {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }

            if(institutionCD.equalsIgnoreCase(TestInstitutionCode.PTMFB.getInstitutionCD()) || institutionCD.equalsIgnoreCase(ProdInstitutionCode.PTMFB.getInstitutionCD())) {

                bankserviceResponseJSON = cardWS.hotlistPtmfbCard(payload.getAccountNumber(), payload.getSerialNo(), payload.getReason(), generateReference(null), institutionCD);

                // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
                Gson gs = new Gson();

                HotlistResponse bankResp = gs.fromJson(bankserviceResponseJSON, HotlistResponse.class);

                if (bankResp != null && bankResp.isSuccessful() == true) {
                    response.setCode(ResponseCode.SUCCESS);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                } else if (bankResp != null && bankResp.isSuccessful() == false) {
                    response.setCode(ResponseCode.ERROR);
                    response.setDescription(bankResp.getResponseMessage());
                } else {
                    response.setDescription("NO VALUE OBTAINED");
                }
            }else{
                cardPan = this.decrypt(payload.getAccountNumber(), payload.getCardPan());
                LOGGER.info(String.format(" %s- %s", "card Pan", cardPan));

                bankserviceResponseJSON = cardWS.hotlistCard(payload.getAccountNumber(), cardPan, custNo, institutionCD);

                // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
                Gson gs = new Gson();

                BaseResponse bankResp = gs.fromJson(bankserviceResponseJSON, BaseResponse.class);

                if (bankResp != null && Integer.parseInt(bankResp.getResponseCode()) != 0) {
                    response.setCode(ResponseCode.ERROR);
                    response.setDescription(bankResp.getResponseMessage());
                } else if (bankResp != null && Integer.parseInt(bankResp.getResponseCode()) == 0) {
                    response.setCode(ResponseCode.SUCCESS);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                } else {
                    response.setDescription("NO VALUE OBTAINED");
                }
            }
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s -%s", "HOTLIST CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getMessage());
        }
        return response;
    }

    public ServiceResponse blockCard(GenericRequest payload, String deviceId, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(payload.getAccountNumber())) {
            response.setDescription("account number is blank");
            return response;
        }
        if(StringUtils.isBlank(payload.getSerialNo())){
                response.setDescription("serial no is blank");
                return response;
        }
        if(StringUtils.isBlank(payload.getReason())){
                response.setDescription("reason is blank");
                return response;
        }
        if(StringUtils.isBlank(payload.getBlock().toString())){
            response.setDescription("block is blank");
            return response;
        }
        String bankserviceResponseJSON = "";

        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, payload.getAccountNumber(), institutionCD);
            if (resp == null || resp.getCode() != 0) {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }

                bankserviceResponseJSON = cardWS.blockandUnblockPtmfbCard(payload.getAccountNumber(), payload.getSerialNo(), payload.getReason(), generateReference(null), institutionCD, payload.getBlock());
                Gson gs = new Gson();

                BlockCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, BlockCardResponse.class);

                if (bankResp != null && bankResp.isIsSuccessful() == true) {
                    CardAudit cardAudit = new CardAudit();
                    cardAudit.setReason(payload.getReason());
                    cardAudit.setDate(new Date());
                    if(payload.getBlock() == true){
                        cardAudit.setAction(CardAction.BLOCK.getAction());
                    }
                    else{
                        cardAudit.setAction(CardAction.UNBLOCK.getAction());
                    }
                    cardAudit.setAccountNumber(payload.getAccountNumber());
                    cardRepository.save(cardAudit);

                    response.setCode(ResponseCode.SUCCESS);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                } else if (bankResp != null && bankResp.isIsSuccessful() == false) {
                    response.setCode(ResponseCode.ERROR);
                    response.setDescription(bankResp.getResponseMessage());
                } else {
                    response.setDescription("NO VALUE OBTAINED");
                }
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s -%s", "BLOCK/UNBLOCK CARD ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getMessage());
        }
        return response;
    }

    private String decrypt(String accountNumber, String cardPan) throws CustomException {
        String response = "";
        try {
            response = cipher.decrypt(accountNumber, cardPan);
        }
        catch (NoSuchAlgorithmException
                | InvalidKeySpecException
                | NoSuchPaddingException
                | InvalidKeyException
                | IOException
                | InvalidAlgorithmParameterException
                | // UnsupportedEncodingException|
                IllegalBlockSizeException
                | BadPaddingException ex) {
            LOGGER.info(String.format(" %s- %s", "DECRYPTION ERROR ", ex));
            throw new CustomException(ex.getMessage(), "Enter valid credentials");
        }
        return response;
    }

    public ServiceResponse requestCard(GenericRequest request, String deviceId, String institutionCD) {
        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");

        if (StringUtils.isBlank(request.getAccountNumber())) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(request.getNameOnCard())) {
            response.setDescription("name on card is blank");
            return response;
        }
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("Invalid institution code");
            return response;
        }

        if(institutionCD.equalsIgnoreCase(TestInstitutionCode.PTMFB.getInstitutionCD()) || institutionCD.equalsIgnoreCase(ProdInstitutionCode.PTMFB.getInstitutionCD())){
            if(StringUtils.isBlank(request.getBin())){
                response.setDescription("card bin is blank");
            }
            if(StringUtils.isBlank(request.getDeliveryOption())){
                response.setDescription("delivery option is blank");
            }
            if(StringUtils.isBlank(request.getRequestType())){
                response.setDescription("request type is blank");
            }
        }else {
            if (StringUtils.isBlank(request.getCardType())) {
                response.setDescription("card type is blank");
                return response;
            }
            if (StringUtils.isBlank(request.getBranchCode())) {
                response.setDescription("Please provide a branch code");
                return response;
            }
        }
        String bankserviceResponseJSON = "";
        Gson gs = new Gson();

        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, request.getAccountNumber(), institutionCD);
            String custNo;
            if (resp.getCode() == 0) {
                custNo = resp.getDescription();
            } else {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }

            if (institutionCD.equalsIgnoreCase(TestInstitutionCode.PTMFB.getInstitutionCD()) || institutionCD.equalsIgnoreCase(ProdInstitutionCode.PTMFB.getInstitutionCD())) {
                bankserviceResponseJSON = cardWS.requestPtmfbCard(request.getAccountNumber(), request.getRequestType(), request.getCustNo(), request.getNameOnCard(), institutionCD, request.getBin(), request.getDeliveryOption());

                RequestCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, RequestCardResponse.class);
                if (bankResp != null && bankResp.isSuccessful() == true) {
                    CardAudit cardAudit = new CardAudit();
                    cardAudit.setReason(request.getReason());
                    cardAudit.setDate(new Date());
                    cardAudit.setAction(CardAction.REQUEST.getAction());
                    cardAudit.setAccountNumber(request.getAccountNumber());
                    cardRepository.save(cardAudit);

                    response.setCode(ResponseCode.SUCCESS);
                    response.setDescription(bankResp.getResponseMessage());
                } else if (bankResp != null && bankResp.isSuccessful() == false) {
                    response.setCode(ResponseCode.ERROR);
                    response.setDescription(bankResp.getResponseMessage());
                } else {
                    response.setDescription("NO VALUE OBTAINED");
                }
            } else {
                bankserviceResponseJSON = cardWS.requestCard(request.getAccountNumber(), request.getCardType(), custNo, request.getNameOnCard(), institutionCD, request.getBranchCode());

                BaseResponse bankResp = gs.fromJson(bankserviceResponseJSON, BaseResponse.class);
                if (bankResp != null && Integer.parseInt(bankResp.getResponseCode()) != 0) {
                    response.setCode(ResponseCode.ERROR);
                    response.setDescription(bankResp.getResponseMessage());
                } else if (bankResp != null && Integer.parseInt(bankResp.getResponseCode()) == 0) {
                    response.setCode(ResponseCode.SUCCESS);
                    response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                } else {
                    response.setDescription("NO VALUE OBTAINED");
                }
            }
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s -%s", "REQUEST CARD ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
            return response;
    }

    public ServiceResponse replacCard(String accountNumber, String deviceId, String cardPan, String deliveryType, String deliveryAddress, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");

        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(cardPan)) {
            response.setDescription("card pan is blank");
            return response;
        }
        if (StringUtils.isBlank(deliveryType)) {
            response.setDescription("delivery type is blank");
            return response;
        }
        if (StringUtils.isBlank(deliveryAddress)) {
            response.setDescription("delivery address is blank");
            return response;
        }

        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("Invalid institution code");
            return response;
        }
        String bankserviceResponseJSON = "";

        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            String custNo;
            if (resp.getCode() == 0) {
                custNo = resp.getDescription();
            } else {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }
            cardPan = this.decrypt(accountNumber, cardPan);

            bankserviceResponseJSON = cardWS.replaceCard(accountNumber, cardPan, custNo, deliveryAddress, deliveryType, institutionCD);

            // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
            Gson gs = new Gson();

            BaseResponse bankResp = gs.fromJson(bankserviceResponseJSON, BaseResponse.class);

            if (bankResp != null)//&& bankResp.length > 0) 
            {
                //response.setCards(bankResp.getCards());
                int code = Integer.parseInt(bankResp.getResponseCode());
                if (code != 0) {
                    code = ResponseCode.ERROR;
                }
                response.setCode(code);

                response.setDescription(bankResp.getResponseMessage());
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            return response;
        }
        catch (CustomException ex) {
            LOGGER.info(String.format(" %s- %s -%s", "REPLACE CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getErrorMessage());
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s - %s", "REPLACE CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public ServiceResponse unblockCard(String accountNumber, String deviceId, String cardPan, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);

        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(cardPan)) {
            response.setDescription("card pan is blank");
            return response;
        }

        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("Invalid institution code");
            return response;
        }
        String bankserviceResponseJSON = "";

        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            String custNo;
            if (resp.getCode() == 0) {
                custNo = resp.getDescription();
            } else {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }
            cardPan = this.decrypt(accountNumber, cardPan);

            bankserviceResponseJSON = cardWS.unblockCard(accountNumber, custNo, cardPan, institutionCD);

            // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
            Gson gs = new Gson();

            BaseResponse bankResp = gs.fromJson(bankserviceResponseJSON, BaseResponse.class);

            if (bankResp != null)//&& bankResp.length > 0) 
            {
                //response.setCards(bankResp.getCards());
                int code = Integer.parseInt(bankResp.getResponseCode());
                if (code != 0) {
                    code = ResponseCode.ERROR;
                }
                response.setCode(code);

                response.setDescription(bankResp.getResponseMessage());
                return response;
            }
            response.setDescription(ResponseCode.GENERAL_ERROR_MESSAGE);
            return response;
        }
        catch (CustomException ex) {
            LOGGER.info(String.format(" %s- %s - %s", "UNBLOCK CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getErrorMessage());
        }
        catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s - %s", "UNBLOCK CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public String calculateJwtToken(String panNumber) throws Exception {
        if (this.tokenWalletId.length() == 0) {
            this.tokenWalletId = "DUMMY";
        }

        PrivateKey privateKey = getPrivateKey(this.tokenKey);

        if (privateKey == null) {
            return "";
        }

        return Jwts.builder()
                .setSubject(this.tokenWalletId)
                .setIssuer(this.tokenIssuerId)
                .setHeaderParam("typ", "JWT")
                .setAudience("enrollment")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 180000L))
                .claim("last4", panNumber)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    private PrivateKey getPrivateKey(String key)
            throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("private.key");
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource
                .getInputStream(), StandardCharsets.UTF_8));

        Throwable localThrowable3 = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (Throwable localThrowable1) {
            localThrowable3 = localThrowable1;
            throw localThrowable1;
        }
        finally {
            if (br != null) {
                if (localThrowable3 != null) {
                    try {
                        br.close();
                    }
                    catch (Throwable localThrowable2) {
                        localThrowable3.addSuppressed(localThrowable2);
                    }
                } else {
                    br.close();
                }
            }
        }
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(sb.toString()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public synchronized static String generateReference(String prefix) {
        String refPrefix = prefix;
        if (prefix == null) {
            return String.valueOf(System.currentTimeMillis() + (new SecureRandom().nextInt(999) + 1));
        }

        return (refPrefix + System.currentTimeMillis() + (new SecureRandom().nextInt(999) + 1)).replaceAll("\r", "");
    }

}
