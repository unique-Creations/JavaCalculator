package edu.csc413.calculator.evaluator;


import edu.csc413.calculator.exceptions.InvalidTokenException;
import edu.csc413.calculator.operators.*;

import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {

    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;
    private StringTokenizer expressionTokenizer;
    private final String delimiters = " +/*-^()";

    public Evaluator() {
        operandStack = new Stack<>();
        operatorStack = new Stack<>();
    }

    //evaluate expression using current operator
    private void eval() {
        Operator operatorFromStack = operatorStack.pop();
        Operand operandTwo = operandStack.pop();
        Operand operandOne = operandStack.pop();
        Operand result = operatorFromStack.execute(operandOne, operandTwo);
        operandStack.push(result);
    }

    public int evaluateExpression(String expression) throws InvalidTokenException {
        String expressionToken;

        this.expressionTokenizer = new StringTokenizer(expression, this.delimiters, true);

        while (this.expressionTokenizer.hasMoreTokens()) {
            // filter out spaces
            if (!(expressionToken = this.expressionTokenizer.nextToken()).equals(" ")) {
                // check if token is an operand
                if (Operand.check(expressionToken)) {
                    operandStack.push(new Operand(expressionToken));
                } else {
                    if (!Operator.check(expressionToken)) {
                        throw new InvalidTokenException(expressionToken);
                    } //token is an Operator, no need to check again.
                    //process expression for both operatorStack scenarios, ignoring the scenarios with "()".
                    if (operatorStack.empty() && Operator.getOperator(expressionToken).priority() != -1
                            && Operator.getOperator(expressionToken).priority() != 0) {
                        Operator newOperator = Operator.getOperator(expressionToken);
                        operatorStack.push(newOperator);
                    } else if (!operatorStack.empty() && (operatorStack.peek().priority() < Operator.getOperator(expressionToken).priority())
                            && Operator.getOperator(expressionToken).priority() != -1 && Operator.getOperator(expressionToken).priority() != 0) {

                        Operator newOperator = Operator.getOperator(expressionToken);

                        if (operatorStack.peek().priority() != 0) {
                            while (operatorStack.peek().priority() >= newOperator.priority()) {
                                eval();
                            }
                        }
                        operatorStack.push(newOperator);
                        //process expression inside of a parentheses.
                    } else if (Operator.getOperator(expressionToken).priority() == 0) {
                        Operator newOperator = Operator.getOperator(expressionToken);
                        operatorStack.push(newOperator);
                    } else if (Operator.getOperator(expressionToken).priority() == -1) {
                        if (operatorStack.peek().priority() != 0) {
                            eval();
                            operatorStack.pop(); //Pop "(" once evaluation is complete.
                        }
                    }
                }
            }
        }
        //Process the operator stack until it's empty
        while (!operatorStack.empty()) {
            eval();
        }

        return operandStack.peek().getValue();
    }
}