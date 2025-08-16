package com.ritmosoft.calculator.calcservice.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ritmosoft.calculator.calcservice.generator.IDGenerator;
import com.ritmosoft.calculator.calcservice.model.Operations;
import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Calculators;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    private final IDGenerator calculatorIDGenerator;
    private final Map<String, CalculatorBrain> storage = new HashMap<>();

    public CalculatorService(final IDGenerator calculatorIDGenerator) {
        this.calculatorIDGenerator = calculatorIDGenerator;
    }

    public Calculator createCalculator(final Calculator calculator) {
        if (calculator.getId() == null) {
            calculator.setId(calculatorIDGenerator.get());
        }
        storage.put(calculator.getId(), new CalculatorBrain(calculator));
        return calculator;
    }

    public Calculators getCalculators() {
        Calculators ret = new Calculators();
        storage.values().forEach(cb -> ret.add(cb.getCalculator()));
        return ret;
    }

    public Calculator getCalculator(final String id) {
        CalculatorBrain cb = storage.get(id);
        if (cb != null) {
            return cb.getCalculator();
        } else {
            throw new CalculatorNotFound(id);
        }
    }

    public Calculator doCalculation(final String id, final Operations operations) {
        final CalculatorBrain cb = getCalculatorBrain(id);
        if (cb != null) {
            doCalculation(cb, operations);
            return cb.getCalculator();
        } else {
            throw new CalculatorNotFound(id);
        }
    }

    private void doCalculation(CalculatorBrain calculatorBrain, Operations operations) {
        operations.forEach(op -> calculatorBrain.doCalculation(op));
    }

    public Calculator clearCalculator(final String id) {
        final Calculator ret = getCalculator(id);
        ret.setResult(BigDecimal.valueOf(0.0));
        return ret;
    }

    public Calculator deleteCalculator(final String id) {
        CalculatorBrain cb = storage.get(id);
        if (cb != null) {
            return storage.remove(id).getCalculator();
        } else {
            throw new CalculatorNotFound(id);
        }
    }

    private CalculatorBrain getCalculatorBrain(String id) {
        return storage.get(id);
    }
}