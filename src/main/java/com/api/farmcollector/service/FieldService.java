package com.api.farmcollector.service;

import com.api.farmcollector.model.Farm;
import com.api.farmcollector.model.Field;

import java.util.List;

public interface FieldService {

    Field createField(Field field);
    List<Field> getAllFields();
    Field getFieldById(Long id);
    Field updateField(Long id, Field field);
    void deleteField(Long id);
}
