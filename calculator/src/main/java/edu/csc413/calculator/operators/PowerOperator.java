package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;
import java.lang.Math;
public class PowerOperator extends Operator {
    @Override
    public int priority() {
        return 3;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        double o1 =  operandOne.getValue();
        double o2 =  operandTwo.getValue();
        int result = (int) Math.pow(o1,o2);
        return new Operand(result);
    }
}
