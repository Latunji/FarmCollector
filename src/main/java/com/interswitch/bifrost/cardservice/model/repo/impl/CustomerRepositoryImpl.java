package com.interswitch.bifrost.cardservice.model.repo.impl;

import com.interswitch.bifrost.cardservice.exception.CustomException;
import com.interswitch.bifrost.cardservice.exception.DeactivatedDeviceException;

//import com.interswitch.bifrost.customer.model.BillsPaymentBeneficiary;
import com.interswitch.bifrost.cardservice.model.Customer;
import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import com.interswitch.bifrost.cardservice.model.repo.CustomerRepository;

import java.util.Date;
import java.util.List;
//import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oladele
 */
//@Dependent
@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    @PersistenceContext
    //(unitName = "bifrostPU")
    private EntityManager manager;

   
   
    @Override
    @Transactional
    public CustomerDevice findCustomerDeviceAndInstitution(String deviceId, String institutionCD) {
        List<CustomerDevice> list = manager.createNamedQuery("CustomerDevice.findByDeviceIdAndInstitutionId", CustomerDevice.class).
                setParameter("deviceId", deviceId).
                setParameter("insCode", institutionCD).
                getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

 

}
