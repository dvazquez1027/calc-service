package com.ritmosoft.calculator.calcservice.service;

import java.util.Stack;

import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Operation;
import com.ritmosoft.calculator.calcservice.model.OperationType;

import org.springframework.beans.factory.xml.PluggableSchemaResolver;


public class CalculatorBrain {
    private Calculator calculator = new Calculator();
    private Stack<Operation> stack = new Stack<>();

    public CalculatorBrain() {
        this.calculator = new Calculator();
    }

    public CalculatorBrain(Calculator calculator) {
        this.calculator = calculator;
    }

    public Calculator doCalculation(Operation operation) {
        Operation workingOp = new Operation();
        workingOp.setOperationType(operation.getOperationType());
        workingOp.setOperand(operation.getOperand());

        if (!stack.isEmpty()) {
            while (!stack.isEmpty() &&
                   precedenceOf(operation.getOperationType()) <= precedenceOf(stack.peek().getOperationType())) {
                Operation op = stack.pop();
                switch (op.getOperationType()) {
                    case PLUS:
                        workingOp.setOperand(op.getOperand().add(workingOp.getOperand()));
                        break;
                    case MINUS:
                        workingOp.setOperand(op.getOperand().subtract(workingOp.getOperand()));
                        break;
                    case STAR:
                        workingOp.setOperand(op.getOperand().multiply(workingOp.getOperand()));
                        break;
                    case SLASH:
                        workingOp.setOperand(op.getOperand().divide(workingOp.getOperand()));
                        break;
                    default: 
                        break;
                }
            }
        }
        if (operation.getOperationType() != OperationType.EQUAL) {
            stack.push(workingOp);
        }
        calculator.setResult(workingOp.getOperand());
        return calculator;
    }

    private int precedenceOf(OperationType operation) {
        switch (operation) {
            case PLUS:
            case MINUS:
                return 1;

            case SLASH:
            case STAR:
                return 2;

            case EQUAL:
                return 0;

            default:
                return -1;
        }
    }

    public Calculator getCalculator() {
        return calculator;
    }
}
