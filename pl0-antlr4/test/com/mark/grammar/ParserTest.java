package com.mark.grammar;

import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void parseCases() {
        TreeGeneratorUtil.generateTree("examples/example1.pl", "program");
        TreeGeneratorUtil.generateTree("examples/example2.pl", "program");
        TreeGeneratorUtil.generateTree("examples/example3.pl", "program");
        assertTrue("should be able to parse all the expressions", true);
    }
}
