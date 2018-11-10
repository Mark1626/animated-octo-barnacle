package com.mark.grammar;

import org.antlr.v4.runtime.ParserRuleContext;

public interface Value {

    final class IntValue implements Value {
        private final Integer val;

        public static IntValue valueOf(final String value) {
            return new IntValue(value);
        }

        public static IntValue valueOf(final Integer value) {
            return new IntValue(value);
        }

        public IntValue(final String strVal) {
            this(Integer.valueOf(strVal));
        }

        private IntValue(final Integer value) {
            this.val = value;
        }

        public Integer getValue() {
            return this.val;
        }

        public boolean odd() {
            return this.val % 2 == 0;
        }

        public IntValue deepCopy() {
            return new IntValue(val);
        }

        public Entities.Type getType() {
            return Entities.NUMBER;
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof IntValue) {
                return ((IntValue) o).getValue().equals(val);
            }
            return false;
        }

        @Override
        public String toString() {
            return "NumberValue<" + this.val + ">";
        }
    }

    final class BoolValue implements Value {

        private static final BoolValue TRUE = new BoolValue(Boolean.TRUE);
        private static final BoolValue FALSE = new BoolValue(Boolean.FALSE);

        private Boolean val;

        private BoolValue(final Boolean b) {
            this.val = b;
        }

        public static BoolValue valueOf(final Boolean value) {
            if (value.equals(Boolean.TRUE)) {
                return BoolValue.TRUE;
            } else {
                return BoolValue.FALSE;
            }
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof BoolValue) {
                return ((BoolValue) o).getValue().equals(val);
            }
            return false;
        }

        public Boolean getValue() {
            return this.val;
        }
    }

    class MethodValue implements Value {
        private final ParserRuleContext ctx;

        public ParserRuleContext getCtx() {
            return ctx;
        }

        public MethodValue(final ParserRuleContext method) {
            ctx = method;
        }
    }
}

