package com.mark.grammar;

import com.mark.grammar.visitor.ExecutionVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;
import java.util.Map;

public class Interpretor {

    private Interpretor() {

    }

    public static void main(final String[] args) {
		ParserRuleContext parseTree = TreeGeneratorUtil.generateTree(
		        "examples/example3.pl", "program");
        Map<String, Value> values = new HashMap<String, Value>();
		SymbolTable symbolTable = new SymbolTable(new Entities.Scope(null), values);
        ExecutionVisitor visitor = new ExecutionVisitor(symbolTable);
        visitor.visitProgram((PLParser.ProgramContext) parseTree);
    }

}
