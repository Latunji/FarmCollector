package com.interswitch.bifrost.cardservice.model.repo.impl;

import com.interswitch.bifrost.cardservice.exception.CustomException;
import com.interswitch.bifrost.cardservice.exception.DeactivatedDeviceException;

//import com.interswitch.bifrost.customer.model.BillsPaymentBeneficiary;
import com.interswitch.bifrost.cardservice.model.Customer;
import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import com.interswitch.bifrost.cardservice.model.repo.CustomerRepository;
import com.interswitch.bifrost.cardservice.service.CardService;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EntityManager manager;

   private static final Logger LOGGER = Logger.getLogger(CardService.class.getName());
   
    @Override
    public CustomerDevice findCustomerDeviceAndInstitution(String deviceId, String institutionCD) {
        LOGGER.log(Level.INFO, String.format("ATREPOinstititionCD: %s - ATREPOdeviceId: %s", institutionCD, deviceId));
          List<CustomerDevice> list = manager.createNamedQuery("CustomerDevice.findAll", CustomerDevice.class).
             getResultList();
//        List<CustomerDevice> list = manager.createNamedQuery("CustomerDevice.findByDeviceIdAndInstitutionId", CustomerDevice.class).
//                setParameter("deviceId", deviceId).
//                setParameter("insCode", institutionCD).
//                getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public CustomerDevice findByCustomerDeviceAndInstitution(String deviceId, String institutionCD) {
        LOGGER.log(Level.INFO, String.format("ATREPOinstititionCD: %s - ATREPOdeviceId: %s", institutionCD, deviceId));
        List<CustomerDevice> list = manager.createNamedQuery("CustomerDevice.findByDeviceIdAndInstitutionId", CustomerDevice.class).
                setParameter("deviceId", deviceId).
                setParameter("insCode", institutionCD).
                getResultList();
        LOGGER.log(Level.INFO, String.format("ReturnedList: %s", list));
        return list.isEmpty() ? null : list.get(0);
    }

}
