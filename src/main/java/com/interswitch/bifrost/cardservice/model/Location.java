package com.interswitch.bifrost.cardservice.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author olise
 */
@Embeddable
public class Location implements Serializable
{
    private String longitude, latitude;
    
    public Location() {}
    
    public Location(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }
}
