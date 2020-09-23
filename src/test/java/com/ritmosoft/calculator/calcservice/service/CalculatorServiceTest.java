package com.ritmosoft.calculator.calcservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.ritmosoft.calculator.calcservice.model.Calculation;
import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Calculators;
import com.ritmosoft.calculator.calcservice.model.Operation;
import com.ritmosoft.calculator.calcservice.model.OperationType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorServiceTest {
    static private CalculatorService service;

    @BeforeAll
    static public void beforeAll() {
        service = new CalculatorService(() -> { return "testCalculator"; });
    }

    @Test
    @Order(1)
    public void testCreateCalculator() {
        // Create calculator with preset ID.
        Calculator arg = new Calculator();
        arg.setId("calculator");
        arg.setResult(BigDecimal.valueOf(0.0));
        Calculator expected = new Calculator();
        expected.setId("calculator");
        expected.setResult(BigDecimal.valueOf(0.0));
        Calculator actual = service.createCalculator(arg);
        assertEquals(expected, actual);

        // Create calculator with service provided iD.
        Calculator arg2 = new Calculator();
        arg2.setId(null);
        arg2.setResult(BigDecimal.valueOf(0.0));
        Calculator expected2 = new Calculator();
        expected2.setId("testCalculator");
        expected2.setResult(BigDecimal.valueOf(0.0));
        Calculator actual2 = service.createCalculator(arg2);
        assertEquals(expected2, actual2);
    }

    @Test
    @Order(2)
    public void testGetCalculators() {
        Calculator expected1 = new Calculator();
        expected1.setId("calculator");
        expected1.setResult(BigDecimal.valueOf(0.0));
        Calculator expected2 = new Calculator();
        expected2.setId("testCalculator");
        expected2.setResult(BigDecimal.valueOf(0.0));

        Collection<Calculator> expected = Arrays.asList(new Calculator[] {
            expected1, expected2
        });
        Collection<Calculator> actual = service.getCalculators();
        assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }

    @Test
    @Order(3)
    public void testGetCalculator() {
        Calculator expected = new Calculator();
        expected.setId("calculator");
        expected.setResult(BigDecimal.valueOf(0.0));
        Calculator actual = service.getCalculator("calculator");
        assertEquals(expected, actual);

        Calculator expected2 = new Calculator();
        expected2.setId("testCalculator");
        expected2.setResult(BigDecimal.valueOf(0.0));
        Calculator actual2 = service.getCalculator("testCalculator");
        assertEquals(expected2, actual2);
    }

    @Test
    @Order(4)
    public void testDoCalculation() {
        Calculator expected = new Calculator();
        expected.setId("calculator");
        expected.setResult(BigDecimal.valueOf(5.0));
        Operation operation = new Operation();
        operation.setOperand(BigDecimal.valueOf(5.0));
        operation.setOperationType(OperationType.PLUS);
        Operation[] operations = new Operation[] { operation };
        Calculation calculation = new Calculation();
        calculation.setOperations(Arrays.asList(operations));
        Calculator actual = service.doCalculation("calculator", calculation);
        assertEquals(expected, actual);

        Calculator expected2 = new Calculator();
        expected2.setId("calculator");
        expected2.setResult(BigDecimal.valueOf(2.5));
        Operation operation2 = new Operation();
        operation2.setOperand(BigDecimal.valueOf(2.5));
        operation2.setOperationType(OperationType.MINUS);
        Operation[] operations2 = new Operation[] { operation2 };
        Calculation calculation2 = new Calculation();
        calculation2.setOperations(Arrays.asList(operations2));
        Calculator actual2 = service.doCalculation("calculator", calculation2);
        assertEquals(expected2, actual2);

        Calculator expected3 = new Calculator();
        expected3.setId("calculator");
        expected3.setResult(BigDecimal.valueOf(10.0));
        Operation operation3 = new Operation();
        operation3.setOperand(BigDecimal.valueOf(4));
        operation3.setOperationType(OperationType.STAR);
        Operation[] operations3 = new Operation[] { operation3 };
        Calculation calculation3 = new Calculation();
        calculation3.setOperations(Arrays.asList(operations3));
        Calculator actual3 = service.doCalculation("calculator", calculation3);
        assertEquals(expected3, actual3);

        Calculator expected4 = new Calculator();
        expected4.setId("calculator");
        expected4.setResult(BigDecimal.valueOf(5.0));
        Operation operation4 = new Operation();
        operation4.setOperand(BigDecimal.valueOf(2));
        operation4.setOperationType(OperationType.SLASH);
        Operation[] operations4 = new Operation[] { operation4 };
        Calculation calculation4 = new Calculation();
        calculation4.setOperations(Arrays.asList(operations4));
        Calculator actual4 = service.doCalculation("calculator", calculation4);
        assertEquals(expected4, actual4);
    }

    @Test
    @Order(5)
    public void testClear() {
        Calculator expected = new Calculator();
        expected.setId("calculator");
        expected.setResult(BigDecimal.valueOf(0.0));
        Calculator actual = service.clearCalculator("calculator");
        assertEquals(expected, actual);
    }

    @Test
    @Order(6)
    public void testDeleteCalculator() {
        int expected = 0;

        service.deleteCalculator("calculator");
        service.deleteCalculator("testCalculator");
        Calculators actual = service.getCalculators();
        
        assertEquals(expected ,actual.size());
    }

    @Test
    @Order(7)
    public void testGetCalculatorNotFound() {
        try {
            service.getCalculator("NotFound");
            fail("Should not get here!");
        } catch (CalculatorNotFound cnf) {
            String expected = "Calculator with ID NotFound was not found.";
            String actual = cnf.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @Order(8)
    public void testDoCalculationNotFound() {
        try {
            service.doCalculation("NotFound", null);
            fail("Should not get here!");
        } catch (CalculatorNotFound cnf) {
            String expected = "Calculator with ID NotFound was not found.";
            String actual = cnf.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    @Order(9)
    public void testDeleteCalculatorNotFound() {
        try {
            service.deleteCalculator("NotFound");
            fail("Should not get here!");
        } catch (CalculatorNotFound cnf) {
            String expected = "Calculator with ID NotFound was not found.";
            String actual = cnf.getMessage();
            assertEquals(expected, actual);
        }
    }
}

