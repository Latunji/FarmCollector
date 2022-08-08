/*
 * Holds mapping of customer to its registered devices.
 */
package com.interswitch.bifrost.cardservice.model;

import com.interswitch.bifrost.cardservice.exception.CustomException;
import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author Oladele.Olaore
 */
@Entity
@Table(name = "CustomerDevices")
@NamedQueries({
    @NamedQuery(name = "CustomerDevice.findByCustomer", query = "SELECT s FROM CustomerDevice s WHERE s.customer = :customer"),
    @NamedQuery(name = "CustomerDevice.findByDeviceId", query = "SELECT s FROM CustomerDevice s WHERE s.device.uniqueId = :deviceId"),
    @NamedQuery(name = "CustomerDevice.findByDeviceIdAndCustomerId", query = "SELECT s FROM CustomerDevice s WHERE s.device.uniqueId = :deviceId AND s.customer.id = :customerId"),
    @NamedQuery(name = "CustomerDevice.findByDeviceIdAndInstitutionId", query = "SELECT s FROM CustomerDevice s WHERE s.device.uniqueId = :deviceId AND s.customer.institution.code = :insCode"),
    @NamedQuery(name = "CustomerDevice.findByIdAndInstitutionId", query = "SELECT s FROM CustomerDevice s WHERE s.id = :id AND s.customer.institution.code = :insCode"),
    @NamedQuery(name = "CustomerDevice.findAll", query = "SELECT s FROM CustomerDevice s"),
    @NamedQuery(name = "CustomerDevice.removeById", query = "DELETE FROM CustomerDevice s WHERE s.id = :id")
})
public class CustomerDevice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateRegistered;
    @ManyToOne
    private Customer customer;
    @Embedded
    private Device device;
    
    private boolean status = true;

    public boolean getStatus()
    {
        return status;
    }
    public void setStatus(boolean status)
    {
        this.status = status;
    }
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
    
//    public Customer getActiveCustomer() throws CustomException
//    {
//        if(customer == null)
//        {
//            throw new CustomException("Customer not found for device "+device.getUniqueId(),"Customer not found");
//        }
//        else
//        {
//            if(!customer.getProfileStatus().equals(StatusEnums.ACTIVE.name())) 
//            {
//                throw new CustomException("Customer not active for device "+device.getUniqueId(),"Customer profile not active");
//            }
//        }
//        return customer;
//    }

    public Customer getCustomer() {
        
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
