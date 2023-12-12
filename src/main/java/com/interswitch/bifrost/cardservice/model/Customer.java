package com.interswitch.bifrost.cardservice.model;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author oladele
 */
@Entity
@Table(name = "Customers")
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@NamedQueries({
    @NamedQuery(name = "Customer.findByBankID", query = "SELECT c FROM Customer c WHERE c.bankID = :bankID AND c.institution.code = :insCode"),
    @NamedQuery(name = "Customer.findByInstitutionID", query = "SELECT c FROM Customer c WHERE c.institution.code = :insCode"),
    @NamedQuery(name = "Customer.findByPrimaryAccountNoOnly", query = "SELECT c FROM Customer c WHERE c.primaryAccountNumber = :primaryAccountNumber"),
    @NamedQuery(name = "Customer.findByPrimaryAccountNo", query = "SELECT c FROM Customer c WHERE c.primaryAccountNumber = :primaryAccountNumber AND c.institution.code = :insCode"),
    @NamedQuery(name = "Customer.findByUserName", query = "SELECT c FROM Customer c WHERE  c.institution.code = :insCode AND c.userName = :userName"),
    @NamedQuery(name = "Customer.findByUserNamefindByUserNameOnly", query = "SELECT c FROM Customer c WHERE c.userName = :userName"),
    @NamedQuery(name = "Customer.findByUserNameAndID", query = "SELECT c FROM Customer c WHERE  c.institution.code = :insCode AND c.id <> :id AND c.userName = :userName"),
    @NamedQuery(name = "Customer.findByCustomerNo", query = "SELECT c FROM Customer c WHERE c.custNo = :custNo AND c.institution.code = :insCode"),
    @NamedQuery(name = "Customer.findBySessionKey", query = "SELECT c FROM Customer c WHERE c.sessionData.sessionId = :sessionKey"),
    @NamedQuery(name = "Customer.findByRegDeviceId", query = "SELECT c FROM Customer c WHERE c.regDevice.uniqueId = :deviceId"),
    @NamedQuery(name = "Customer.findByDeviceIdAndInsCode", query = "SELECT c FROM Customer c WHERE c.regDevice.uniqueId = :deviceId AND c.institution.code = :insCode"),
    @NamedQuery(name = "Customer.findByLatestDeviceIdAndInsCode", query = "SELECT c FROM Customer c WHERE c.regDevice.uniqueId = :deviceId AND c.institution.code = :insCode order by c.dateRegistered desc"),
})
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered, dateCreated;
    private String fullname;
    @Column(unique=true)
    private String primaryAccountNumber;
    private String phoneNumber;
    private String email;
    private String bvn;
    private String onboardMode; // Identifies the onboard type this user used to register
    @Column(unique=true)
    private String bankID; // ID that identifies the customer at the bank
    //@Column(unique=true)
    private String userName;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Embedded
    private Device regDevice; // uuid of the device the customer's registered with
    @Embedded
    private Location regInitiateLocation;
    private String profileStatus; // status of the customer's mobile diamond yello profile, if not active then user can not login
    @Embedded
    private SessionData sessionData;
    
    private String pinSalt;
    private String pin;
    private boolean pinChangeRequired;
    private String transactionPinSalt;
    private String transactionPin;
    private boolean transactionPinChangeRequired;
    
    private int counter;
    
    private String imageType;

    private String imageString ;
    
    private String custNo;
    
    private boolean canTransact;
    
    private int kycLevel;
    
    private String profileJson;
    private String passwords;

    public int getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(int kycLevel) {
        this.kycLevel = kycLevel;
    }
    
    @ManyToOne
    private Institution institution;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    public void setPrimaryAccountNumber(String primaryAccountNumber) {
        this.primaryAccountNumber = primaryAccountNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getOnboardMode() {
        return onboardMode;
    }

    public void setOnboardMode(String onboardMode) {
        this.onboardMode = onboardMode;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Device getRegDevice() {
        return regDevice;
    }

    public void setRegDevice(Device regDevice) {
        this.regDevice = regDevice;
    }

    public Location getRegInitiateLocation() {
        return regInitiateLocation;
    }

    public void setRegInitiateLocation(Location regInitiateLocation) {
        this.regInitiateLocation = regInitiateLocation;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String getPinSalt() {
        return pinSalt;
    }

    public void setPinSalt(String pinSalt) {
        this.pinSalt = pinSalt;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTransactionPinSalt() {
        return transactionPinSalt;
    }

    public void setTransactionPinSalt(String transactionPinSalt) {
        this.transactionPinSalt = transactionPinSalt;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    public boolean isPinChangeRequired() {
        return pinChangeRequired;
    }

    public void setPinChangeRequired(boolean pinChangeRequired) {
        this.pinChangeRequired = pinChangeRequired;
    }

    public boolean isTransactionPinChangeRequired() {
        return transactionPinChangeRequired;
    }

    public void setTransactionPinChangeRequired(boolean transactionPinChangeRequired) {
        this.transactionPinChangeRequired = transactionPinChangeRequired;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    public String getImageString()
    {
        return this.imageString;
    }
    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }
    
    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String imgType) {
        this.imageType = imgType;
    }
    
    public String getCustNo() {
        return this.custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }
    
    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    public boolean getCanTransact()
    {
        return this.canTransact;
    }
    public void setCanTransact(boolean canTransact)
    {
        this.canTransact = canTransact;
    }

    public String getProfileJson() {
        return profileJson;
    }

    public void setProfileJson(String profileJson) {
        this.profileJson = profileJson;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    
    
    
    
    
    
    
    
    

}
