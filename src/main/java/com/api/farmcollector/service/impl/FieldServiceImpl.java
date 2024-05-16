package com.api.farmcollector.service.impl;

import com.api.farmcollector.model.Farm;
import com.api.farmcollector.model.Field;
import com.api.farmcollector.service.FieldService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldServiceImpl implements FieldService {

    private final List<Field> fields = new ArrayList<>();

    @Override
    public Field createField(Field field) {
        fields.add(field);
        return field;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public Field getFieldById(Long id) {
        return fields.stream()
                .filter(field -> field.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Field not found with id " + id));
    }

    @Override
    public Field updateField(Long id, Field field) {
        Field existingField = getFieldById(id);
        existingField.setSeason(field.getSeason());
        existingField.setCrop(field.getCrop());
        existingField.setPlantingArea(field.getPlantingArea());
        existingField.setExpectedProduct(field.getExpectedProduct());
        existingField.setActualHarvestedProduct(field.getActualHarvestedProduct());
        return existingField;
    }

    @Override
    public void deleteField(Long id) {
        fields.removeIf(field -> field.getId().equals(id));
    }
}
