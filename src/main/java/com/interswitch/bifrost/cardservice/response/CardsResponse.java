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
    private List<PtmfbCard> cards;

    public List<PtmfbCard> getCards() {
        return cards;
    }

    public void setCards(List<PtmfbCard> cards) {
        this.cards = cards;
    }

    public CardsResponse(int code) {
        super(code);
    }
    
    public CardsResponse(int code, String description) {
        super(code, description);
    }
     
//     public void setMaskedCards(String  key,List<Cards> Allcards) throws Exception {
//         //cards = new ArrayList<Cards>();
//         for(Cards card : Allcards)
//         {
//             //.Cards NewCard = new Cards();
////
//             //card.setUnmaskedCardPan(cipher.encrypt(key,card.getCardPan()));
//             card.setUnmaskedCardPan(key,card.getCardPan());
//             card.setUnmaskedCardPan(card.getUnmaskedCardPan());
//             //Added this to mask card pan
//             card.setCardPan(maskNumber(card.getCardPan()));
//
//         }
//         this.cards =  Allcards;
//         //this.cards = cards;
//     }


     
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

}

