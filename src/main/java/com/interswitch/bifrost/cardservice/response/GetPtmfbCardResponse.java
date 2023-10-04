package com.interswitch.bifrost.cardservice.response;

import com.interswitch.bifrost.cardservice.service.vo.PtmfbCard;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetPtmfbCardResponse {

    private boolean successful;
    private String responseDescription;
    private List<PtmfbCard> cards;

}
