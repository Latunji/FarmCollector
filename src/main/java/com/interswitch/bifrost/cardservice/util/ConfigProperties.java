/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author chidiebere.onyeagba
 */
@Component
@RefreshScope
@Getter
public class ConfigProperties {
    

    private static final Logger LOGGER = Logger.getLogger(ConfigProperties.class.getName());
  
   @Autowired
   private Environment environment;      
    
   //@Value("${gatewayUrl:https://localhost:8080/}")
  //@Value("${bank.api-gateway.base-url:http://localhost:7080/}")
    //private String bankGatewayBaseUrl;
   
   public String getCustomerUrl()
   {
       String key = "customerServiceUrl";
       String value = environment.getProperty(key);
       return key;
   }
   //to be changed to  getBankGatewayUrl
   public String getBankGatewayUrl2(String institutionCode)
    {
        String key = institutionCode + ".bank.api-gateway.base-url";
        
        String value = environment.getProperty(key);
        LOGGER.log(Level.INFO, String.format("gateway key %s - value %s\n",key,value));
        return value;
    }
   
    //to be changed to  getBankGatewayUrlObsolete
   public String getBankGatewayUrl(String institutionCode)
    {
        String key = "bank.api-gateway.base-url";//institutionCode + ".gatewayUrl";
        
        String value = environment.getProperty(key);
        return value;
    }
    
    public String getBankBaseUrl(String institutionCode)
    {
        String key = institutionCode + ".bank.base-url";
        String value = environment.getProperty(key);
        LOGGER.log(Level.INFO, String.format("key %s - value %s\n",key,value));
        return value;
    }
    
    public String getVersionedUrl(String institutionCode)  
    {
        String key = institutionCode + ".bank-versioned-gatewayUrl";
        String value = environment.getProperty(key);
        return value;  
    }
    public String getAPIKey(String institutionCode)
    {
        String key = institutionCode+".APIKey";
        LOGGER.info(String.format("%s - %s ", "GET API KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET API KEY VALUE", value));
        //int intValue = Integer.parseInt(value);
        
        return value==null?"a61ad9a8-3ffe-40d0-9485-bce472379b7e":value;
    }
    
    public String getAuthID(String institutionCode)
    {
        String key = institutionCode+".authenticationID";
        LOGGER.info(String.format("%s - %s ", "GET authentication ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET authentication ID VALUE", value));
        //int intValue = Integer.parseInt(value);
        
        return value==null?"81e4f76a-2dda-4398-b922-b343f84a0651":value;
    }
    
    public String getAppID(String institutionCode)
    {
        String key = institutionCode+".applicationID";
        LOGGER.info(String.format("%s - %s ", "GET application ID KEY", key));
        String value = environment.getProperty(key);
        LOGGER.info(String.format("%s - %s ", "GET application ID VALUE", value));
        //int intValue = Integer.parseInt(value);
        
        return value==null?"InterSwitch":value;
    }
            
}
