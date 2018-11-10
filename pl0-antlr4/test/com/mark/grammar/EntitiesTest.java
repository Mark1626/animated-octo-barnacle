package com.mark.grammar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EntitiesTest {

    @Test
    public void entitiesTest() {
        assertEquals("sum of values should be equal",
                Value.IntValue.valueOf(9),
                Entities.addValue(Value.IntValue.valueOf(5),Value.IntValue.valueOf(4))
        );
        assertEquals("diff of vales should be equal",
                Value.IntValue.valueOf(1),
                Entities.subValue(Value.IntValue.valueOf(5),Value.IntValue.valueOf(4))
        );
        assertEquals("mul of values should be equal",
                Value.IntValue.valueOf(20),
                Entities.mulValue(Value.IntValue.valueOf(5), Value.IntValue.valueOf(4))
        );
        assertEquals("div of values should be equal",
                Value.IntValue.valueOf(2),
                Entities.divValue(Value.IntValue.valueOf(10), Value.IntValue.valueOf(5))
        );
        try {
            Entities.divValue(Value.IntValue.valueOf(10), Value.IntValue.valueOf(0));
            fail("should not divide by zero");
        } catch (PLException ple) {
            assertTrue(true);
        }
    }

    @Test
    public void basicScopeTest() {
        Entities.Scope scope = new Entities.Scope(null);

        Symbol symbolA = new Symbol.VariableSymbol("a", false);
        scope.define(symbolA);

        Symbol symbolFound = scope.resolve(symbolA.getSymbolName());
        assertEquals("Symbol should be found in scope", symbolA, symbolFound);
    }

    @Test(expected = PLException.class)
    public void redefineScopeTest() {
        Entities.Scope scope = new Entities.Scope(null);

        Symbol symbolA = new Symbol.VariableSymbol("a", false);
        scope.define(symbolA);

        Symbol symbolB = new Symbol.VariableSymbol("b", false);
        scope.define(symbolB);
        scope.define(symbolA);
    }

    @Test
    public void basicNestedScopeTest() {
        Entities.Scope currentScope = new Entities.Scope(null);

        // Create outmost scope add symbol a
        Symbol symbolA = new Symbol.VariableSymbol("a", false);
        currentScope.define(symbolA);

        // Create local scope with symbols a, b
        currentScope = new Entities.Scope(currentScope);
        Symbol symbolB = new Symbol.VariableSymbol("a", false);
        Symbol symbolC = new Symbol.VariableSymbol("b", false);
        currentScope.define(symbolB);
        currentScope.define(symbolC);

        // Create local scope with symbols b, c
        currentScope = new Entities.Scope(currentScope);
        Symbol symbolD = new Symbol.VariableSymbol("b", false);
        Symbol symbolE = new Symbol.VariableSymbol("c", false);
        currentScope.define(symbolD);
        currentScope.define(symbolE);

        assertEquals("variable b in inner scope 2 should be equal", symbolD, currentScope.resolve("b"));
        assertEquals("variable a in inner scope 2 should be equal", symbolB, currentScope.resolve("a"));
        currentScope = currentScope.getEnclosingScope();
        assertEquals("variable b in inner scope 1 should be equal", symbolC, currentScope.resolve("b"));
        assertEquals("variable a in inner scope 1 should be equal", symbolB, currentScope.resolve("a"));
        currentScope = currentScope.getEnclosingScope();
        assertEquals("variable a in outer scope should be equal", symbolA, currentScope.resolve("a"));
    }

}
