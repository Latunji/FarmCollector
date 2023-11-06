package com.interswitch.bifrost.cardservice.response;

import com.interswitch.bifrost.cardservice.service.vo.PtmfbCard;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import com.interswitch.bifrost.commons.vo.ServiceResponse;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class PtmfbCardsResponse extends ServiceResponse {

    private List<PtmfbCard> cards;

    public List<PtmfbCard> getCards() {
        return cards;
    }

    public void setCards(List<PtmfbCard> cards) {
        this.cards = cards;
    }

    public PtmfbCardsResponse(int code) {
        super(code);
    }

    public PtmfbCardsResponse(int code, String description) {
        super(code, description);
    }

}
