package com.api.farmcollector.controller;

import com.api.farmcollector.enums.Crop;
import com.api.farmcollector.enums.Season;
import com.api.farmcollector.model.Farm;
import com.api.farmcollector.service.FarmService;
import com.api.farmcollector.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/generateFarmReport/{season}")
    public Map<Farm, Double> getFarmById(@PathVariable Season season) {
        return reportService.generateFarmReports(season);
    }

    @GetMapping("/generateCropReport/{season}")
    public Map<Crop, Double> getCropById(@PathVariable Season season) {
        return reportService.generateCropReports(season);
    }
}
