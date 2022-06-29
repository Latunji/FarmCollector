/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.controller;

//import com.interswitch.bifrost.cardservice.request.GetCardRequest;
import com.interswitch.bifrost.cardservice.request.GenericRequest;
import com.interswitch.bifrost.cardservice.response.CardPanDetailsResponse;
import com.interswitch.bifrost.cardservice.service.CardService;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.interswitch.bifrost.cardservice.response.CardsResponse;
import com.interswitch.bifrost.cardservice.vo.ResponseCode;
import com.interswitch.bifrost.commons.annotations.Encrypted;
import com.interswitch.bifrost.commons.annotations.Secured;
import com.interswitch.bifrost.commons.security.vo.AuthenticatedUser;
import com.interswitch.bifrost.commons.security.vo.SessionDetail;
import com.interswitch.bifrost.commons.security.vo.SessionDetailFactory;
import java.security.PrivateKey;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author chidiebere.onyeagba
 */
@RestController
@Validated
@RequestMapping("card")
public class CardOperation {

    private static final Logger LOGGER = Logger.getLogger(CardOperation.class.getName());

    @Autowired
    CardService cardService;

    @Autowired
    SessionDetailFactory sessionDetailFactory;

    @Value("${isw.token.key}")
    String tokenKey;

    //@Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("validateCard")
    public CompletableFuture<ServiceResponse> validateCard(@ModelAttribute GenericRequest payload) {
        //SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        LOGGER.info(String.format(" %s- %s, %s", "VALIDATE CARDS", payload.getDeviceId(), ""));
        ServiceResponse response = new CardsResponse(10);
        try {
            response = cardService.activateAccountWithCard(payload.getAccountNumber(), payload.getDeviceId(), payload.getMissingDigit(), payload.getCustNo(), payload.getInstitutionCD());//cardService.getMyCards(payload.getAccountNumber(),sessionDetail.getDeviceId(),payload.getCustNo());
        } catch (Exception ex) {
            //log exception if occured
            LOGGER.log(Level.SEVERE, String.format("%s - %s", "VALIDATE CARDS EXCEPTION", payload.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS EXCEPTION", ""), ex);
            response.setDescription("ERROR");
            //return response;
        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("getCards")
    public CompletableFuture<CardsResponse> getCardDetails(@RequestParam("accountNumber") String accountNumber, @RequestParam("custNo") String custNo) {

        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s- %s, %s", "CARDS", user.getUserName(), user.getDeviceId(), ""));
        //LOGGER.info(String.format("%s - %s, %s", "CARDS", "", ""));
        CardsResponse response = new CardsResponse(10);
        try {
            response = cardService.getMyCards(accountNumber, sessionDetail.getDeviceId(), custNo, sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            //log exception if occured
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "GET CARDS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS EXCEPTION", ""), ex);
            response.setDescription("ERROR");
            //return response;
        }
        // LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS FINAL RESPONSE", response.toString() ));
        return CompletableFuture.completedFuture(response);
    }
    
    

    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("providusGetCards")
    public CompletableFuture<CardPanDetailsResponse> providusGetcards(@RequestParam("deviceId") String deviceId, @RequestParam("institutionCD") String institutionCD) {

      CardPanDetailsResponse response = new CardPanDetailsResponse(10);
        try {
            response = cardService.providusGetCards(deviceId, institutionCD);
        } catch (Exception ex) {
           
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "GET Tokenization EXCEPTION", institutionCD, deviceId), ex);
            
            response.setDescription("ERROR");
        
        }
        LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS_TOKEN FINAL RESPONSE", response.toString() ));
        return CompletableFuture.completedFuture(response);
    }
    
    
    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("getMaskedCards")
    public CompletableFuture<CardsResponse> getMaskedCardDetails(@ModelAttribute GenericRequest payload) {

        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s- %s, %s", "GET MASKED CARDS", user.getUserName(), user.getDeviceId(), ""));
        //LOGGER.info(String.format("%s - %s, %s", "CARDS", "", ""));
        CardsResponse response = new CardsResponse(10);
        try {
            //response =  cardService.getMaskedCards(payload.getAccountNumber(),sessionDetail.getDeviceId(),payload.getCustNo(),sessionDetail.getInstitutionCD());
            response = cardService.getMyCards(payload.getAccountNumber(), sessionDetail.getDeviceId(), payload.getCustNo(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            //log exception if occured
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "GET MASKED CARDS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS EXCEPTION", ""), ex);
            response.setDescription("ERROR");
            //return response;
        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("activateCustomer")
    public CompletableFuture<ServiceResponse> activateCustomerTransaction(@RequestBody GenericRequest payload) {

        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s- %s, %s", "ACTIVATE CUSTOMER WITH CARDS", user.getUserName(), user.getDeviceId(), ""));
        //LOGGER.info(String.format("%s - %s, %s", "CARDS", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.activateAccountWithCard(payload.getAccountNumber(), sessionDetail.getDeviceId(), payload.getMissingDigit(), payload.getCustNo(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            //log exception if occured
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "ACTIVATE CUSTOMER WITH CARDS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS EXCEPTION", ""), ex);
            response.setDescription("ERROR");
            //return response;
        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("hotlist")
    public CompletableFuture<ServiceResponse> hotlistCard(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "HOTLIST CARDS", user.getUserName(), user.getDeviceId()));
        //LOGGER.info(String.format("%s - %s, %s", "HOTLIST CARDS", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.hotlistCards(payload.getAccountNumber(), payload.getCardPan(), user.getDeviceId(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "HOTLIST CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "HOTLIST CARD EXCEPTION", ""), ex);
            response.setDescription("ERROR");
            //return response;
        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("request")
    public CompletableFuture<ServiceResponse> requestCard(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "REQUEST CARDS", user.getUserName(), user.getDeviceId()));
        //LOGGER.info(String.format("%s - %s, %s", "REQUEST CARDS", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.requestCard(payload.getAccountNumber(), user.getDeviceId(),
                    payload.getCardType(), payload.getNameOnCard(), sessionDetail.getInstitutionCD(), payload.getBranchCode()); //.hotlistCards(payload.getAccountNumber(), payload.getCardPan(),payload.getCustNo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "REQUEST CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "REQUEST CARD EXCEPTION", ""), ex);
            response.setDescription("ERROR");

        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("replace")
    public CompletableFuture<ServiceResponse> replaceCard(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "REPLACE CARD", user.getUserName(), user.getDeviceId()));
        //LOGGER.info(String.format("%s - %s, %s", "REPLACE CARD", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.replacCard(payload.getAccountNumber(), user.getDeviceId(),
                    payload.getCardPan(), payload.getDeliveryType(),
                    payload.getDeliveryAddress(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s -%s", "REPLACE CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "REPLACE CARD EXCEPTION", ""), ex);
            response.setDescription("ERROR");

        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("accountDetails")
    public CompletableFuture<ServiceResponse> getAccountDetails(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "REPLACE CARD", user.getUserName(), user.getDeviceId()));
        //LOGGER.info(String.format("%s - %s, %s", "REPLACE CARD", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.getCustomerDetails(payload.getCardPan(), payload.getCardPin(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s -%s", "GET CUSTOMER DETAILS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "REPLACE CARD EXCEPTION", ""), ex);
            response.setDescription("ERROR");

        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("unblock")
    public CompletableFuture<ServiceResponse> unblockCard(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "UNBLOCK CARDS", user.getUserName(), user.getDeviceId()));
        //LOGGER.info(String.format("%s - %s, %s", "HOTLIST CARDS", "", ""));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.unblockCard(payload.getAccountNumber(), sessionDetail.getDeviceId(), payload.getCardPan(), sessionDetail.getInstitutionCD());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "UNBLOCK CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            //LOGGER.log(Level.SEVERE, String.format("%s - %s", "HOTLIST CARD EXCEPTION", ""), ex);
            response.setDescription(ResponseCode.GENERAL_ERROR_MESSAGE);
            response.setCode(ResponseCode.ERROR);
            //return response;
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async("threadPool")
    @PostMapping({"getCardToken"})
    public CompletableFuture<String> getCardToken(@RequestBody String payload) {

        String panLastFour = new JSONObject(payload).getString("panLastFour");
        LOGGER.info(String.format("Input: Pan last four digit %s ", new Object[]{panLastFour}));
        JSONObject rspJson = new JSONObject();
        try {
            String response = cardService.calculateJwtToken(panLastFour);
            rspJson.put("token", response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", new Object[]{"GET CARD TOKEN EXCEPTION", ""}), ex);

            rspJson.put("error", "request could not be completed");
        }
        LOGGER.log(Level.SEVERE, String.format("%s - %s \n Key: %s", new Object[]{"GET TOKEN FINAL RESPONSE :", rspJson.toString(), this.tokenKey}));
        return CompletableFuture.completedFuture(rspJson.toString());
    }
    


}
