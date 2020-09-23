package com.ritmosoft.calculator.calcservice.service;

public class CalculatorNotFound extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CalculatorNotFound(String id) {
        super("Calculator with ID " + id + " was not found.");
    }
}
