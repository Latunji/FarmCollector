package com.api.farmcollector.service.impl;

import com.api.farmcollector.enums.Crop;
import com.api.farmcollector.enums.Season;
import com.api.farmcollector.model.Farm;
import com.api.farmcollector.model.Field;
import com.api.farmcollector.repository.FarmRepository;
import com.api.farmcollector.service.FarmService;
import com.api.farmcollector.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    private final FarmService farmService;

    @Autowired
    private FarmRepository farmRepository;

    public ReportServiceImpl(FarmService farmService) {
        this.farmService = farmService;
    }

    @Override
    public Map<Farm, Double> generateFarmReports(Season season) {
        Map<Farm, Double> farmReports = new HashMap<>();
        for (Farm farm : farmService.getAllFarms()) {
            double expectedProduct = farm.getFields().stream()
                    .filter(field -> field.getCrop() != null)
                    .filter(field -> field.getSeason() == season)
                    .mapToDouble(Field::getExpectedProduct)
                    .sum();
            double actualHarvestedProduct = farm.getFields().stream()
                    .filter(field -> field.getCrop() != null)
                    .filter(field -> field.getSeason() == season)
                    .mapToDouble(Field::getActualHarvestedProduct)
                    .sum();
            farmReports.put(farm, expectedProduct - actualHarvestedProduct);
        }
        return farmReports;
    }

    @Override
    public Map<Crop, Double> generateCropReports(Season season) {
        Map<Crop, Double> cropReports = new HashMap<>();
        for (Crop crop : Crop.values()) {
            double expectedProduct = farmService.getAllFarms().stream()
                    .flatMap(farm -> farm.getFields().stream())
                    .filter(field -> field.getCrop() == crop)
                    .filter(field -> field.getSeason() == season)
                    .mapToDouble(Field::getExpectedProduct)
                    .sum();
            double actualHarvestedProduct = farmService.getAllFarms().stream()
                    .flatMap(farm -> farm.getFields().stream())
                    .filter(field -> field.getCrop() == crop)
                    .filter(field -> field.getSeason() == season)
                    .mapToDouble(Field::getActualHarvestedProduct)
                    .sum();
            cropReports.put(crop, expectedProduct - actualHarvestedProduct);
        }
        return cropReports;
    }
}
