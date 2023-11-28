package com.interswitch.bifrost.cardservice.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author chidiebere.onyeagba
 */
@Getter
@Component
@RefreshScope
@RequiredArgsConstructor
public class ConfigProperties {

    private static final Logger LOGGER = Logger.getLogger(ConfigProperties.class.getName());

    private final Environment environment;

    public String getInterserviceHeaderAuth() {
        String key = "interservice.header.auth";
        return environment.getProperty(key, "BIFROST_INT3RS3RVIC3_H3AD3R_AUTH_$_1");
    }

    public String getCustomerUrl() {
        String key = "customerServiceUrl";
        String value = environment.getProperty(key);
        return key;
    }

    //to be changed to  getBankGatewayUrl
    public String getBankGatewayUrl2(String institutionCode) {
        String key = institutionCode + ".bank.api-gateway.base-url";

        String value = environment.getProperty(key);
        LOGGER.log(Level.INFO, String.format("gateway key %s - value %s\n", key, value));
        return value;
    }

    //to be changed to  getBankGatewayUrlObsolete
    public String getBankGatewayUrl(String institutionCode) {
        String key = "bank.api-gateway.base-url";//institutionCode + ".gatewayUrl";

        String value = environment.getProperty(key);
        return value;
    }

    public String getBankBaseUrl(String institutionCode) {
        String key = institutionCode + ".bank.base-url";
        String value = environment.getProperty(key);
        LOGGER.log(Level.INFO, String.format("key %s - value %s\n", key, value));
        return value;
    }

    public String getBankCardBaseUrl(String institutionCode) {
        String key = institutionCode + ".bank.card.base-url";
        String value = environment.getProperty(key);
        LOGGER.log(Level.INFO, String.format("key %s - value %s\n", key, value));
        return value;
    }

    public String getVersionedUrl(String institutionCode) {
        String key = institutionCode + ".bank-versioned-gatewayUrl";
        String value = environment.getProperty(key);
        return value;
    }

    public String getAPIKey(String institutionCode) {
        String key = institutionCode + ".APIKey";
        LOGGER.info(String.format("%s - %s ", "GET API KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET API KEY VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "a61ad9a8-3ffe-40d0-9485-bce472379b7e" : value;
    }

    public String getAuthID(String institutionCode) {
        String key = institutionCode + ".authenticationID";
        LOGGER.info(String.format("%s - %s ", "GET authentication ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET authentication ID VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "81e4f76a-2dda-4398-b922-b343f84a0651" : value;
    }

    public String getAppID(String institutionCode) {
        String key = institutionCode + ".applicationID";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "InterSwitch" : value;
    }

    public String getToken(String institutionCode) {
        String key = institutionCode + ".cardApiToken";
        LOGGER.info(String.format("%s - %s ", "GET Card API TOKEN KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET Card API TOKEN VALUE", value));

        return value;
    }

    public String getNameProvidus(String institutionCode) {
        String key = institutionCode + ".nameProvidus";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);

//        return value==null?"InterSwitch":value;
        return value == null ? "" : value;
    }

    public String getCardBin(String institutionCode){
        String key = institutionCode + ".cardBin";
        LOGGER.info(String.format("%s - %s ", "GET Card Bin KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET Card Bin VALUR", value));
        return value == null ? "" : value;
    }


    public String getClientIDProvidus(String institutionCode) {
        String key = institutionCode + ".clientIDProvidus";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "" : value;
    }

    public String getSecretKeyProvidus(String institutionCode) {
        String key = institutionCode + ".secretKeyProvidus";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "" : value;
    }


    public String getIvProvidus(String institutionCode) {
        String key = institutionCode + ".ivProvidus";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);

        return value == null ? "" : value;
    }

}
