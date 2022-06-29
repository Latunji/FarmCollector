package com.interswitch.bifrost.cardservice.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author olise
 */
@Embeddable
public class Device implements Serializable {
    private String uniqueId;
    private String model;
    private String deviceName;

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
}
