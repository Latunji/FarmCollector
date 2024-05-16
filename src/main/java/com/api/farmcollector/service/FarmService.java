package com.api.farmcollector.service;

import com.api.farmcollector.model.Farm;

import java.util.List;

public interface FarmService {
    Farm createFarm(Farm farm);
    List<Farm> getAllFarms();
    Farm getFarmById(Long id);
    Farm updateFarm(Long id, Farm farm);
    void deleteFarm(Long id);
}
