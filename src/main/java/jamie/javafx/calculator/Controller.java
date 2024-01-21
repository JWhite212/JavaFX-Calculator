package jamie.javafx.calculator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.OptionalDouble;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final Model model;
    private final StringProperty formula;
    private final StringProperty display;

    @FXML
    public Label lblDisplay;

    @FXML
    public Label lblFormula;

    public Controller() {

        model = new Model();
        formula = new SimpleStringProperty("");
        display = new SimpleStringProperty("");
    }

    public String getFormula() { return formula.get(); }

    public void setFormula(String formula) { this.formula.set(formula); }

    public String getDisplay() { return display.get(); }

    public void setDisplay(String display) { this.display.set(display); }

    public Model getModel() { return model; }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblFormula.textProperty().bind(formula);
        lblDisplay.textProperty().bind(display);
        clearAll();
    }

    /**
     * A method used to clear both the formula and display labels, as well as clear all assigned
     * operand and operator values.
     */
    @FXML
    public void clearAll() {

        //TODO
        setDisplay(""); //Clears Display Label
        setFormula(""); //Clears Formula Label
        model.reset(); //Resets Current and previous operands, and operator values to empty.
    }

    /**
     * A method used to clear the display label and reset the current operand to empty.
     */
    @FXML
    public void delete() {

        //TODO
        if(isAnswerState()){
            clearAll();
        } else {
            setDisplay("");
            model.setCurrentOperand(OptionalDouble.empty());
        }
    }

    /**
     * A method linked to the number buttons on the calculator. The method enters the pressed number as a value into
     * the calculator.
     *
     * @param event The event parameter for the ActionEvent of the pressed button.
     */
    @FXML
    public void processNumber(ActionEvent event) {

        //TODO
        if(isErrorState()){ // If in error state, clears the display label.
            delete();
        }
        if(isAnswerState()){ // If in answer state, resets the calculator.
            clearAll();
        }

        try {
            String btnText = getBtnText(event);
            String currentDisplayInput = getDisplay();
            if(isDisplayEmpty() && btnText.equals(".")){ // Checks if display is empty and if '.' is pressed, inputs the value as "0.".
                currentDisplayInput = "0.";
            } else if(currentDisplayInput.equals("0") && btnText.equals("0")) { // Empty if clause to prevent more than one 0 being input.
                //Do Nothing
            } else {
                if(currentDisplayInput.equals("0") && !btnText.equals(".")){ // Check for logic with 0 being replaced by inputted number.
                    currentDisplayInput = btnText;
                } else {
                    currentDisplayInput = currentDisplayInput + btnText;
                }
            }
            OptionalDouble operandValue = OptionalDouble.of(Double.parseDouble(currentDisplayInput));
            model.setCurrentOperand(operandValue);
            setDisplay(currentDisplayInput);
        } catch (RuntimeException e) { // Catches numeracy errors with more than one decimal place being entered into the current operand.
            System.out.println("Error: You cannot enter more than one decimal place in one operand! \n Error type: " + e);
        }

    }

    /**
     * A method linked to the binary operator buttons on the calculator. When the button is pressed, the calculator processes the current and previous
     * operands based on the inputted operator.
     *
     * @param event The event parameter for the ActionEvent of the pressed button.
     */
    @FXML
    public void processBinaryOperator(ActionEvent event) {

        //TODO
        if(!isDisplayEmpty() && isCurrentOperandPresent()){ // Check to see an operand has been entered in the display.
            String btnText = getBtnText(event);
            String operandString = operandToString(model.getCurrentOperand());
            if(isPreviousOperandPresent()){ // Check to see what state the calculator is in.
                doComplexCalculation(btnText);
            } else { // If in basic state, then the function performs as normal.
                model.setPreviousOperand(model.getCurrentOperand());
                setFormula(operandString + " " + btnText);
            }
            model.setOperator(btnText);
            delete();
        } else if(isDisplayEmpty() && !isCurrentOperandPresent() && !isFormulaEmpty()){ // Check to see if calculator is in waiting for next operand state.
            String btnText = getBtnText(event); // If in that state, the operator is changed to the selected operator based on selected button.
            String operandString = operandToString(model.getPreviousOperand());
            model.setOperator(btnText);
            setFormula(operandString + " " + btnText); // Formula updated to new operator.
        }

    }

    /**
     * A method linked to the unary operator buttons on the calculator. When the button is pressed, the calculator processes the current operand based
     * on the selected unary operator button.
     *
     * @param event The event parameter for the ActionEvent of the pressed button.
     */
    @FXML
    public void processUnaryOperator(ActionEvent event) {

        //TODO
        if(isCurrentOperandPresent() && model.getCurrentOperand().getAsDouble() != 0.0){ // Check to see if the operand is present, and not 0.
            if(model.getCurrentOperand().getAsDouble() < 0.0 && getBtnText(event).equals("√")){ // Check to see if the operand is a negative number and if the square root button was pressed.
                System.out.println("Invalid Action: Cannot square root a negative number!");
                clearAll();
                setDisplay("Error: Invalid input");
            }else {
                OptionalDouble result = model.unaryOperation(getBtnText(event));
                if (isAnswerState()) {
                    clearAll();
                }
                model.setCurrentOperand(result);
                setDisplay(operandToString(result));
            }
        }
    }

    /**
     * Method linked to the "=" button. The method calculates the answer of the binary operation carried out between the current and previous operands.
     * The calculated answer is then displayed in the display label. With this calculated answer being able to be used in future calculations by being stored
     * in the current operand variable.
     */
    @FXML
    public void doCalculation() {

        //TODO
        if(!isFormulaEmpty() && isPreviousOperandPresent() && isCurrentOperandPresent() && !isErrorState()){ // Check to ensure the calculator is in the correct state.
            if(operandToString(model.getCurrentOperand()).equals("0") && model.getOperator().equals("÷")){ // Check to see if the calculation is dividing a number by 0.
                delete();
                setDisplay("Error: Invalid input"); // If number is divided by 0, the calculator is set to Error state, and the display updated to display this.
            } else {
                String currentFormula = getFormula();
                currentFormula = currentFormula + " " + negateFormulaText(operandToString(model.getCurrentOperand())) + " = ";
                setFormula(currentFormula);
                OptionalDouble result = model.binaryOperation();
                setDisplay(operandToString(result));
                model.reset();
                model.setCurrentOperand(result); // Calculated answer is stored in the current operand variable.
            }
        }
    }

    ///////////////////////////////////////////////////
    // Define your own methods after this line please
    ///////////////////////////////////////////////////

    /**
     * Method implemented to get the String value representation of the pressed button's text property.
     *
     * @param event The button press event.
     * @return The string value of the pressed button's text property.
     */
    private String getBtnText(ActionEvent event){
        return ((Button)event.getSource()).getText();
    }

    /**
     * Method to perform binary calculation when the calculator has a previous operand in the formula label, and a current operand in the display label,
     * and a binary operator button is pressed. Calculates the result and carries it forward into the previous operand to be used for further calculation.
     *
     * @param operator Takes in the action event's operator. This is the text of the pressed operator button.
     */
    private void doComplexCalculation(String operator){
        OptionalDouble result = model.binaryOperation();
        setFormula(operandToString(result) + " " + operator);
        model.setPreviousOperand(result);
    }

    /**
     * Method used to convert the passed in OptionalDouble value, into a string representation. This is used to convert the operand values,
     * so that they can be used in the calculators string based labels. The method also removes trailing 0s and decimal places.
     *
     * @param operand The optional double value to be converted into a string.
     * @return The string representation of the operand parameter passed in.
     */
    private String operandToString(OptionalDouble operand) {
        String operandString = "";
        if (operand.isPresent()) {
            operandString = String.valueOf(operand.getAsDouble());
            int length = operandString.length();
            if (length > 0 && operandString.charAt(length - 1) == '0' && operandString.charAt(length - 2) == '.') {
                operandString = operandString.substring(0, length - 2);
            }
        }
        return operandString;
    }

    /**
     * This method is used to wrap the inputted operand in brackets when the operand is a negative value.
     * This is done to aid readability of negative numbers.
     *
     * @param operandText The operand in string format.
     * @return The negative operand value, wrapped in brackets.
     */
    private String negateFormulaText(String operandText){
        if(operandText.contains("-")){
            operandText = "(" + operandText + ")";
            return operandText;
        }
        return operandText;
    }

    /**
     * Method used to check if the PreviousOperand variable contains a value.
     *
     * @return True if previous operand is present, otherwise false.
     */
    private boolean isPreviousOperandPresent(){
        return model.getPreviousOperand().isPresent();
    }

    /**
     * Method used to check if the CurrentOperand variable contains a value.
     *
     * @return True if current operand is present, otherwise false.
     */
    private boolean isCurrentOperandPresent(){
        return model.getCurrentOperand().isPresent();
    }

    /**
     * Method used to check if the FormulaLbl is empty.
     *
     * @return True if formula label is empty, otherwise false.
     */
    private boolean isFormulaEmpty(){
        return getFormula().length() == 0;
    }

    /**
     * Method used to check if the calculator is in the answer state. This is done by checking the formula bar to see if it contains
     * the "=" character.
     *
     * @return True if formula label contains "=", false if it does not contain.
     */
    private boolean isAnswerState(){
        return getFormula().contains("=");
    }

    /**
     * This method is used to check if the display label is empty.
     *
     * @return True if the display label is empty, false if the display label is not empty.
     */
    private boolean isDisplayEmpty(){
        return getDisplay().length() == 0;
    }

    /**
     * This method is used to check if the calculator is in the error state.
     *
     * @return True if the display label equals "Error: Invalid input", else returns false.
     */
    private boolean isErrorState(){
        return getDisplay().equals("Error: Invalid input");
    }

}