package com.interswitch.bifrost.cardservice.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "Sessions")
@NamedQueries({
    @NamedQuery(name = "Session.findByCustomer", query = "SELECT s FROM Session s WHERE s.customer = :customer"),
    @NamedQuery(name = "Session.findByCustomerAndDateRange", query = "SELECT s FROM Session s WHERE s.customer = :customer AND (s.startDate BETWEEN :startDt and :endDt)")
})
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerID")
    private Customer customer;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate, endDate;
    @Embedded
    private Device originatingDevice;
    @Embedded
    private Location location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Device getOriginatingDevice() {
        return originatingDevice;
    }

    public void setOriginatingDevice(Device originatingDevice) {
        this.originatingDevice = originatingDevice;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
