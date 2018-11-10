package com.mark.grammar.visitor;


import com.mark.grammar.*;

import java.util.Objects;

public final class ExpressionVisitor extends PLParserBaseVisitor<Value> {

    /** SymbolTable accessible to visitor. */
    private final SymbolTable symTable;

    public ExpressionVisitor(final SymbolTable symbolTable) {
        this.symTable = symbolTable;
    }

    @Override
    public Value.BoolValue visitCondition(PLParser.ConditionContext ctx) {
        if (Objects.nonNull(ctx.ODD())) { // Off Condition
            return Value.BoolValue.valueOf(visitExpression(ctx.expression(0)).odd());
        } else {
            Value.IntValue left = visitExpression(ctx.expression(0));
            Value.IntValue right = visitExpression(ctx.expression(1));
            switch (ctx.opr.getText()) {
                case Entities.LESS_THAN:
                    return Value.BoolValue.valueOf(left.getValue() < right.getValue());
                case Entities.GREATOR_THAN:
                    return Value.BoolValue.valueOf(left.getValue() > right.getValue());
                case Entities.LESS_THAN_EQUAL:
                    return Value.BoolValue.valueOf(left.getValue() <= right.getValue());
                case Entities.GREATOR_THAN_EQUAL:
                    return Value.BoolValue.valueOf(left.getValue() <= right.getValue());
                case Entities.NOT_EQUAL:
                    return Value.BoolValue.valueOf(!left.getValue().equals(right.getValue()));
                case Entities.EQUAL:
                    return Value.BoolValue.valueOf(left.getValue().equals(right.getValue()));
                default:
                    throw new PLException("unknown operand");
            }
        }
    }

    @Override
    public Value.IntValue visitExpression(PLParser.ExpressionContext ctx) {
        Value.IntValue result = visitTerm(ctx.term(0));

        if (Objects.nonNull(ctx.unary) && ctx.unary.getText().equals("-")) {
            result =  Value.IntValue.valueOf(-result.getValue());
        }

        for (int i = 0; i < ctx.addOpr().size(); ++i) {
            if (Entities.ADD.equals(ctx.addOpr(i).getText())) {
                result = Entities.addValue(result, visitTerm(ctx.term(i + 1)));
            } else if (Entities.SUB.equals(ctx.addOpr(i).getText())) {
                result = Entities.subValue(result, visitTerm(ctx.term(i + 1)));
            }
        }

        return result;
    }

    @Override
    public Value.IntValue visitTerm(PLParser.TermContext ctx) {

        Value.IntValue result = visitFactor(ctx.factor(0));

        for (int i = 0; i < ctx.mulOpr().size(); ++i) {
            if (Entities.MUL.equals(ctx.mulOpr(i).getText())) {
                result = Entities.mulValue(result, visitFactor(ctx.factor(i + 1)));
            } else if (Entities.DIV.equals(ctx.mulOpr(i).getText())) {
                result = Entities.divValue(result, visitFactor(ctx.factor(i + 1)));
            }
        }

        return result;
    }

    private Value.IntValue visitFactor(PLParser.FactorContext ctx) {
        if (ctx instanceof PLParser.VariableFactorContext) {
            return visitVariableFactor((PLParser.VariableFactorContext) ctx);
        } else if (ctx instanceof PLParser.NumberFactorContext) {
            return visitNumberFactor((PLParser.NumberFactorContext) ctx);
        } else if (ctx instanceof PLParser.NestedFactorContext) {
            return visitNestedFactor((PLParser.NestedFactorContext) ctx);
        }
        throw new PLException("unidentified variable");
    }

    @Override
    public Value.IntValue visitVariableFactor(PLParser.VariableFactorContext ctx) {
        return (Value.IntValue) this.symTable.getValue(ctx.ID().getText());
    }

    @Override
    public Value.IntValue visitNumberFactor(PLParser.NumberFactorContext ctx) {
        return Value.IntValue.valueOf(ctx.NUMBER().getText());
    }

    @Override
    public Value.IntValue visitNestedFactor(PLParser.NestedFactorContext ctx) {
        return visitExpression(ctx.expression());
    }

}
