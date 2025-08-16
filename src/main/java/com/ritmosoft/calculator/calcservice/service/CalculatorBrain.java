package com.ritmosoft.calculator.calcservice.service;

import java.math.BigDecimal;
import java.util.Stack;

import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Operation;
import com.ritmosoft.calculator.calcservice.model.Operator;


public class CalculatorBrain {
    private Calculator calculator = new Calculator();
    private Stack<Operation> stack = new Stack<>();
    private Operation lastOperation;

    public CalculatorBrain() {
        this.calculator = new Calculator();
    }

    public CalculatorBrain(Calculator calculator) {
        this.calculator = calculator;
    }

    public Calculator doCalculation(Operation operation) {
        Operation workingOp = new Operation();
        workingOp.setOperator(operation.getOperator());
        workingOp.setOperand(operation.getOperand());

        if (!stack.isEmpty()) {
            lastOperation = stack.peek();
            while (!stack.isEmpty() &&
                   precedenceOf(operation.getOperator()) <= precedenceOf(stack.peek().getOperator())) {
                Operation op = stack.pop();
                workingOp.setOperand(operate(op.getOperator(), op.getOperand(), workingOp.getOperand()));
            }
        } else if (lastOperation != null && operation.getOperator() == Operator.EVALUATE) {
            workingOp.setOperand(operate(lastOperation.getOperator(), calculator.getResult(), lastOperation.getOperand()));
        }
        if (operation.getOperator() != Operator.EVALUATE) {
            stack.push(workingOp);
        }
        calculator.setResult(workingOp.getOperand());
        return calculator;
    }

    private int precedenceOf(Operator operation) {
        switch (operation) {
            case ADD:
            case SUBTRACT:
                return 1;

            case DIVIDE:
            case MULTIPLY:
                return 2;

            case EVALUATE:
                return 0;

            default:
                return -1;
        }
    }

    private BigDecimal operate(Operator opType, BigDecimal left, BigDecimal right) {
        BigDecimal ret;
        switch (opType) {
            case ADD:
                ret = left.add(right);
                break;
            case SUBTRACT:
                ret = left.subtract(right);
                break;
            case MULTIPLY:
                ret = left.multiply(right);
                break;
            case DIVIDE:
                ret = left.divide(right);
                break;
            default:
                ret = left;
                break;
        }
        return ret;
    }

    public Calculator getCalculator() {
        return calculator;
    }
}
