package com.mark.grammar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Entities {

    public static final String ADD = "+";
    public static final String SUB = "-";
    public static final String MUL = "*";
    public static final String DIV = "/";
    public static final String EQUAL = "=";
    public static final String NOT_EQUAL = "#";
    public static final String LESS_THAN = "<";
    public static final String LESS_THAN_EQUAL = "<=";
    public static final String GREATOR_THAN = ">";
    public static final String GREATOR_THAN_EQUAL = ">=";
    public static final Entities.Type NUMBER = new Entities.Type("number");
    public static final Entities.Type BOOLEAN = new Entities.Type("boolean");
    public static final int PL_LOOP_LIMIT = 10;

    private Entities() {

    }

    public static final class Scope {

        private Map<String, Symbol> symbols;
        private Scope encScope;

        public Scope(final Scope enclosingScope) {
            symbols = new HashMap<>();
            encScope = enclosingScope;
        }

        public Scope getEnclosingScope() {
            return this.encScope;
        }

        void define(final Symbol symbol) {
            if (Objects.nonNull(this.symbols.get(symbol.getSymbolName()))) {
                throw new PLException("symbol already defined in scope");
            }
            this.symbols.put(symbol.getSymbolName(), symbol);
        }

        Symbol resolve(final String symbolName) {
            Symbol symbol = this.symbols.get(symbolName);
            if (Objects.isNull(symbol) && Objects.nonNull(this.encScope)) {
                symbol = this.encScope.resolve(symbolName);
            }
            return symbol;
        }

        @Override
        public int hashCode() {
            if (Objects.nonNull(encScope)) {
                return (symbols.hashCode() * 31) + encScope.hashCode();
            }
            return symbols.hashCode() * 31;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Scope) {
                if (Objects.nonNull(encScope)) {
                    return symbols.equals(((Scope) o).symbols) && encScope.equals(((Scope) o).encScope);
                } else {
                    return symbols.equals(((Scope) o).symbols);
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "{Symbols: " + symbols + "}, EncScope: " + encScope;
        }
    }

    public static final class Type {

        private final String name;

        private Type(final String typeName) {
            this.name = typeName;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Type) {
                return ((Type) o).getName().equals(name);
            }
            return false;
        }
    }

    public static Value.IntValue addValue(Value.IntValue a, Value.IntValue b) {
        return Value.IntValue.valueOf(a.getValue() + b.getValue());
    }

    public static Value.IntValue subValue(Value.IntValue a, Value.IntValue b) {
        return Value.IntValue.valueOf(a.getValue() - b.getValue());
    }

    public static Value.IntValue mulValue(Value.IntValue a, Value.IntValue b) {
        return Value.IntValue.valueOf(a.getValue() * b.getValue());
    }

    public static Value.IntValue divValue(Value.IntValue a, Value.IntValue b) {
        if (b.getValue().equals(0)) {
            throw new PLException("Cannot divide by zero");
        }
        return Value.IntValue.valueOf(a.getValue() / b.getValue());
    }

}
