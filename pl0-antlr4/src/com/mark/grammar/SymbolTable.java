package com.mark.grammar;

import java.util.Map;
import java.util.Objects;

/**
 * This class contains the scope and the values mapped.
 */
public class SymbolTable {

    /** Scope of the execution unit. */
    private Entities.Scope scope;

    /** Map of symbolName to Value. */
    private Map<String, Value> valueMap;

    public SymbolTable(final Entities.Scope programScope, final Map<String, Value> programValues) {
        this.scope = programScope;
        this.valueMap = programValues;
    }

    public void defineSymbol(Symbol symbol) {
        scope.define(symbol);
    }

    public Symbol resolveSymbol(String symbolName) {
        return scope.resolve(symbolName);
    }

    public void pushLocalScope() {
        scope = new Entities.Scope(scope);
    }

    public void popLocalScope() {
        scope = Objects.requireNonNull(scope.getEnclosingScope());
    }

    public Value getValue(final String symbolName) {
        if (Objects.nonNull(scope.resolve(symbolName))) {
            return valueMap.get(symbolName);
        }
        throw new PLException("variable not found : " + symbolName);
    }

    public void assignValue(final String symbolName, final Value value) {
        Symbol symbol = scope.resolve(symbolName);
        if (Objects.isNull(scope.resolve(symbolName))) {
            throw new PLException("undefined symbol " + symbolName);
        } else if (symbol.isConstant() && Objects.nonNull(valueMap.get(symbolName))) {
            throw new PLException("const symbol " + symbolName + " cannot be assigned");
        }
        valueMap.put(symbolName, value);
    }

}
