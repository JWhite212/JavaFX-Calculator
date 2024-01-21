package jamie.javafx.calculator;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Model {

    private OptionalDouble previousOperand;
    private OptionalDouble currentOperand;
    private String operator;

    private Map<String, BinaryOperator<Double>> binaryOperators = Map.of(
            "+", (input1, input2) -> input1 + input2,
            "-", (input1, input2) -> input1 - input2,
            "*", (input1, input2) -> input1 * input2,
            "÷", (input1, input2) -> input1 / input2,
            "Mod", (input1, input2) -> input1 % input2);

    private Map<String, UnaryOperator<Double>> unaryOperators = Map.of(
            "+/-", input -> -input,
            "√", input -> Math.sqrt(input),
            "x^2", input -> input * input,
            "log10", input -> Math.log10(input));

    public Model() { reset(); }

    public void reset() {

        previousOperand = OptionalDouble.empty();
        operator = "";
        currentOperand = OptionalDouble.empty();
    }

    public OptionalDouble getPreviousOperand() { return previousOperand; }

    public void setPreviousOperand(OptionalDouble value) { previousOperand = value; }

    public String getOperator() { return operator; }

    public void setOperator(String operator) { this.operator = operator; }

    public OptionalDouble getCurrentOperand() { return currentOperand; }

    public void setCurrentOperand(OptionalDouble value) { currentOperand = value; }

    public OptionalDouble binaryOperation() {

        if (previousOperand.isEmpty() || currentOperand.isEmpty()) {
            throw new NullPointerException("Missing input");
        }

        var input1 = previousOperand.getAsDouble();
        var input2 = currentOperand.getAsDouble();
        var result = OptionalDouble.empty();
        if (binaryOperators.containsKey(operator)) {
            result = OptionalDouble.of(binaryOperators.get(operator)
                                                      .apply(input1, input2));
        }
        return result;
    }

    public OptionalDouble unaryOperation(String operator) {

        if (currentOperand.isEmpty()) {
            throw new NullPointerException("Missing input");
        }

        var input = currentOperand.getAsDouble();
        var result = OptionalDouble.empty();
        if (unaryOperators.containsKey(operator)) {
            result = OptionalDouble.of(unaryOperators.get(operator)
                                                     .apply(input));
        }
        return result;
    }
}