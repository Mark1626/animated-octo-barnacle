package com.mark.grammar;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.dfa.DFA;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TreeGeneratorUtil {

    private TreeGeneratorUtil() {
    }

    public static ParserRuleContext generateTree(final String fileName, final String rule) {
        ParserRuleContext parseTree = null;
        try {
            CharStream stream = CharStreams.fromFileName(fileName);

            PLLexer lexer = new PLLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PLParser parser = new PLParser(tokens);

            resetDFA(parser);
            return parseRule(parser, rule);

            //System.out.println(parseTree.toStringTree(parser));
            // parser.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseTree;
    }

    private static ParserRuleContext parseRule(PLParser parser, String rule)
    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());

        Method ruleMethod = PLParser.class.getMethod(rule);
        try {
            return (ParserRuleContext) ruleMethod.invoke(parser);
        } catch (Exception e) {
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            parser.setErrorHandler(new DefaultErrorStrategy());
            return (ParserRuleContext) ruleMethod.invoke(parser);
        }
    }

    private static void resetDFA(PLParser parser) {

        PredictionContextCache cache = new PredictionContextCache();
        // Define new/clean DFA array
        DFA[] decisionToDFA = new DFA[parser.getATN().
                getNumberOfDecisions()];
        for (int i = 0; i < parser.getATN().getNumberOfDecisions(); i++) {
            decisionToDFA[i] = new DFA(parser.getATN().getDecisionState(i), i);
        }

        parser.setInterpreter(new ParserATNSimulator(parser,
                                                     parser.getATN(),
                                                     decisionToDFA, cache));
    }
}
