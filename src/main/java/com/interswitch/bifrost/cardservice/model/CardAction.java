package com.interswitch.bifrost.cardservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CardAction {
    REQUEST("Request Card"),
    BLOCK("Block Card"),
    UNBLOCK("Unblock Card");

    private String action;

    CardAction(String action) {
        this.action = action;
    }


    }
