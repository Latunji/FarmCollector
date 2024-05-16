package com.api.farmcollector.service.impl;

import com.api.farmcollector.model.Farm;
import com.api.farmcollector.model.Field;
import com.api.farmcollector.repository.FieldRepository;
import com.api.farmcollector.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService {

    private final List<Field> fields = new ArrayList<>();

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public Field createField(Field field) {
        fieldRepository.save(field);
        return field;
    }

    @Override
    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }

    @Override
    public Field getFieldById(Long id) {
        return fieldRepository.findAll().stream()
                .filter(field -> field.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Field not found with id " + id));
    }

    @Override
    public Field updateField(Long id, Field field) {
        Optional<Field> existingField = fieldRepository.findById(id);
        existingField.get().setSeason(field.getSeason());
        existingField.get().setCrop(field.getCrop());
        existingField.get().setPlantingArea(field.getPlantingArea());
        existingField.get().setExpectedProduct(field.getExpectedProduct());
        existingField.get().setActualHarvestedProduct(field.getActualHarvestedProduct());
        fieldRepository.save(existingField.get());
        return existingField.get();
    }

    @Override
    public void deleteField(Long id) {
        fieldRepository.deleteById(id);
    }
}
