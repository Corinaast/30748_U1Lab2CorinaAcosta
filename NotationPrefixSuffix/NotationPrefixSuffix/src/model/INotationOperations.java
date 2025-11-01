package model;

import java.util.Map;

public interface INotationOperations {

    String convertToPostfix(String infixExpression);

    String convertToPrefix(String infixExpression);

    String evaluatePostfix(String postfixExpression, Map<Character, Double> variableValues);
}