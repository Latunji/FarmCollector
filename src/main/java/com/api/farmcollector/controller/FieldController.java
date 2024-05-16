package com.api.farmcollector.controller;

import com.api.farmcollector.model.Field;
import com.api.farmcollector.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/farms/{farmId}/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @PostMapping
    public Field createField(@RequestBody Field field) {
        return fieldService.createField(field);
    }

    @GetMapping
    public List<Field> getAllFields() {
        return fieldService.getAllFields();
    }

    @GetMapping("/{fieldId}")
    public Field getFieldById(@PathVariable Long fieldId) {
        return fieldService.getFieldById(fieldId);
    }

    @PutMapping("/{fieldId}")
    public Field updateField(@PathVariable Long fieldId, @RequestBody Field field) {
        return fieldService.updateField(fieldId, field);
    }

    @DeleteMapping("/{fieldId}")
    public void deleteField(@PathVariable Long fieldId) {
        fieldService.deleteField(fieldId);
    }
}
