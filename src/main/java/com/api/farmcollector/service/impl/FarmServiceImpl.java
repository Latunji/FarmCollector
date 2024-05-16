package com.api.farmcollector.service.impl;

import com.api.farmcollector.model.Farm;
import com.api.farmcollector.service.FarmService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmServiceImpl implements FarmService {

    private final List<Farm> farms = new ArrayList<>();

    @Override
    public Farm createFarm(Farm farm) {
        farms.add(farm);
        return farm;
    }

    @Override
    public List<Farm> getAllFarms() {
        return farms;
    }

    @Override
    public Farm getFarmById(Long id) {
        return farms.stream()
                .filter(farm -> farm.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Farm not found with id " + id));
    }

    @Override
    public Farm updateFarm(Long id, Farm farm) {
        Farm existingFarm = getFarmById(id);
        existingFarm.setName(farm.getName());
        existingFarm.setFields(farm.getFields());
        return existingFarm;
    }

    @Override
    public void deleteFarm(Long id) {
        farms.removeIf(farm -> farm.getId().equals(id));
    }
}
