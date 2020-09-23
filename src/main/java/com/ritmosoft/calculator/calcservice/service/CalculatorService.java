package com.ritmosoft.calculator.calcservice.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.ritmosoft.calculator.calcservice.generator.IDGenerator;
import com.ritmosoft.calculator.calcservice.model.Calculation;
import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Calculators;
import com.ritmosoft.calculator.calcservice.model.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    private final IDGenerator calculatorIDGenerator;
    private final Map<String, Calculator> storage = new HashMap<>();

    @Autowired
    public CalculatorService(final IDGenerator calculatorIDGenerator) {
        this.calculatorIDGenerator = calculatorIDGenerator;
    }

    public Calculator createCalculator(final Calculator calculator) {
        if (calculator.getId() == null) {
            calculator.setId(calculatorIDGenerator.get());
        }
        storage.put(calculator.getId(), calculator);
        return calculator;
    }

    public Calculators getCalculators() {
        Calculators ret = new Calculators();
        ret.addAll(storage.values());
        return ret;
    }

    public Calculator getCalculator(final String id) {
        Calculator ret = storage.get(id);
        if (ret != null) {
            return ret;
        } else {
            throw new CalculatorNotFound(id);
        }
    }

    public Calculator doCalculation(final String id, final Calculation calculation) {
        final Calculator ret = getCalculator(id);
        doCalculation(ret, calculation);
        return ret;
    }

    private void doCalculation(Calculator calculator, Calculation calculation) {
        for (Operation op : calculation.getOperations()) {
            switch (op.getOperationType()) {
                case PLUS:
                    calculator.setResult(calculator.getResult().add(op.getOperand()));
                    break;
                case MINUS:
                    calculator.setResult(calculator.getResult().subtract(op.getOperand()));
                    break;
                case STAR:
                    calculator.setResult(calculator.getResult().multiply(op.getOperand()));
                    break;
                case SLASH:
                    calculator.setResult(calculator.getResult().divide(op.getOperand()));
                    break;
                default: 
                    break;
            }
        }
    }

    public Calculator clearCalculator(final String id) {
        final Calculator ret = getCalculator(id);
        ret.setResult(BigDecimal.valueOf(0.0));
        return ret;
    }

    public Calculator deleteCalculator(final String id) {
        Calculator ret = storage.get(id);
        if (ret != null) {
            return storage.remove(id);
        } else {
            throw new CalculatorNotFound(id);
        }
    }
}