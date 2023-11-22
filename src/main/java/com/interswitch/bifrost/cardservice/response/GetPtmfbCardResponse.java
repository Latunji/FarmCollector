package com.interswitch.bifrost.cardservice.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.interswitch.bifrost.cardservice.service.vo.PtmfbCard;
import com.interswitch.bifrost.cardservice.util.SecurityCipher;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class GetPtmfbCardResponse {

    private boolean isSuccessful;
    private String responseDescription;
    private List<PtmfbCard> cards;

}
