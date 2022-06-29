/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.response;

import java.util.ArrayList;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import java.util.List;
/**
 *
 * @author Benjamin.Abegunde
 */
public class CardPanDetailsResponse extends ServiceResponse{

    public CardPanDetailsResponse(int code) {
        super(code);
    }

    public CardPanDetailsResponse(int code, String description) {
        super(code, description);
    }

    public CardPanDetailsResponse() {
    }

    public ArrayList<Pans> pans;

    public ArrayList<Pans> getPans() {
        return pans;
    }

    public void setPans(ArrayList<Pans> pans) {
        this.pans = pans;
    }
    
//     public void setMaskedCards(String  key,List<Pans> AllPans) throws Exception {
//    
//         for(Pans card : AllPans)
//         {
//             //.Cards NewCard = new Cards();
////           
//             //card.setUnmaskedCardPan(cipher.encrypt(key,card.getCardPan()));
//             card.setExpiry(card.getExpiry());
//             card.setUnmaskedCardPan(card.getUnmaskedCardPan());
//             //Added this to mask card pan
//             card.setCardPan(maskNumber(card.getCardPan()));
//             
//         }
//         this.cards =  Allcards;
//         
//     }
    public class Pans{
    public String expiry;
    public String pan;

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }
    
}
    
}
