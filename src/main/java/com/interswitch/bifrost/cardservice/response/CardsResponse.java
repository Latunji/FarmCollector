/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.response;

import com.interswitch.bifrost.cardservice.service.vo.PtmfbCard;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
//import com.interswitch.bifrost.cardservice.service.vo.Card;
import com.interswitch.bifrost.cardservice.response.GetCardResponse.Cards;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;

/**
 *
 * @author chidiebere.onyeagba
 */
public class CardsResponse extends ServiceResponse {
    //private Card[] cards;
    //@Autowired
    //SecurityCipher cipher; //= 
            
    transient SecurityCipher  cipher = new SecurityCipher();        
    private List<Cards> cards;
    private List<PtmfbCard> card;
    public CardsResponse(int code) {
        super(code);
    }
    
    public CardsResponse(int code, String description) {
        super(code, description);
    }
    
    public List<Cards> getCards() {
         return cards;
     }

     public void setCards(List<Cards> cards) {
         this.cards = cards;
     }
     
     public void setMaskedCards(String  key,List<Cards> Allcards) throws Exception {
         //cards = new ArrayList<Cards>();
         for(Cards card : Allcards)
         {
             //.Cards NewCard = new Cards();
//           
             //card.setUnmaskedCardPan(cipher.encrypt(key,card.getCardPan()));
             card.setUnmaskedCardPan(key,card.getCardPan());
             card.setUnmaskedCardPan(card.getUnmaskedCardPan());
             //Added this to mask card pan
             card.setCardPan(maskNumber(card.getCardPan()));
             
         }
         this.cards =  Allcards;
         //this.cards = cards;
     }

    public void setPtmfbMaskedCards(String  key,List<PtmfbCard> Allcards) throws Exception {
        //cards = new ArrayList<Cards>();
        for(PtmfbCard card : Allcards)
        {
//            card.setUnmaskedCardPan(key,card.getCardPAN());
//            card.setUnmaskedCardPan(card.getUnmaskedCardPan());
            //Added this to mask card pan
            card.setCardPAN(maskNumber(card.getCardPAN()));

        }
        this.card =  Allcards;
        //this.cards = cards;
    }
     
     private String maskNumber(String number)
     {
         int offset = 4;
         int stringLength = number.length();
         int maskLength = number.length() - 8;
         String mask = StringUtils.repeat("*",offset);//, ERROR)
         StringBuilder maskedNumber = new StringBuilder(number.substring(0,maskLength));// number.substring(0,4) + mask
         maskedNumber.append(mask).append(number.substring(maskLength+4,stringLength));
         
         return maskedNumber.toString();
         
     }
     
     static public void main(String[] args) throws Exception
     {
         List<Cards> allCards = new ArrayList<Cards>();
         
         Cards card1 = new Cards();
         card1.setCardPan("1234567890296459");
        Cards card2 = new Cards();
         card2.setCardPan("0987654321098765");
         
         allCards.add(card1);
         allCards.add(card2);
         
         CardsResponse response = new CardsResponse(0);
         response.setMaskedCards("ben", allCards);
         
         response.printCardPan();
     }
     
     private void printCardPan()
     {
         for( Cards card: this.getCards())
         {
             System.out.printf("\n %s  %s",card.getCardPan(),card.getUnmaskedCardPan());
         }
     }

}

