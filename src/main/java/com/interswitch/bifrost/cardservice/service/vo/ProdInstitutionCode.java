package com.interswitch.bifrost.cardservice.service.vo;

import lombok.Getter;

@Getter
public enum ProdInstitutionCode {

    PROVIDUS("059"),
    PTMFB("061"),
    AGMORTGAGE("060"),
    FINCA("062"),
    HASAL("056"),
    NIRSAL("057"),
    AMJUU("058"),
    ISW("ISW");

    private String institutionCD;

    ProdInstitutionCode(String institutionCD) {
        this.institutionCD = institutionCD;
    }
}
