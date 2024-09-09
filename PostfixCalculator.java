import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PostfixCalculator {
    /**
    * This program evaluates a postfix expression using a stack and returns the result
    * Each digit is treated as a single operand, and the operators act on the most recent two operands
    * 
    * @param postfixExpression The expression in postfix notation to evaluate
    * @return The result of the evaluated postfix expression, or null if the expression is invalid
    */
    public Integer evaluatePostfix(String postfixExpression) {
        Stack<Integer> stack = new Stack<>();

        // Loop through each character in the postfix expression
        for (int i = 0; i < postfixExpression.length(); i++) {
            char currentChar = postfixExpression.charAt(i);

            // If it's a digit, convert it to an integer and push it onto the stack
            if (Character.isDigit(currentChar)) {
                int number = currentChar - '0';  // Convert char to int
                stack.push(number);  // Push the single-digit number onto the stack
            } else {
                // If the character is an operator, check if there are at least two operands
                if (stack.size() < 2) {  // Not enough operands for the operation
                    System.out.println("Error: Invalid postfix expression (not enough operands)");
                    return null;  // Return null to indicate an invalid expression
                }
                
                // Pop the top two operands from the stack
                int operand2 = stack.pop();
                int operand1 = stack.pop();

                // Perform the operation based on the operator
                switch (currentChar) {
                    case '+':
                        stack.push(operand1 + operand2); // Add the two operands
                        break;
                    case '-':
                        stack.push(operand1 - operand2); // Subtract operand2 from operand 1
                        break;
                    case '*':
                        stack.push(operand1 * operand2); // Multiply the two operands
                        break;
                    case '/':
                        if (operand2 == 0) { // Prevent division by 0
                            System.out.println("Error: Division by zero");
                            return null;
                        }
                        stack.push(operand1 / operand2); // Divide operand1 by operand2
                        break;
                    case '%':
                        if (operand2 == 0) { // Prevent modulo by 0
                            System.out.println("Error: Division by zero");
                            return null;
                        }
                        stack.push(operand1 % operand2); // Modulo operation (Give the remainder)
                        break;
                    default:
                        System.out.println("Error: Unknown operator '" + currentChar + "'");
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

    /** 
    * Reads postfix expressions from a file and evaluates each expression
    * This function reas expressions from a given file, line by line, and evaluates each using the evaluatePostfix method
    * 
    * @param filename The name of the file containing postfix expressions
    */
    public void evaluateFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String expression;
            // Read each line from the file
            while ((expression = br.readLine()) != null) {
                // Evaluate each postfix expression and print the result
                Integer result = evaluatePostfix(expression); 
                if (result != null) {  
                    System.out.println("Result: " + result);
                } else {
                    System.out.println("Evaluation failed for expression: " + expression);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to read file.");
        }
    }

    public static void main(String[] args) {
        PostfixCalculator calculator = new PostfixCalculator();

        // Test with valid and invalid expressions
        String expression1 = "23*4-";  // (2 * 3) - 4 = 2
        System.out.println("Result 1: " + calculator.evaluatePostfix(expression1));

        String expression2 = "55+9*";  // (5 + 5) * 9 = 90
        System.out.println("Result 2: " + calculator.evaluatePostfix(expression2));
        
        String expression3 = "93/8+"; // (9 / 3) + 8 = 11
        System.out.println("Result 3: " + calculator.evaluatePostfix(expression3));
        
        String expression4 = "92%4+"; // (9 % 2) + 4 = 5
        System.out.println("Result 4: " + calculator.evaluatePostfix(expression4));

        String expression5 = "82/-";  // Invalid expression (not enough operands for '-')
        System.out.println("Result 5: " + calculator.evaluatePostfix(expression5));
        
        String expression6 = "30/8+"; // Test dividing by 0 error
        System.out.println("Result 6: " + calculator.evaluatePostfix(expression6));

        // Test reading postfix expressions from a file (the file should contain one expression per line)
        calculator.evaluateFromFile("expressions.txt");
    }
}
