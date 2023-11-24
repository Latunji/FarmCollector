package com.interswitch.bifrost.cardservice.model.repo;

import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import org.springframework.stereotype.Repository;

/**
 *
 * @author oladele
 */

@Repository
public interface CustomerRepository {
    

    CustomerDevice findCustomerDeviceAndInstitution(String deviceId, String institutionCD);
    CustomerDevice findByCustomerDeviceAndInstitution(String deviceId, String institutionCD);
   
}
