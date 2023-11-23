/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.controller;

//import com.interswitch.bifrost.cardservice.request.GetCardRequest;
import com.interswitch.bifrost.cardservice.request.GenericRequest;
import com.interswitch.bifrost.cardservice.response.CardPanDetailsResponse;
import com.interswitch.bifrost.cardservice.response.GetTokenizationResponse;
import com.interswitch.bifrost.cardservice.response.PtmfbCardsResponse;
import com.interswitch.bifrost.cardservice.response.ViewCardStatusResponse;
import com.interswitch.bifrost.cardservice.service.CardService;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.interswitch.bifrost.cardservice.response.CardsResponse;
import com.interswitch.bifrost.cardservice.vo.ResponseCode;
import com.interswitch.bifrost.commons.annotations.Encrypted;
import com.interswitch.bifrost.commons.annotations.Secured;
import com.interswitch.bifrost.commons.security.vo.AuthenticatedUser;
import com.interswitch.bifrost.commons.security.vo.SessionDetail;
import com.interswitch.bifrost.commons.security.vo.SessionDetailFactory;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author chidiebere.onyeagba
 */
@RestController
@Validated
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
        }
        catch (Exception ex) {
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
    public CompletableFuture<CardsResponse> getCardDetails(@RequestParam("accountNumber") String accountNumber, @RequestParam("custNo") String custNo){

        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s- %s, %s", "CARDS", user.getUserName(), user.getDeviceId(), ""));
        CardsResponse response = new CardsResponse(10);
        try {
            response = cardService.getMyCards(accountNumber, sessionDetail.getDeviceId(), custNo, sessionDetail.getInstitutionCD());
        }
        catch (Exception ex) {
            //log exception if occured
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "GET CARDS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            response.setDescription("ERROR");
        }
        return CompletableFuture.completedFuture(response);
    }


    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("ptmfbGetCards")
    public CompletableFuture<PtmfbCardsResponse> ptmfbGetCardDetails(@RequestParam("accountNumber") String accountNumber, @RequestParam("custNo") String custNo){

        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s- %s, %s", "CARDS", user.getUserName(), user.getDeviceId(), ""));
        PtmfbCardsResponse response = new PtmfbCardsResponse(10);
        try {
            response = cardService.ptmfbGetMyCards(accountNumber, sessionDetail.getDeviceId(), custNo, sessionDetail.getInstitutionCD());
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "GET CARDS EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            response.setDescription("ERROR");
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("providusGetCards")
    public CompletableFuture<CardPanDetailsResponse> providusGetcards(@RequestParam("deviceId") String deviceId, @RequestParam("institutionCD") String institutionCD) {


        CardPanDetailsResponse response = new CardPanDetailsResponse(10);
        LOGGER.log(Level.INFO, String.format("instititionCD: %s - deviceId: %s", institutionCD, deviceId));

        try {
            response = cardService.providusGetCards(deviceId, institutionCD);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("GET Tokenization EXCEPTION - %s, %s", institutionCD, deviceId), ex);
            response.setDescription("ERROR");

        }
        //LOGGER.log(Level.SEVERE, String.format("%s - %s", "GET CARDS_TOKEN FINAL RESPONSE", response.toString() ));

        return CompletableFuture.completedFuture(response);
    }
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("viewCardStatus")
    public CompletableFuture<ViewCardStatusResponse> providusViewcards(@RequestParam("deviceId") String deviceId,
                                                                       @RequestParam("institutionCD") String institutionCD,
                                                                       @RequestParam("cardPan") String cardPan) {

        ViewCardStatusResponse response = new ViewCardStatusResponse();
        LOGGER.log(Level.INFO, String.format("institutionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {
            response = cardService.providusViewCardStatus(cardPan, deviceId, institutionCD);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("VIEW CARD STATUS EXCEPTION - %s, %s", institutionCD, deviceId), ex);
            response.setDescription("ERROR");

        }
        return CompletableFuture.completedFuture(response);
    }
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("providus/hotlistCard")
    public CompletableFuture<GetTokenizationResponse> providusHotlistCard(@RequestParam("deviceId") String deviceId,
                                                                          @RequestParam("institutionCD") String institutionCD,
                                                                          @RequestParam("cardPan") String cardPan,
                                                                          @RequestParam("currency") String currency) {

        GetTokenizationResponse response = new GetTokenizationResponse();
        LOGGER.log(Level.INFO, String.format("institutionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {
            response = cardService.providusHotlistCard(cardPan, currency, deviceId, institutionCD);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("HOTLIST CARD EXCEPTION - %s, %s", institutionCD, deviceId), ex);
            response.setText("ERROR");

        }
        return CompletableFuture.completedFuture(response);
    }
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @GetMapping("providus/dehotlistCard")
    public CompletableFuture<GetTokenizationResponse> providusDeHotlistCard(@RequestParam("deviceId") String deviceId,
                                                                          @RequestParam("institutionCD") String institutionCD,
                                                                          @RequestParam("cardPan") String cardPan,
                                                                          @RequestParam("currency") String currency) {

        GetTokenizationResponse response = new GetTokenizationResponse();
        LOGGER.log(Level.INFO, String.format("institutionCD: %s - deviceId: %s", institutionCD, deviceId));
        try {
            response = cardService.providusDeHotlistCard(cardPan, currency, deviceId, institutionCD);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("DEHOTLIST CARD EXCEPTION - %s, %s", institutionCD, deviceId), ex);
            response.setText("ERROR");

        }
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
            response = cardService.hotlistCards(payload, user.getDeviceId(), sessionDetail.getInstitutionCD());
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "HOTLIST CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            response.setDescription("ERROR");
        }
        return CompletableFuture.completedFuture(response);
    }

    @Secured
    @Async("threadPool")
    @Encrypted(isOptional = true)
    @PostMapping("block")
    public CompletableFuture<ServiceResponse> blockCard(@RequestBody GenericRequest payload) {
        SessionDetail sessionDetail = sessionDetailFactory.getSessionDetail();
        AuthenticatedUser user = (AuthenticatedUser) sessionDetail.getPrincipal();
        LOGGER.info(String.format("%s - %s, %s", "BLOCK CARD", user.getUserName(), user.getDeviceId()));
        ServiceResponse response = new ServiceResponse(10);
        try {
            response = cardService.blockCard(payload, user.getDeviceId(), sessionDetail.getInstitutionCD());
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s - %s", "BLOCK CARD EXCEPTION", user.getUserName(), user.getDeviceId()), ex);
            response.setDescription("ERROR");
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
            response = cardService.requestCard(payload, user.getDeviceId(), sessionDetail.getInstitutionCD(), user); //.hotlistCards(payload.getAccountNumber(), payload.getCardPan(),payload.getCustNo());
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
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
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("%s - %s", new Object[]{"GET CARD TOKEN EXCEPTION", ""}), ex);

            rspJson.put("error", "request could not be completed");
        }
        LOGGER.log(Level.SEVERE, String.format("%s - %s \n Key: %s", new Object[]{"GET TOKEN FINAL RESPONSE :", rspJson.toString(), this.tokenKey}));
        return CompletableFuture.completedFuture(rspJson.toString());
    }
}
