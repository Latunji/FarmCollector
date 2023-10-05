package com.interswitch.bifrost.cardservice.service.vo;

import lombok.Getter;

@Getter
public enum TestInstitutionCode {

        PROVIDUS("023"),
        PTMFB("025"),
        AGMORTGAGE("027"),
        FINCA("028"),
        HASAL("056"),
        NIRSAL("057"),
        AMJUU("055"),
        ISW("ISW");
        private String paramName;

        TestInstitutionCode(String paramName) {
            this.paramName = paramName;
        }


}
