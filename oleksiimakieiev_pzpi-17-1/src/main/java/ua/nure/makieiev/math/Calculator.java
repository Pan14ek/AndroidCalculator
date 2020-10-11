package ua.nure.makieiev.math;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BinaryOperator;

public class Calculator {

    public static final char MULTIPLY_SYMBOL = '*';
    public static final char DIVIDE_SYMBOL = '/';
    public static final char PLUS_SYMBOL = '+';
    public static final char MINUS_SYMBOL = '-';
    public static final char ZERO_NUMBER = '0';
    public static final char NINE_NUMBER = '9';
    public static final char ENTRY_BRACE = '(';
    public static final char END_BRACE = ')';

    private String inputLine;
    private final Stack<Character> symbols;
    private final Stack<BigDecimal> numbers;
    private final Map<Character, Integer> priorities;
    private final Map<Character, BinaryOperator<BigDecimal>> operations;

    public Calculator() {
        symbols = new Stack<>();
        numbers = new Stack<>();
        priorities = obtainPriorities();
        operations = obtainOperations();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public BigDecimal calculate() {
        char[] elements = inputLine.toCharArray();
        StringBuilder tempStringBuilder = new StringBuilder();
        for (char element : elements) {
            checkElementOnNumberCharacter(tempStringBuilder, element);
            tempStringBuilder = checkElementOnOperation(tempStringBuilder, element);
            checkOnEntryBrace(element);
            checkOnEndBrace(element);
        }
        doLastOperations();
        return numbers.pop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doLastOperations() {
        while (!symbols.empty()) {
            doOperation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkOnEndBrace(char element) {
        if (element == END_BRACE) {
            if (symbols.peek() != ENTRY_BRACE) {
                while (symbols.peek() != ENTRY_BRACE) {
                    doOperation();
                }
            }
            symbols.pop();
        }
    }

    private void checkOnEntryBrace(char element) {
        if (element == ENTRY_BRACE) {
            symbols.push(element);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private StringBuilder checkElementOnOperation(StringBuilder tempStringBuilder, char element) {
        if (element == MULTIPLY_SYMBOL || element == DIVIDE_SYMBOL ||
                element == PLUS_SYMBOL || element == MINUS_SYMBOL) {
            tempStringBuilder = new StringBuilder();
            checkSymbol(element);
        }
        return tempStringBuilder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkSymbol(char element) {
        if (symbols.empty()) {
            symbols.push(element);
        } else {
            if (symbols.peek() == ENTRY_BRACE) {
                symbols.push(element);
            } else {
                int nextPriority = priorities.get(element);
                int previousPriority = priorities.get(symbols.peek());
                checkSymbolPriorities(element, nextPriority, previousPriority);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkSymbolPriorities(char element, int nextPriority, int previousPriority) {
        if (nextPriority > previousPriority) {
            symbols.push(element);
        } else if (nextPriority == previousPriority) {
            doOperation();
            symbols.push(element);
        } else {
            checkSymbolOnEntryBrace();
            symbols.push(element);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkSymbolOnEntryBrace() {
        if (symbols.contains(ENTRY_BRACE)) {
            while (symbols.peek() != ENTRY_BRACE) {
                doOperation();
            }
        } else {
            doOperation();
        }
    }

    private void checkElementOnNumberCharacter(StringBuilder tempStringBuilder, char element) {
        if (element > ZERO_NUMBER && element < NINE_NUMBER) {
            tempStringBuilder.append(element);
            if (tempStringBuilder.length() > 1) {
                numbers.pop();
                BigDecimal newNumber = new BigDecimal(tempStringBuilder.toString());
                numbers.push(newNumber);
            } else {
                numbers.push(new BigDecimal(tempStringBuilder.toString()));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doOperation() {
        char symbol = symbols.pop();
        BigDecimal firstNumber = numbers.pop();
        BigDecimal secondNumber = numbers.pop();
        numbers.push(Objects.requireNonNull(operations.get(symbol)).apply(secondNumber, firstNumber));
    }

    public void writeLine(String line) {
        this.inputLine = line;
    }

    private Map<Character, Integer> obtainPriorities() {
        Map<Character, Integer> priorities = new HashMap<>();
        priorities.put(MULTIPLY_SYMBOL, 2);
        priorities.put(DIVIDE_SYMBOL, 2);
        priorities.put(PLUS_SYMBOL, 1);
        priorities.put(MINUS_SYMBOL, 1);
        return priorities;
    }

    private Map<Character, BinaryOperator<BigDecimal>> obtainOperations() {
        Map<Character, BinaryOperator<BigDecimal>> operations = new HashMap<>();
        operations.put(MULTIPLY_SYMBOL, BigDecimal::multiply);
        operations.put(DIVIDE_SYMBOL, BigDecimal::divide);
        operations.put(PLUS_SYMBOL, BigDecimal::add);
        operations.put(MINUS_SYMBOL, BigDecimal::subtract);
        return operations;
    }

}
