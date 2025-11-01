package controller;
import model.INotationOperations;
import java.util.*;
public class NotationProcessor implements INotationOperations {

    @Override
    public String convertToPostfix(String infix) {
        StringBuilder trace = new StringBuilder();
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        String processedInfix = preprocessInfix(infix);
        
        trace.append("ORIGINAL INFIX EXPRESSION: ").append(infix).append("\n");
        trace.append("PRE-PROCESSED EXPRESSION: ").append(processedInfix).append("\n\n");
        
        trace.append("═══════════════════════════════════════════════════════════\n");
        trace.append("           CONVERSION TO POSTFIX NOTATION\n");
        trace.append("═══════════════════════════════════════════════════════════\n\n");
        
        trace.append("----------------------------------------------------------------\n");
        trace.append(String.format("%-12s | %-25s | %s\n", "Symbol", "Stack (Top → Base)", "Postfix"));
        trace.append("----------------------------------------------------------------\n");
        
        List<String> tokens = tokenizeInfix(processedInfix);
        
        for (String token : tokens) {
            String symbolDisplay = "'" + token + "'";
            String stackDisplay = getStackDisplay(stack);
            String outputDisplay = output.toString().trim();
            
            if (isOperand(token)) {
                output.append(token).append(" ");
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    symbolDisplay, stackDisplay, output.toString().trim()));
            } else if (token.equals("(")) {
                stack.push('(');
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    symbolDisplay, getStackDisplay(stack), outputDisplay));
            } else if (token.equals(")")) {
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    symbolDisplay, stackDisplay, outputDisplay));
                
                while (!stack.isEmpty() && stack.peek() != '(') {
                    char op = stack.pop();
                    output.append(op).append(" ");
                    trace.append(String.format("%-12s | %-25s | %s\n", 
                        "POP '" + op + "'", getStackDisplay(stack), output.toString().trim()));
                }
                
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Unbalanced parentheses: Missing '('");
                }
                stack.pop(); 
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    "POP '('", getStackDisplay(stack), output.toString().trim()));
            } else if (isOperator(token.charAt(0))) {
                char currentOp = token.charAt(0);
                
                // Display the symbol first
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    symbolDisplay, stackDisplay, outputDisplay));
                
                while (!stack.isEmpty() && stack.peek() != '(' && 
                       hasHigherPrecedence(stack.peek(), currentOp)) {
                    char op = stack.pop();
                    output.append(op).append(" ");
                    trace.append(String.format("%-12s | %-25s | %s\n", 
                        "POP '" + op + "'", getStackDisplay(stack), output.toString().trim()));
                }
                
                stack.push(currentOp);
                trace.append(String.format("%-12s | %-25s | %s\n", 
                    "PUSH '" + currentOp + "'", getStackDisplay(stack), output.toString().trim()));
            }
        }
        
        trace.append("----------------------------------------------------------------\n");
        trace.append("END EXPRESSION: Emptying remaining stack\n");
        trace.append("----------------------------------------------------------------\n");
        
        while (!stack.isEmpty()) {
            char op = stack.pop();
            if (op == '(') {
                throw new IllegalArgumentException("Unbalanced parentheses: Missing ')'");
            }
            output.append(op).append(" ");
            trace.append(String.format("%-12s | %-25s | %s\n", 
                "POP '" + op + "'", getStackDisplay(stack), output.toString().trim()));
        }
        
        String result = output.toString().trim();
        
        trace.append("----------------------------------------------------------------\n");
        trace.append("FINAL POSTFIX RESULT: ").append(result).append("\n"); 
        trace.append("═══════════════════════════════════════════════════════════\n");
        
        return trace.toString();
    }

    @Override
    public String evaluatePostfix(String postfix, Map<Character, Double> variableValues) {
        Stack<Double> stack = new Stack<>();
        StringBuilder trace = new StringBuilder();
        
        trace.append("═══════════════════════════════════════════════════════════\n");
        trace.append("             POSTFIX EXPRESSION EVALUATION\n");
        trace.append("═══════════════════════════════════════════════════════════\n\n");
        trace.append("Postfix Expression: ").append(postfix).append("\n");
        
        if (variableValues != null && !variableValues.isEmpty()) {
            trace.append("Variable Values: ").append(variableValues).append("\n");
        }
        trace.append("\n");
        
        trace.append("--------------------------------------------------------------------------------\n");
        trace.append("| TOKEN      | OPERAND A | OPERAND B | OPERATION       | RESULT    | STACK    \n");
        trace.append("--------------------------------------------------------------------------------\n");

        List<String> tokens = Arrays.asList(postfix.trim().split("\\s+"));
        
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            
            String opA = "N/A", opB = "N/A", res = "N/A", opDisplay = "N/A";
            String stackBefore = getNumericStackDisplay(stack);
            
            if (isNumeric(token)) {
                double value = Double.parseDouble(token);
                stack.push(value);
                opA = String.format("%.2f", value);
                res = "PUSH";
            }
            else if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
                char var = token.charAt(0);
                if (variableValues == null || !variableValues.containsKey(var)) {
                    throw new IllegalArgumentException("Variable '" + var + "' has no assigned value.");
                }
                double value = variableValues.get(var);
                stack.push(value);
                opA = String.format("Var %c=%.2f", var, value);
                res = "PUSH";
            }
            else if (token.length() == 1 && isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression: missing operands for '" + token + "'");
                }
                
                double b = stack.pop();
                double a = stack.pop();
                
                opA = String.format("%.2f", a);
                opB = String.format("%.2f", b);
                opDisplay = String.format("%.2f %s %.2f", a, token, b);

                double result = applyOperation(a, b, token.charAt(0));
                stack.push(result);
                res = String.format("%.2f", result);
            } else {
                 throw new IllegalArgumentException("Invalid token in postfix: '" + token + "'");
            }
            
            String stackAfter = getNumericStackDisplay(stack);
            
            trace.append(String.format("| %-10s | %-10s | %-10s | %-15s | %-9s | %s \n", 
                        token, opA, opB, opDisplay, res, stackAfter));
        }
        
        trace.append("--------------------------------------------------------------------------------\n");

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression: inconsistent result. Elements on stack: " + stack.size());
        }
        
        double finalResult = stack.pop();
        trace.append("\nFINAL RESULT: ").append(finalResult).append("\n");
        trace.append("═══════════════════════════════════════════════════════════\n");
        
        return trace.toString();
    }
    @Override
    public String convertToPrefix(String infix) {
        String reversed = reverseExpression(infix);
        String postfix = convertToPostfixForPrefix(reversed);
        
        StringBuilder prefix = new StringBuilder(postfix);
        return prefix.reverse().toString().replace(" ", "");
    }
    private String preprocessInfix(String infix) {
        StringBuilder processed = new StringBuilder();
        String cleanExpr = infix.replaceAll("\\s+", "");
        int n = cleanExpr.length();
        
        for (int i = 0; i < n; i++) {
            char current = cleanExpr.charAt(i);
            
            if (current == '.') {
                boolean prevIsDigit = (i > 0) && Character.isDigit(cleanExpr.charAt(i - 1));
                boolean nextIsDigit = (i + 1 < n) && Character.isDigit(cleanExpr.charAt(i + 1));
                if (!prevIsDigit && !nextIsDigit) {
                    processed.append(' '); 
                } else {
                    processed.append(current); 
                }
            } else {
                processed.append(current);
            }

            if (i + 1 < n) {
                char next = cleanExpr.charAt(i + 1);
                
                if ((Character.isDigit(current) || current == ')') && next == '(') {
                    processed.append('*');
                }
                else if (Character.isLetter(current) && next == '(') {
                    processed.append('*');
                }
            }
        }
        return processed.toString();
    }
    
    private String getStackDisplay(Stack<Character> stack) {
        if (stack.isEmpty()) {
            return "Empty";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Character> elements = new ArrayList<>();
        
        for (Character c : stack) {
            elements.add(c);
        }
        Collections.reverse(elements); // Reverse to show top first
        
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(elements.get(i));
        }
        
        return sb.toString();
    }
   
    private String getNumericStackDisplay(Stack<Double> stack) {
        if (stack.isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < stack.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.2f", stack.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
    
    private List<String> tokenizeInfix(String expression) {
        List<String> tokens = new ArrayList<>();
        
        String spacedExpression = expression.replaceAll("([+\\-*/^\\(\\)])", " $1 ").replaceAll("\\s+", " ").trim();

        String[] parts = spacedExpression.split(" ");
        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part);
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("-") && i + 1 < tokens.size()) {
                String prev = (i == 0) ? "" : tokens.get(i - 1);
                String next = tokens.get(i + 1);

                if (i == 0 || prev.equals("(") || isOperator(prev.charAt(0))) {
                    if (isNumeric(next) || Character.isLetter(next.charAt(0))) {
                        tokens.set(i, token + next);
                        tokens.remove(i + 1);
                        i--; 
                    }
                }
            }
        }
        
        return tokens;
    }

    private boolean isOperand(String token) {
        return isNumeric(token) || (token.length() == 1 && Character.isLetter(token.charAt(0)));
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    private boolean hasHigherPrecedence(char op1, char op2) {
        int prec1 = precedence(op1);
        int prec2 = precedence(op2);
        
        if (prec1 == prec2) {
            return op2 != '^'; 
        }
        return prec1 > prec2;
    }

    private double applyOperation(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    private String reverseExpression(String expr) {
        List<String> tokens = tokenizeInfix(expr);
        
        StringBuilder reversed = new StringBuilder();
        
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);
            if (token.equals("(")) {
                reversed.append(") "); 
            } else if (token.equals(")")) {
                reversed.append("( "); 
            } else {
                reversed.append(token).append(" ");
            }
        }
        return reversed.toString().trim();
    }

    private String convertToPostfixForPrefix(String reversedInfix) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        List<String> tokens = tokenizeInfix(reversedInfix);
        
        for (String token : tokens) {
            if (isOperand(token)) {
                output.append(token).append(" ");
            } else if (token.equals("(")) {
                stack.push('(');
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if (token.length() == 1 && isOperator(token.charAt(0))) {
                char currentOp = token.charAt(0);
                while (!stack.isEmpty() && stack.peek() != '(' && 
                       hasHigherPrecedence(stack.peek(), currentOp)) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(currentOp);
            }
        }
        
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }
        
        return output.toString().trim();
    }
}