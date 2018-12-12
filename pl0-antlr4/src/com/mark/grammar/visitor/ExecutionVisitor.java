package com.mark.grammar.visitor;

import com.mark.grammar.*;

import java.util.Objects;
import java.util.Scanner;

public final class ExecutionVisitor extends PLParserBaseVisitor<Void> {
    // TODO Make IO generic, possibly write to file.
    /** SymbolTable accessible to visitor. */
    private final SymbolTable symTable;
    private ExpressionVisitor exprVisitor;
    private Scanner scanner;
    public ExecutionVisitor(final SymbolTable symbolTable) {
        this.symTable = symbolTable;
        this.exprVisitor = new ExpressionVisitor(symbolTable);
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    @Override
    public Void visitProgram(PLParser.ProgramContext ctx) {
        return visitBlock(ctx.block());
    }

    @Override
    public Void visitBlock(PLParser.BlockContext ctx) {
        symTable.pushLocalScope();
        if (Objects.nonNull(ctx.consts())) {
            visitConsts(ctx.consts());
        }
        if (Objects.nonNull(ctx.vars())) {
            visitVars(ctx.vars());
        }
        if (Objects.nonNull(ctx.procedure())) {
            ctx.procedure().forEach(this::visitProcedure);
        }
        visitStmt(ctx.statement());
        symTable.popLocalScope();
        return null;
    }

    @Override
    public Void visitConsts(PLParser.ConstsContext ctx) {
        for (int i = 0; i < ctx.ID().size(); ++i) {
            Symbol variable = new Symbol.VariableSymbol(ctx.ID(i).getText(), true);
            Value.IntValue val = Value.IntValue.valueOf(ctx.NUMBER(i).getText());
            symTable.defineSymbol(variable);
            symTable.assignValue(variable.getSymbolName(), val);
        }
        return null;
    }

    @Override
    public Void visitVars(PLParser.VarsContext ctx) {
        for (int i = 0; i < ctx.ID().size(); ++i) {
            Symbol variable = new Symbol.VariableSymbol(ctx.ID(i).getText(), false);
            symTable.defineSymbol(variable);
        }
        return null;
    }

    @Override
    public Void visitProcedure(PLParser.ProcedureContext ctx) {
        Symbol.MethodSymbol methodSymbol = new Symbol.MethodSymbol(ctx.ID().getText());
        symTable.defineSymbol(methodSymbol);
        symTable.assignValue(methodSymbol.getSymbolName(), new Value.MethodValue(ctx.block()));
        return null;
    }

    private void visitStmt(PLParser.StatementContext ctx) {
        if (ctx instanceof PLParser.AssignmentStmtContext) {
            visitAssignmentStmt((PLParser.AssignmentStmtContext) ctx);
        } else if (ctx instanceof PLParser.CallStmtContext) {
            visitCallStmt((PLParser.CallStmtContext) ctx);
        } else if (ctx instanceof PLParser.QuestionStmtContext) {
            visitQuestionStmt((PLParser.QuestionStmtContext) ctx);
        } else if (ctx instanceof PLParser.WriteStmtContext) {
            visitWriteStmt((PLParser.WriteStmtContext) ctx);
        } else if (ctx instanceof PLParser.BeginStmtContext) {
            visitBeginStmt((PLParser.BeginStmtContext) ctx);
        } else if (ctx instanceof PLParser.IfStmtContext) {
            visitIfStmt((PLParser.IfStmtContext) ctx);
        } else if (ctx instanceof PLParser.WhileStmtContext) {
            visitWhileStmt((PLParser.WhileStmtContext) ctx);
        }
    }

    @Override
    public Void visitAssignmentStmt(PLParser.AssignmentStmtContext ctx) {
        this.symTable.assignValue(ctx.ID().getText(), exprVisitor.visitExpression(ctx.expression()));
        return null;
    }

    @Override
    public Void visitCallStmt(PLParser.CallStmtContext ctx) {
        Value methodCtx = this.symTable.getValue(ctx.ID().getText());
        if (methodCtx instanceof  Value.MethodValue) {
            visitBlock((PLParser.BlockContext) ((Value.MethodValue) methodCtx).getCtx());
        }
        return null;
    }

    @Override
    public Void visitQuestionStmt(PLParser.QuestionStmtContext ctx) {
        if (scanner.hasNextInt()) {
            this.symTable.assignValue(ctx.ID().getText(), Value.IntValue.valueOf(scanner.nextInt()));
        } else {
            // TODO Review error messages.
            throw new RuntimeException("input not given");
        }
        return null;
    }

    @Override
    public Void visitWriteStmt(PLParser.WriteStmtContext ctx) {
        // TODO Is System.out.println the right thing to do?
        System.out.println(exprVisitor.visitExpression(ctx.expression()).getDisplayValue());
        return null;
    }

    @Override
    public Void visitBeginStmt(PLParser.BeginStmtContext ctx) {
        ctx.statement().forEach(this::visitStmt);
        return null;
    }

    @Override
    public Void visitIfStmt(PLParser.IfStmtContext ctx) {
        if (exprVisitor.visitCondition(ctx.condition()).getValue()) {
            visitStmt(ctx.statement());
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(PLParser.WhileStmtContext ctx) {
        int limiter = 0;
        while (exprVisitor.visitCondition(ctx.condition()).getValue() && limiter < Entities.PL_LOOP_LIMIT) {
            limiter++;
            visitStmt(ctx.statement());
        }
        return null;
    }
}
