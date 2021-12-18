package com.tcrade.enums;

import org.ta4j.core.Rule;
import org.ta4j.core.rules.BooleanRule;

import java.util.function.BiFunction;

public enum OperatorType {

    AND(BooleanRule.TRUE, Rule::and),
    OR(BooleanRule.FALSE, Rule::or);

    private final BooleanRule defaultRule;
    private final BiFunction<Rule, Rule, Rule> action;

    OperatorType(BooleanRule defaultRule, BiFunction<Rule, Rule, Rule> action) {
        this.defaultRule = defaultRule;
        this.action = action;
    }

    public BooleanRule getDefaultRule() {
        return defaultRule;
    }

    public Rule apply(Rule rule, Rule ruleToBeCombined) {
        return action.apply(rule, ruleToBeCombined);
    }
}