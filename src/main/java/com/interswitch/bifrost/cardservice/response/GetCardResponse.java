/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.response;

import com.google.gson.annotations.Expose;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 *
 * @author chidiebere.onyeagba
 */
public class GetCardResponse  {
    private List<Cards> cards;
    private String responseCode;
    private String responseMessage;
    
    public static class Cards
     {
        //@Expose (serialize = false, deserialize = false) 
         private String cardPan;//": "5061270138300459839",
         private String expiryDate;//": "2019-04-29 12:30:01.0",
         private String cardType;//": "20",
         private String nameOnCard;//": "ADEBIMPE ADEDAPO ABIODUN"
         private String unmaskedCardPan;
         
         //@Expose (serialize = false, deserialize = false) 
         transient SecurityCipher  cipher = new SecurityCipher();

        public String getUnmaskedCardPan() {
            return unmaskedCardPan;
        }

        public  void setUnmaskedCardPan(String key, String unmaskedCardPan) throws Exception {
 
            this.unmaskedCardPan = cipher.encrypt(key, unmaskedCardPan);
        }
        public  void setUnmaskedCardPan(String unmaskedCardPan) throws Exception {
 
            this.unmaskedCardPan = unmaskedCardPan;
        }

         public String getCardPan()
         {
             return this.cardPan;
         }
         public void setCardPan(String cardPan)
         {
             this.cardPan = cardPan;
         }

         public String getExpiryDate()
         {
             return this.expiryDate;
         }
         public void setExpiryDate(String expiryDate)
         {
             this.expiryDate = expiryDate;

         }

         public String getCardType()
         {
             return this.cardType;
         }
         public void setCardType(String cardType)
         {
             this.cardType = cardType;
         }

         public String getNameOnCard()
         {
             return this.nameOnCard;
         }
         public void setNameOnCard(String nameOnCard)
         {
             this.nameOnCard = nameOnCard;
         }

     }

    public List<Cards> getCards() {
         return this.cards;
     }

     public void setCards(List<Cards> cards) {
         this.cards = cards;
     }

     public String getResponseCode() {
         return responseCode;
     }

     public void setResponseCode(String responseCode) {
         this.responseCode = responseCode;
     }

     public String getResponseMessage() {
         return responseMessage;
     }

     public void setResponseMessage(String responseMessage) {
         this.responseMessage = responseMessage;
     }
}
