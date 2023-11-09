package com.interswitch.bifrost.cardservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CardAudit")
public class CardAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private String accountNumber;
    private String reason;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
}