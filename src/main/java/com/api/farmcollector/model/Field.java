package com.api.farmcollector.model;

import com.api.farmcollector.enums.Crop;
import com.api.farmcollector.enums.Season;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double plantingArea;
    private Crop crop;
    private Season season;
    private Double expectedProduct;
    private Double actualHarvestedProduct;
}
