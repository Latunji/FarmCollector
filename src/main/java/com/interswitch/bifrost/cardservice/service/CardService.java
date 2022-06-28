/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interswitch.bifrost.cardservice.exception.CustomException;
import com.interswitch.bifrost.cardservice.model.Customer;
import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import com.interswitch.bifrost.cardservice.model.repo.CustomerRepository;
import com.interswitch.bifrost.cardservice.response.CardPanDetailsResponse;
import com.interswitch.bifrost.cardservice.response.CardsResponse;
import com.interswitch.bifrost.cardservice.response.CustomerDetailsResponse;
import com.interswitch.bifrost.cardservice.response.GetCardResponse;
import com.interswitch.bifrost.cardservice.response.GetTokenizationResponse;
import com.interswitch.bifrost.cardservice.service.bankws.CardWS;
import com.interswitch.bifrost.cardservice.util.ConfigProperties;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import com.interswitch.bifrost.cardservice.vo.BaseResponse;
import com.interswitch.bifrost.cardservice.vo.ResponseCode;
import org.springframework.stereotype.Component;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
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
import org.springframework.core.io.Resource;
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
    

    //@Value("classpath:private.key")
    //Resource resourceFile;

//    public CustomerDetailsResponse validateCustomer(String deviceId,String institutionCD)
//    {
//        LOGGER.info(String.format(" %s- %s", "VALIDATE CUSTOMER",deviceId));
//        CustomerDetailsResponse response = new CustomerDetailsResponse(ResponseCode.ERROR,"error in processing");
//        if (StringUtils.isBlank(deviceId)) {
//            response.setDescription("device is not valid ");
//            return response;
//        }
//        try {
//            
//            String bankserviceResponseJSON = cardWS.validateCustomer(deviceId,institutionCD);                
//            Gson gs = new Gson();
//            
//            response = gs.fromJson(bankserviceResponseJSON, CustomerDetailsResponse.class);
//            
//            
//            return response;
//        } catch (Exception ex) {
//            LOGGER.info(String.format(" %s- %s", "VALIDATE CUSTOMER ERROR ",ex));
//            return new CustomerDetailsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE );
//        }
//    }
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            if (resp != null) {
                if (resp.getCode() != 0) {
                    LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                    return new CardsResponse(ResponseCode.ERROR, "ERROR VALIDATING CUSTOMER");
                }

            } else {
                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", resp.toString()));
                return new CardsResponse(ResponseCode.ERROR_INTERNAL, "NO DATA FROM VALIDATION");
            }

            String bankserviceResponseJSON = cardWS.getCards(accountNumber, custNum, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));
            // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetCardResponse.class);

            if (bankResp != null && bankResp.getCards().size() > 0)//&& bankResp.length > 0) 
            {

                response.setMaskedCards(accountNumber, bankResp.getCards());
                //response.

                response.setCode(0);
                response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);

                LOGGER.log(Level.SEVERE, String.format("%s - %s", "service response", response.toString()));
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            return response;
        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET CARDS ERROR ", ex));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
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

       CardPanDetailsResponse response = new CardPanDetailsResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
       
       
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        try {
            System.out.print("Before customer repo");
            CustomerDevice customerDevice = customerRepo.findCustomerDeviceAndInstitution(deviceId, institutionCD);
            
            if (customerDevice == null) {
                return new CardPanDetailsResponse(ResponseCode.ERROR, "Customer device does not exist");   
            }
            
             Customer customer = customerDevice.getCustomer();
            if (customer == null){
                return new CardPanDetailsResponse(ResponseCode.ERROR, "Customer does not exist"); 
            }
            
            String custNum = customer.getCustNo();
   
            String accountNumber = customer.getPrimaryAccountNumber();
            System.out.print("Account number is " + accountNumber);
            System.out.print("Account number is " + custNum);
            
            LOGGER.log(Level.INFO, String.format("%s - %s", " response  ", accountNumber, custNum));
            
             if (accountNumber.isEmpty()){
                return new CardPanDetailsResponse(ResponseCode.ERROR, "Account number does not exist");
            }
            
            if (custNum.isEmpty()){
                return new CardPanDetailsResponse(ResponseCode.ERROR, "CustNo does not exist");
            }
            
            System.out.print("Before gatewaycall");
            String bankserviceResponseJSON = cardWS.getProvidusCards(accountNumber, custNum, institutionCD);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankserviceResponseJSON));
            // String bankserviceResponseJSON = backendWS.getAccounts(customer.getPrimaryAccountNumber());
            Gson gs = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            GetTokenizationResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetTokenizationResponse.class);
            LOGGER.log(Level.INFO, String.format("%s - %s", " response from third party service ", bankResp));
             System.out.print("gateway responsecode  -----------" + bankResp.getResponseCode() );
            if (bankResp != null && bankResp.getResponseCode().equalsIgnoreCase("0"))//&& bankResp.length > 0) 
            {
                System.out.print("gateway was successful");
                String cipherText = bankResp.getText();
                String mockCipherText = "ZzkfDVjhi392+Xk6tMNln6kNLg5nPkGkUQ1ICCjpagpzNB+e0nJde6z8sTN6W+4bTA5VseTcP04yeXkOOLS8vtmPuI+gf16Z2o6cQzCnWbIFr/nSV6yqMHn3IZAN++oeNkX3I4er2YrL0mu/91x6fAwgWEfVq7Vq6NqIdzlVZhYu7k2sqWxIZ1/J/kFBwyHwNc3OzzhH+3PzVA3pUO4WF9gKArf0knlMl1aYNViHYvCa/GL2DqZ3D5EVP3d4kxhsWbsL4hLOgyMxSK9m1gmuYkudCe54Hzd82cu/qpnox41vnvMhwUAHOYHJlQtwx9LnLtfGAxe6QItd5nvthmkJDWWLa+vH48FB0OqOU0/3xWs=";
                
                if (custNum.equals("65347")){
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
            return response;
        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET CARDS ERROR ", ex));
            
            return new CardPanDetailsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
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
        } catch (Exception ex) {
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

        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET CARD CUSTOMER DETAILS ERROR ", ex));
            return new CustomerDetailsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
        return response;
    }

    public CardsResponse getMaskedCards(String accountNumber, String deviceId, String custNo, String institutionCD) {

        CardsResponse response = new CardsResponse(ResponseCode.ERROR, "No card available");
        if (StringUtils.isBlank(deviceId)) {
            response.setDescription("Invalid device");
            return response;
        }
        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(custNo)) {
            response.setDescription("customer number is blank");
            return response;
        }
        if (StringUtils.isBlank(institutionCD)) {
            response.setDescription("institution code is blank");
            return response;
        }
        try {
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            //String custNo;
            if (resp != null) {
                if (resp.getCode() != 0) {
                    return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
                }
            } else {
                return new CardsResponse(ResponseCode.ERROR_INTERNAL, "NO DATA FROM VALIDATION");
            }

            String bankserviceResponseJSON = cardWS.getCards(accountNumber, custNo, institutionCD);
            Gson gs = new Gson();

            GetCardResponse bankResp = gs.fromJson(bankserviceResponseJSON, GetCardResponse.class);
            if (bankResp != null && bankResp.getCards().size() > 0)//&& bankResp.length > 0) 
            {

                List<GetCardResponse.Cards> cards = maskCard(bankResp.getCards());

                response.setCards(bankResp.getCards());

                response.setCode(0);
                response.setDescription(ResponseCode.GENERAL_SUCCESS_MESSAGE);
                return response;
            }
            response.setDescription("NO VALUE OBTAINED");
            return response;
        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s", "GET MASKED CARDS ERROR ", ex));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    public ServiceResponse hotlistCards(String accountNumber, String cardPan, String deviceId, String institutionCD) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");

        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(cardPan)) {
            response.setDescription("cardpan is blank");
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
            cardPan = this.decrypt(accountNumber, cardPan);
            LOGGER.info(String.format(" %s- %s", "card Pan", cardPan));
            ServiceResponse resp = this.validateCustomerWithAccount(deviceId, accountNumber, institutionCD);
            String custNo;
            if (resp.getCode() == 0) {
                custNo = resp.getDescription();
            } else {
                return new CardsResponse(ResponseCode.ERROR, "INVALID CUSTOMER");
            }

            bankserviceResponseJSON = cardWS.hotlistCard(accountNumber, cardPan, custNo, institutionCD);

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
        } catch (CustomException ex) {
            LOGGER.info(String.format(" %s- %s -%s", "HOTLIST CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getErrorMessage());
        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s -%s", "HOTLIST CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
    }

    private String decrypt(String accountNumber, String cardPan) throws CustomException {
        String response = "";
        try {
            response = cipher.decrypt(accountNumber, cardPan);
        } catch (NoSuchAlgorithmException
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

    public ServiceResponse requestCard(String accountNumber, String deviceId,
            String cardtype, String nameOnCard, String institutionCD, String branchCode) {

        ServiceResponse response = new ServiceResponse(ResponseCode.ERROR, "No card available");

        if (StringUtils.isBlank(accountNumber)) {
            response.setDescription("account number is blank");
            return response;
        }
        if (StringUtils.isBlank(cardtype)) {
            response.setDescription("card type is blank");
            return response;
        }
        if (StringUtils.isBlank(nameOnCard)) {
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
        if (StringUtils.isBlank(branchCode)) {
            response.setDescription("Please provide a branch code");
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

            bankserviceResponseJSON = cardWS.requestCard(accountNumber, cardtype, custNo, nameOnCard, institutionCD, branchCode);

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
            } else {
                response.setDescription("NO VALUE OBTAINED");
                return response;
            }

        } catch (Exception ex) {
            LOGGER.info(String.format(" %s- %s -%s", "REQUEST CARD ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ResponseCode.GENERAL_ERROR_MESSAGE);
        }
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
        } catch (CustomException ex) {
            LOGGER.info(String.format(" %s- %s -%s", "REPLACE CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getErrorMessage());
        } catch (Exception ex) {
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
        } catch (CustomException ex) {
            LOGGER.info(String.format(" %s- %s - %s", "UNBLOCK CARDS ERROR ", ex, bankserviceResponseJSON));
            return new CardsResponse(ResponseCode.ERROR, ex.getErrorMessage());
        } catch (Exception ex) {
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
        } catch (Throwable localThrowable1) {
            localThrowable3 = localThrowable1;
            throw localThrowable1;
        } finally {
            if (br != null) {
                if (localThrowable3 != null) {
                    try {
                        br.close();
                    } catch (Throwable localThrowable2) {
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
}
