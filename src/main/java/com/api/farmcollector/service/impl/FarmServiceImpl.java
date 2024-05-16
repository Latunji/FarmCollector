package com.api.farmcollector.service.impl;

import com.api.farmcollector.model.Farm;
import com.api.farmcollector.repository.FarmRepository;
import com.api.farmcollector.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FarmServiceImpl implements FarmService {

    private final List<Farm> farms = new ArrayList<>();

    @Autowired
    private FarmRepository farmRepository;

    @Override
    public Farm createFarm(Farm farm) {
        farmRepository.save(farm);
        return farm;
    }

    @Override
    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    @Override
    public Farm getFarmById(Long id) {
        return farmRepository.findAll().stream()
                .filter(farm -> farm.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Farm not found with id " + id));
    }

    @Override
    public Farm updateFarm(Long id, Farm farm) {
        Optional<Farm> f = farmRepository.findById(id);
        f.get().setName(farm.getName());
        f.get().setFields(farm.getFields());
        farmRepository.save(f.get());
        return f.get();
    }

    @Override
    public void deleteFarm(Long id) {
        farmRepository.deleteById(id);
    }
}
