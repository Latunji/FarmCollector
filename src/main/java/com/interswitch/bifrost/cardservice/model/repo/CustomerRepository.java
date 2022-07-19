package com.interswitch.bifrost.cardservice.model.repo;
import com.interswitch.bifrost.cardservice.model.Customer; 
import com.interswitch.bifrost.cardservice.model.CustomerDevice;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oladele
 */

@Repository
public interface CustomerRepository {
    

    CustomerDevice findCustomerDeviceAndInstitution(String deviceId, String institutionCD);
    Customer findByCustomerDeviceAndInstitution(String deviceId, String institutionCD);
   
}
