package com.stream.ai_agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(
            name = "calculator",
            description = """
            Performs mathematical calculations using two numeric inputs.

            Binary operations that require both numbers:
            +, -, *, /, %, ^, pow, min, max

            Unary operations that use only num1:
            sqrt, cbrt, abs, floor, ceil, round,
            sin, cos, tan, asin, acos, atan,
            log, log10, exp

            Trigonometric functions use radians.

            For unary operations, num2 is ignored.

            Use this tool whenever an exact mathematical calculation is required.
            Do not perform arithmetic mentally when this tool can calculate it.
            """
    )
    public double calculate(

            @ToolParam(
                    description = """
                    Mathematical operation to perform.
                    Supported values:
                    +, -, *, /, %, ^, pow,
                    sqrt, cbrt, abs, floor, ceil, round,
                    sin, cos, tan, asin, acos, atan,
                    log, log10, exp, min, max.
                    """
            )
            String operation,

            @ToolParam(
                    description = """
                    First numeric operand.
                    For unary operations such as sqrt, sin, cos, log,
                    this is the number on which the operation is performed.
                    """
            )
            double num1,

            @ToolParam(
                    description = """
                    Second numeric operand.
                    Required for binary operations such as +, -, *, /, %,
                    ^, min, and max.
                    Ignored for unary operations.
                    """
            )
            double num2
    ) {

        System.out.println("Calculator tool called");
        validateNumber(num1, "num1");
        validateNumber(num2, "num2");

        if (operation == null || operation.isBlank()) {
            throw new IllegalArgumentException(
                    "Operation must not be null or blank"
            );
        }

        String op = operation.trim().toLowerCase();

        return switch (op) {

            // ============================================================
            // BASIC ARITHMETIC
            // ============================================================

            case "+" -> num1 + num2;

            case "-" -> num1 - num2;

            case "*" -> num1 * num2;

            case "/" -> divide(num1, num2);

            case "%" -> modulo(num1, num2);

            // ============================================================
            // POWER
            // ============================================================

            case "^", "pow" -> Math.pow(num1, num2);

            // ============================================================
            // ROOTS
            // ============================================================

            case "sqrt" -> squareRoot(num1);

            case "cbrt" -> Math.cbrt(num1);

            // ============================================================
            // ABSOLUTE VALUE
            // ============================================================

            case "abs" -> Math.abs(num1);

            // ============================================================
            // ROUNDING
            // ============================================================

            case "floor" -> Math.floor(num1);

            case "ceil" -> Math.ceil(num1);

            case "round" -> Math.round(num1);

            // ============================================================
            // TRIGONOMETRY
            // ============================================================

            case "sin" -> Math.sin(num1);

            case "cos" -> Math.cos(num1);

            case "tan" -> Math.tan(num1);

            // ============================================================
            // INVERSE TRIGONOMETRY
            // ============================================================

            case "asin" -> asin(num1);

            case "acos" -> acos(num1);

            case "atan" -> Math.atan(num1);

            // ============================================================
            // LOGARITHMS
            // ============================================================

            case "log" -> naturalLog(num1);

            case "log10" -> log10(num1);

            // ============================================================
            // EXPONENTIAL
            // ============================================================

            case "exp" -> Math.exp(num1);

            // ============================================================
            // MIN / MAX
            // ============================================================

            case "min" -> Math.min(num1, num2);

            case "max" -> Math.max(num1, num2);

            // ============================================================
            // UNKNOWN OPERATION
            // ============================================================

            default -> throw new IllegalArgumentException(
                    "Unsupported operation: " + operation
            );
        };
    }

    // ====================================================================
    // VALIDATION
    // ====================================================================

    private void validateNumber(double value, String parameterName) {

        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(
                    parameterName + " cannot be NaN"
            );
        }

        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(
                    parameterName + " must be a finite number"
            );
        }
    }

    // ====================================================================
    // DIVISION
    // ====================================================================

    private double divide(double num1, double num2) {

        if (num2 == 0.0) {
            throw new ArithmeticException(
                    "Cannot divide by zero"
            );
        }

        return num1 / num2;
    }

    // ====================================================================
    // MODULO
    // ====================================================================

    private double modulo(double num1, double num2) {

        if (num2 == 0.0) {
            throw new ArithmeticException(
                    "Cannot calculate modulo with zero"
            );
        }

        return num1 % num2;
    }

    // ====================================================================
    // SQUARE ROOT
    // ====================================================================

    private double squareRoot(double num1) {

        if (num1 < 0) {
            throw new ArithmeticException(
                    "Square root is not defined for negative numbers"
            );
        }

        return Math.sqrt(num1);
    }

    // ====================================================================
    // ARCSIN
    // ====================================================================

    private double asin(double num1) {

        if (num1 < -1 || num1 > 1) {
            throw new ArithmeticException(
                    "asin input must be between -1 and 1"
            );
        }

        return Math.asin(num1);
    }

    // ====================================================================
    // ARCCOS
    // ====================================================================

    private double acos(double num1) {

        if (num1 < -1 || num1 > 1) {
            throw new ArithmeticException(
                    "acos input must be between -1 and 1"
            );
        }

        return Math.acos(num1);
    }

    // ====================================================================
    // NATURAL LOG
    // ====================================================================

    private double naturalLog(double num1) {

        if (num1 <= 0) {
            throw new ArithmeticException(
                    "Natural logarithm is only defined for positive numbers"
            );
        }

        return Math.log(num1);
    }

    // ====================================================================
    // LOG BASE 10
    // ====================================================================

    private double log10(double num1) {

        if (num1 <= 0) {
            throw new ArithmeticException(
                    "Log10 is only defined for positive numbers"
            );
        }

        return Math.log10(num1);
    }
}