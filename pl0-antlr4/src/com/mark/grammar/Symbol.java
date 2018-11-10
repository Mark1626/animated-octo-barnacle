package com.mark.grammar;

public abstract class Symbol {
    private final String name;
    private final boolean isConst;

    public Symbol(final String symbolName, final boolean isConstant) {
        this(symbolName, null, isConstant);
    }

    public Symbol(final String symbolName, final Entities.Type symbolType, final boolean isConstant) {
        this.name = symbolName;
        this.isConst = isConstant;
    }

    public boolean isConstant() {
        return isConst;
    }

    public String getSymbolName() {
        return this.name;
    }

    public static class VariableSymbol extends Symbol {

        public VariableSymbol(final String variableName, final boolean isConstant) {
            // All variables are integers in PL/0
            super(variableName, isConstant);
        }
    }

    public static class MethodSymbol extends Symbol {
        public MethodSymbol(final String methodName) {
            super(methodName, true);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        stringBuilder.append("name: ");
        stringBuilder.append(name);
        stringBuilder.append(",");
        stringBuilder.append("isConst: ");
        stringBuilder.append(isConst);
        stringBuilder.append(">");
        return stringBuilder.toString();
    }
}
