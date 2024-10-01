package uz.project.filter;

import uz.project.operators.AndOperator;
import uz.project.operators.ConditionNode;
import uz.project.operators.LogicalOperator;
import uz.project.operators.OrOperator;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Parser from filter request to Condtion nodes
public class FilterParser {

    // Regexps for validating conditions inside of brackets and expressions, keywords, operators inside of it
    private static final String CONDITION_PATTERN = "\\(([^)]+)\\)";
    private static final String EXPRESSION_PATTERN = "(\\w+)\\s*(==|!=|~|<=|>=|<|>)\\s*(?:'(.*?)'|prev\\.(\\w+))";

    // Method to return all condtions from request
    public List<LogicalOperator> parseFilterRequest(String filterRequest) {
        List<LogicalOperator> rootConditions = new ArrayList<>();
        Matcher matcher = Pattern.compile(CONDITION_PATTERN).matcher(filterRequest);

        while (matcher.find()) {
            String conditionGroup = matcher.group(1).trim();
            LogicalOperator rootCondition = parseExpression(conditionGroup);
            rootConditions.add(rootCondition);
        }
        return rootConditions;
    }


    private LogicalOperator parseExpression(String expression) {
        return parseOrExpression(expression);
    }

    private LogicalOperator parseOrExpression(String expression) {
        String[] tokens = expression.split(" or ");
        LogicalOperator result = parseAndExpression(tokens[0]);
        for (int i = 1; i < tokens.length; i++) {
            result = new OrOperator(result, parseAndExpression(tokens[i]));
        }
        return result;
    }

    private LogicalOperator parseAndExpression(String expression) {
        String[] tokens = expression.split(" and ");
        LogicalOperator result = parsePrimaryExpression(tokens[0]);
        for (int i = 1; i < tokens.length; i++) {
            result = new AndOperator(result, parsePrimaryExpression(tokens[i]));
        }
        return result;
    }

    private LogicalOperator parsePrimaryExpression(String expression) {
        expression = expression.trim();
        if (expression.startsWith("(") && expression.endsWith(")")) {
            return parseExpression(expression.substring(1, expression.length() - 1));
        } else {
            Matcher matcher = Pattern.compile(EXPRESSION_PATTERN).matcher(expression);
            if (matcher.find()) {
                String field = matcher.group(1).trim(); // Handles field of an expression
                String operator = matcher.group(2).trim();// Handles operators of an expression
                String value;

                if (matcher.group(3) != null) {
                    value = matcher.group(3).trim();  // Handles the quoted value of an expression
                } else if (matcher.group(4) != null) {
                    value = "prev." + matcher.group(4).trim();  // Handles the prev.FIELD syntax of an expression
                } else {
                    throw new IllegalArgumentException("Invalid expression: " + expression);
                }

                return new ConditionNode(new Condition(field, operator, value));
            }
        }
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }
}
