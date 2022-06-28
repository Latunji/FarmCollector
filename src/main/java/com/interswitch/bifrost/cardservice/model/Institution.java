/*
 * 
 */
package com.interswitch.bifrost.cardservice.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oladele.Olaore
 */
@Entity
@Table(name = "Institution")
@NamedQueries({
    @NamedQuery(name = "Institution.findAll", query = "SELECT e FROM Institution e"),
    @NamedQuery(name = "Institution.findByCode", query = "SELECT e FROM Institution e WHERE e.code = :code"),
})
public class Institution implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;
    @Column(name = "BANK_CODE")
    private String bankCode;
    
    @Column(name = "APP_VERSION")
    private String currentAppVersion;
    @Column(name = "FORCE_UPDATE")
    boolean forceUpdate;// = false ;

    public String getCurrentAppVersion() {
        return currentAppVersion;
    }

    public void setCurrentAppVersion(String currentAppVersion) {
        this.currentAppVersion = currentAppVersion;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
