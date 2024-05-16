package com.api.farmcollector.service;

import com.api.farmcollector.enums.Crop;
import com.api.farmcollector.enums.Season;
import com.api.farmcollector.model.Farm;

import java.util.Map;

public interface ReportService {
    Map<Farm, Double> generateFarmReports(Season season);
    Map<Crop, Double> generateCropReports(Season season);
}
