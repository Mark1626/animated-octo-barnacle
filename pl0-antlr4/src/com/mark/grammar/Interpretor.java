package com.mark.grammar;

import com.mark.grammar.visitor.ExecutionVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;
import java.util.Map;

public class Interpretor {

    private Interpretor() {

    }

    public static void main(final String[] args) {
        if(args.length == 0) {
            throw new PLException("Please Specify the filename");
        } else if (args.length > 1) {
            throw new PLException("Too many arguments specified");
        }
        String fileName = args[0];
		ParserRuleContext parseTree = TreeGeneratorUtil.generateTree(fileName, "program");
        Map<String, Value> values = new HashMap<String, Value>();
		SymbolTable symbolTable = new SymbolTable(new Entities.Scope(null), values);
        ExecutionVisitor visitor = new ExecutionVisitor(symbolTable);
        visitor.visitProgram((PLParser.ProgramContext) parseTree);
    }

}
