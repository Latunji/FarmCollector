/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.bifrost.cardservice.service.vo;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author chidiebere.onyeagba
 */
@Getter
@Setter
public class Card {
    //@JsonProperty("ProdID")
    private int prodID;
    //@JsonProperty("CardNo")
    private String cardNo;
    //@JsonProperty("AccountNumber")
    private String accountNumber;
    //@JsonProperty("AccountName")
    private String accountName;
    //@JsonProperty("ID")
    private long id;
    //@JsonProperty("Balance")
    private BigDecimal balance;
            //double balance;
    //@JsonProperty("ClearCardNumber")
    private String clearCardNumber;
    //@JsonProperty("CanTransfer")
    private boolean canTransfer;
    //@JsonProperty("Currency")
    private String currency;
    //@JsonProperty("CardType")
    private String cardType;
    //@JsonProperty("IsActive")
    private boolean isActive;
}
