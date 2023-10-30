package com.interswitch.bifrost.cardservice.response;

import com.interswitch.bifrost.commons.vo.ServiceResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewCardStatusResponse extends ServiceResponse {
    public ViewCardStatusResponse() {
    }

    public ViewCardStatusResponse(int code, String description) {
        super(code, description);
    }

    private String pan;
    private String cardStatus;
    private String holdResponseCode;
    private String sequenceNumber;
    private String expiryDate;
}
