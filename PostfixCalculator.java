import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PostfixCalculator {

    /**
     * Evaluates a postfix expression and returns the result.
     * This function processes a postfix expression using a stack.
     * Both single and multi-digit operands are supported. Multi-digit operands must be space-separated.
     *
     * @param postfixExpression The expression in postfix notation to evaluate.
     * @return The result of the evaluated postfix expression, or null if the expression is invalid.
     */
    public Integer evaluatePostfix(String postfixExpression) {
        Stack<Integer> stack = new Stack<>();

        // Determine if the expression is space-separated
        String[] tokens;
        if (postfixExpression.contains(" ")) {
            // Split by spaces to handle multi-digit operands
            tokens = postfixExpression.split(" ");
        } else {
            // Treat each character as an individual token for non-space-separated expressions
            tokens = postfixExpression.split("");
        }

        // Loop through each token in the postfix expression
        for (String token : tokens) {

            // If the token is a number, push it onto the stack
            if (isNumeric(token)) {
                int number = Integer.parseInt(token);  // Convert string to int
                stack.push(number);  // Push the number onto the stack
            } else {
                // If the token is an operator, check if there are at least two operands
                if (stack.size() < 2) {  // Not enough operands for the operation
                    System.out.println("Error: Invalid postfix expression (not enough operands)");
                    return null;  // Return null to indicate an invalid expression
                }
                
                // Pop the top two operands from the stack
                int operand2 = stack.pop();
                int operand1 = stack.pop();

                // Perform the operation based on the operator
                switch (token) {
                    case "+":
                        stack.push(operand1 + operand2); // Add the two operands
                        break;
                    case "-":
                        stack.push(operand1 - operand2); // Subtract operand2 from operand1
                        break;
                    case "*":
                        stack.push(operand1 * operand2); // Multiply the two operands
                        break;
                    case "/":
                        if (operand2 == 0) { // Prevent division by 0
                            System.out.println("Error: You can't divide by zero");
                            return null;
                        }
                        stack.push(operand1 / operand2); // Divide operand1 by operand2
                        break;
                    case "%":
                        if (operand2 == 0) { // Prevent modulo by 0
                            System.out.println("Error: You can't divide by zero");
                            return null;
                        }
                        stack.push(operand1 % operand2); // Modulo operation (Give the remainder)
                        break;
                    default:
                        System.out.println("Error: Unknown operator '" + token + "'");
                        return null; // Return null for unsupported operators
                }
            }
        }

        // At the end of processing, there should be exactly one operand left in the stack
        if (stack.size() != 1) {
            System.out.println("Error: Invalid postfix expression (incorrect final stack size)");
            return null;  // Invalid if there are more or fewer than 1 item in the stack
        }

        return stack.pop();  // Return the final result
    }

     // Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);  // Try to convert the string to an integer
            return true;
        } catch (NumberFormatException e) {
            return false;  // The string is not numeric
        }
    }

    /** 
     * Reads postfix expressions from a file and evaluates each expression.
     * This function reads expressions from a given file, line by line, and evaluates each using the evaluatePostfix method.
     * 
     * @param filename The name of the file containing postfix expressions.
     */
    public void evaluateFromFile(String filename) {
        System.out.println("These are the expressions from the saved file.");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String expression;
            // Read each line from the file
            while ((expression = br.readLine()) != null) {
                // Evaluate each postfix expression and print the result
                Integer result = evaluatePostfix(expression); 
                if (result != null) {  
                    System.out.println("Result: " + expression + " = " + result + "\n");
                } else {
                    System.out.println("Evaluation failed for expression: " + expression + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to read file.");
        }
    }

    public static void main(String[] args) {
        PostfixCalculator calculator = new PostfixCalculator();

        // Test with valid and invalid expressions
        String expression1 = "23 4 * 5 +";  // (23 * 4) + 5 = 97
        System.out.println("First test: " + expression1 + " = " + calculator.evaluatePostfix(expression1) + "\n");

        String expression2 = "55 9 + 7 *";  // (55 + 9) * 7 = 448
        System.out.println("Second test: " + expression2 + " = " + calculator.evaluatePostfix(expression2) + "\n");
        
        String expression3 = "92 2 % 4 +";  // (92 % 2) + 4 = 4
        System.out.println("Third test: " + expression3 + " = " + calculator.evaluatePostfix(expression3) + "\n");
        
        String expression4 = "93 8 - 9 +";  // 93 8 - 9 + = 94
        System.out.println("Fourth test: " + expression4 + " = " + calculator.evaluatePostfix(expression4) + "\n");
        
        String expression5 = "76 9 % 2 -"; // (76 % 9) - 2 = 2
        System.out.println("Fifth test: " + expression5 + " = " + calculator.evaluatePostfix(expression5) + "\n");

        String expression6 = "8 2 / -";  // Invalid
        System.out.println("Sixth test: " + expression6 + " = " + calculator.evaluatePostfix(expression6) + "\n");
        
        String expression7 = "30 0 / 8 +";  // Division by zero
        System.out.println("Seventh test: " + expression7 + " = " + calculator.evaluatePostfix(expression7) + "\n");
        
        String expression8 = "45 0 % 9 -"; // Modulo by zero
        System.out.println("Eighth test: " + expression8 + " = " + calculator.evaluatePostfix(expression8) + "\n");
        
        // Test reading postfix expressions from a file (the file should contain one expression per line)
        calculator.evaluateFromFile("expressions.txt");
    }
}
