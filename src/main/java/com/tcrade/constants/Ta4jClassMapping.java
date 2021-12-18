package com.tcrade.constants;

import java.util.HashMap;
import java.util.Map;

public class Ta4jClassMapping {

    public static final Map<String, String> RULE_CLASS_MAPPING = new HashMap<>();
    public static final Map<String, String> INDICATOR_CLASS_MAPPING = new HashMap<>();

    private static final String RULES_BASE_PATH = "org.ta4j.core.rules.";
    private static final String INDICATORS_BASE_PATH = "org.ta4j.core.indicator.";

    static {
        RULE_CLASS_MAPPING.put("AbstractRule", RULES_BASE_PATH + "AbstractRule");
        RULE_CLASS_MAPPING.put("AndRule", RULES_BASE_PATH + "AndRule");
        RULE_CLASS_MAPPING.put("BooleanIndicatorRule", RULES_BASE_PATH + "BooleanIndicatorRule");
        RULE_CLASS_MAPPING.put("BooleanRule", RULES_BASE_PATH + "BooleanRule");
        RULE_CLASS_MAPPING.put("ChainRule", RULES_BASE_PATH + "ChainRule");
        RULE_CLASS_MAPPING.put("CrossedDownIndicatorRule", RULES_BASE_PATH + "CrossedDownIndicatorRule");
        RULE_CLASS_MAPPING.put("CrossedUpIndicatorRule", RULES_BASE_PATH + "CrossedUpIndicatorRule");
        RULE_CLASS_MAPPING.put("DayOfWeekRule", RULES_BASE_PATH + "DayOfWeekRule");
        RULE_CLASS_MAPPING.put("FixedRule", RULES_BASE_PATH + "FixedRule");
        RULE_CLASS_MAPPING.put("InPipeRule", RULES_BASE_PATH + "InPipeRule");
        RULE_CLASS_MAPPING.put("InSlopeRule", RULES_BASE_PATH + "InSlopeRule");
        RULE_CLASS_MAPPING.put("IsEqualRule", RULES_BASE_PATH + "IsEqualRule");
        RULE_CLASS_MAPPING.put("IsFallingRule", RULES_BASE_PATH + "IsFallingRule");
        RULE_CLASS_MAPPING.put("IsHighestRule", RULES_BASE_PATH + "IsHighestRule");
        RULE_CLASS_MAPPING.put("IsLowestRule", RULES_BASE_PATH + "IsLowestRule");
        RULE_CLASS_MAPPING.put("IsRisingRule", RULES_BASE_PATH + "IsRisingRule");
        RULE_CLASS_MAPPING.put("JustOnceRule", RULES_BASE_PATH + "JustOnceRule");
        RULE_CLASS_MAPPING.put("NotRule", RULES_BASE_PATH + "NotRule");
        RULE_CLASS_MAPPING.put("OpenedPositionMinimumBarCountRule", RULES_BASE_PATH + "OpenedPositionMinimumBarCountRule");
        RULE_CLASS_MAPPING.put("OrRule", RULES_BASE_PATH + "OrRule");
        RULE_CLASS_MAPPING.put("OverIndicatorRule", RULES_BASE_PATH + "OverIndicatorRule");
        RULE_CLASS_MAPPING.put("StopGainRule", RULES_BASE_PATH + "StopGainRule");
        RULE_CLASS_MAPPING.put("StopLossRule", RULES_BASE_PATH + "StopLossRule");
        RULE_CLASS_MAPPING.put("TimeRangeRule", RULES_BASE_PATH + "TimeRangeRule");
        RULE_CLASS_MAPPING.put("TrailingStopLossRule", RULES_BASE_PATH + "TrailingStopLossRule");
        RULE_CLASS_MAPPING.put("UnderIndicatorRule", RULES_BASE_PATH + "UnderIndicatorRule");
        RULE_CLASS_MAPPING.put("WaitForRule", RULES_BASE_PATH + "WaitForRule");
        RULE_CLASS_MAPPING.put("XorRule", RULES_BASE_PATH + "XorRule");

        INDICATOR_CLASS_MAPPING.put("", INDICATORS_BASE_PATH + "");
    }
}
