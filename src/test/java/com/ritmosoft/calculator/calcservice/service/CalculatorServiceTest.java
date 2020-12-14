package com.ritmosoft.calculator.calcservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.ritmosoft.calculator.calcservice.model.Calculation;
import com.ritmosoft.calculator.calcservice.model.Calculator;
import com.ritmosoft.calculator.calcservice.model.Operation;
import com.ritmosoft.calculator.calcservice.model.OperationType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorServiceTest {
    @BeforeAll
    static public void beforeAll() {
    }

    @Test
    // Create calculator with preset ID.
    public void testCreateCalculatorPresetId() {
        // given
        CalculatorService service = createCalculatorService();

        // when
        Calculator actual = service.createCalculator(createCalculator("calculator", 0.0));

        // then
        Calculator expected = createCalculator("calculator", 0.0);
        assertEquals(expected, actual);
    }

    @Test
    // Create calculator with generated ID.
    public void testCreateCalculatorGeneratedId() {
        // given
        CalculatorService service = createCalculatorService();

        // when
        Calculator actual = service.createCalculator(createCalculator(null, 0.0));

        // then
        Calculator expected = createCalculator("testCalculator", 0.0);
        assertEquals(expected, actual);
    }

    @Test
    // Get all calculators
    public void testGetCalculators() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator("calculator", 0.0));
        service.createCalculator(createCalculator(null, 0.0));

        // when
        Collection<Calculator> actual = service.getCalculators();

        // then
        assertEquals(2, actual.size());
        Calculator expected1 = createCalculator("calculator", 0.0);
        Calculator expected2 = createCalculator("testCalculator", 0.0);
        Collection<Calculator> expected = Arrays.asList(new Calculator[] {
            expected1, expected2
        });
        assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }

    @Test
    // Get a single calculator by ID.
    public void testGetCalculator() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 5.0));

        // when
        Calculator actual = service.getCalculator("testCalculator");

        // then
        Calculator expected = createCalculator("testCalculator", 5.0);
        assertEquals(expected, actual);
    }

    @Test
    // Addition
    public void testDoCalculationAdd() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.PLUS, 5.0));

        // then
        Calculator expected = createCalculator("testCalculator", 5.0);
        assertEquals(expected, actual);

        // and when
        actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));

        // and then
        expected = createCalculator("testCalculator", 10.0);
        assertEquals(expected, actual);
    }

    @Test
    // Subtraction
    public void testDoCalculationSubtract() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.MINUS, 25.0));

        // then
        Calculator expected = createCalculator("testCalculator", 25.0);
        assertEquals(expected, actual);

        // and when
        actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));

        // and then
        expected = createCalculator("testCalculator", 20.0);
        assertEquals(expected, actual);
    }

    @Test
    // Multiply
    public void testDoCalculationMultiply() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.STAR, 5.0));

        // then
        Calculator expected = createCalculator("testCalculator", 5.0);
        assertEquals(expected, actual);

        // and when
        actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));

        // and then
        expected = createCalculator("testCalculator", "25.00");
        assertEquals(expected, actual);
    }

    @Test
    // Divide
    public void testDoCalculationDivide() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.SLASH, 5.0));

        // then
        Calculator expected = createCalculator("testCalculator", 5.0);
        assertEquals(expected, actual);

        // and when
        actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));

        // and then
        expected = createCalculator("testCalculator", "1");
        assertEquals(expected, actual);
    }

    @Test
    // Obeys order of operations
    public void testDoCalculationOrderOfOperations() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when - 5 + 10 * 5 + 5
        service.doCalculation("testCalculator", createCalculation(OperationType.PLUS, 5));
        service.doCalculation("testCalculator", createCalculation(OperationType.STAR, 10));
        service.doCalculation("testCalculator", createCalculation(OperationType.PLUS, 5));
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5));

        // then
        Calculator expected = createCalculator("testCalculator", "60.00");
        assertEquals(expected, actual);
    }

    @Test
    // Clears when told to clear
    public void testClear() {
        // given
        CalculatorService service = createCalculatorService();
        Calculator actual = service.createCalculator(createCalculator("clearCalculator", 5.0));

        //when
        actual = service.clearCalculator("clearCalculator");

        // then
        assertEquals(createCalculator("clearCalculator", 0.0), actual);
    }

    @Test
    public void testDeleteCalculator() {
        // given
        CalculatorService service = createCalculatorService();
        Calculator actual = service.createCalculator(createCalculator("deleteCalculator", 0.0));

        // when
        actual = service.deleteCalculator("deleteCalculator");
        
        // then
        assertEquals(createCalculator("deleteCalculator", 0.0), actual);
        assertEquals(0, service.getCalculators().size());
        try {
            service.getCalculator("deleteCalculator");
        } catch (CalculatorNotFound cnf) {
            assertEquals("Calculator with ID deleteCalculator was not found.", cnf.getMessage());
        }
    }

    @Test
    public void testGetCalculatorNotFound() {
        CalculatorService service = createCalculatorService();
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
    public void testDoCalculationNotFound() {
        CalculatorService service = createCalculatorService();
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
    public void testDeleteCalculatorNotFound() {
        CalculatorService service = createCalculatorService();
        try {
            service.deleteCalculator("NotFound");
            fail("Should not get here!");
        } catch (CalculatorNotFound cnf) {
            String expected = "Calculator with ID NotFound was not found.";
            String actual = cnf.getMessage();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRepeatsLastOperation() {
        // given
        CalculatorService service = createCalculatorService();
        service.createCalculator(createCalculator(null, 0.0));

        // when
        service.doCalculation("testCalculator", createCalculation(OperationType.PLUS, 5.0));
        service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));
        Calculator actual = service.doCalculation("testCalculator", createCalculation(OperationType.EQUAL, 5.0));

        // then
        Calculator expected = createCalculator("testCalculator", 15.0);
        assertEquals(expected, actual);
    }

    private CalculatorService createCalculatorService() {
       return new CalculatorService(() -> { return "testCalculator"; });
    }

    private Calculator createCalculator(String id, double result) {
        Calculator newCalculator = new Calculator();
        newCalculator.setId(id);
        newCalculator.setResult(BigDecimal.valueOf(result));
        return newCalculator;
    }

    private Calculator createCalculator(String id, String resultAsString) {
        Calculator newCalculator = new Calculator();
        newCalculator.setId(id);
        newCalculator.setResult(new BigDecimal(resultAsString));
        return newCalculator;
    }

    private Calculation createCalculation(OperationType operation, double operand) {
        Calculation ret = new Calculation();
        Operation operationsItem = new Operation();
        operationsItem.setOperationType(operation);
        operationsItem.setOperand(BigDecimal.valueOf(operand));
		ret.addOperationsItem(operationsItem);
        return ret;
    }
}

