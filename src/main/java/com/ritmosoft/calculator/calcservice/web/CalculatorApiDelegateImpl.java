package com.ritmosoft.calculator.calcservice.web;

import java.net.URI;

import com.ritmosoft.calculator.calcservice.api.CalculatorApiDelegate;
import com.ritmosoft.calculator.calcservice.model.Operations;
import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Calculators;
import com.ritmosoft.calculator.calcservice.service.CalculatorNotFound;
import com.ritmosoft.calculator.calcservice.service.CalculatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class CalculatorApiDelegateImpl implements CalculatorApiDelegate {
    private final CalculatorService service;

    @Autowired
    public CalculatorApiDelegateImpl(CalculatorService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Calculator> createCalculator(Calculator calculator) {
        Calculator body = service.createCalculator(calculator);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand(calculator.getId())
                                    .toUri();
        return ResponseEntity.created(location).body(body);
    }

    @Override
    public ResponseEntity<Void> deleteCalculator(String id) {
        try {
            service.deleteCalculator(id);
            return ResponseEntity.ok().build();
        } catch (CalculatorNotFound cnf) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Calculator> doCalculationOnCalculatorWithID(String id, Operations operations) {
        try {
            Calculator result = service.doCalculation(id, operations);
            return ResponseEntity.ok(result);
        } catch (CalculatorNotFound cnf) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Calculators> getAllCalculators() {
        return ResponseEntity.ok(service.getCalculators());
    }

    @Override
    public ResponseEntity<Calculator> getCalculatorByID(String id) {
        try {
            Calculator result = service.getCalculator(id);
            return ResponseEntity.ok(result);
        } catch (CalculatorNotFound cnf) {
            return ResponseEntity.notFound().build();
        }
    }
}
