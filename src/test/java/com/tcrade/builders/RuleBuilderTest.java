package com.tcrade.builders;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Rule;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.BooleanRule;
import org.ta4j.core.rules.IsFallingRule;
import org.ta4j.core.rules.OrRule;
import org.ta4j.core.rules.StopGainRule;
import org.ta4j.core.rules.TrailingStopLossRule;

import java.lang.reflect.Field;

import static com.tcrade.enums.JsonElementType.BAR_SERIES;
import static com.tcrade.enums.JsonElementType.BOOLEAN;
import static com.tcrade.enums.JsonElementType.INTEGER;
import static com.tcrade.enums.JsonElementType.NUM;
import static com.tcrade.enums.JsonElementType.NUMBER;
import static com.tcrade.utils.BuilderUtils.formatDefaultElementJson;
import static com.tcrade.utils.BuilderUtils.formatIndicatorJson;
import static com.tcrade.utils.BuilderUtils.formatRule;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.reflect.FieldUtils.getField;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleBuilderTest {

    private RuleBuilder ruleBuilder;

    private BaseBarSeries series;

    @BeforeEach
    public void setUp() {
        series = new BaseBarSeries();
        IndicatorBuilder indicatorBuilder = new IndicatorBuilder(series);
        ruleBuilder = new RuleBuilder(indicatorBuilder);
    }

    @Test
    public void shouldBuildARuleWithIndicatorAndIntegerAndNumConstructorParameters() throws Exception {
        Class<HighPriceIndicator> highPriceIndicatorClass = HighPriceIndicator.class;
        String highPriceIndicatorJson = formatIndicatorJson(highPriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        Num lossPercentage = DoubleNum.valueOf(10);
        String lossPercentageJson = formatDefaultElementJson(NUM, lossPercentage);

        int barCount = 20;
        String barCountJson = formatDefaultElementJson(INTEGER, barCount);

        Class<TrailingStopLossRule> trailingStopLossRuleClass = TrailingStopLossRule.class;
        String ruleElement = formatRule(trailingStopLossRuleClass, asList(highPriceIndicatorJson, lossPercentageJson, barCountJson));

        Rule trailingStopLossRule = ruleBuilder.build(new JSONObject(ruleElement));

        Field highPriceIndicatorField = getField(trailingStopLossRuleClass, "priceIndicator", true);
        Object highPriceIndicator = highPriceIndicatorField.get(trailingStopLossRule);
        assertEquals(highPriceIndicatorClass, highPriceIndicator.getClass());
        assertEquals(series, ((HighPriceIndicator) highPriceIndicator).getBarSeries());

        Field lossPercentageField = getField(trailingStopLossRuleClass, "lossPercentage", true);
        assertEquals(lossPercentage, lossPercentageField.get(trailingStopLossRule));

        Field barCountField = getField(trailingStopLossRuleClass, "barCount", true);
        assertEquals(barCount, barCountField.get(trailingStopLossRule));
    }

    @Test
    public void shouldBuildAnIndicatorWithNumberConstructorParameters() throws Exception {
        Class<ClosePriceIndicator> closePriceIndicatorClass = ClosePriceIndicator.class;
        String closePriceIndicatorJson = formatIndicatorJson(closePriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        Num gainPercentage = DecimalNum.valueOf(20);
        String gainPercentageJson = formatDefaultElementJson(NUMBER, gainPercentage.doubleValue());

        Class<StopGainRule> stopGainRuleClass = StopGainRule.class;
        String ruleElement = formatRule(stopGainRuleClass, asList(closePriceIndicatorJson, gainPercentageJson));

        Rule stopGainRule = ruleBuilder.build(new JSONObject(ruleElement));

        Field closePriceIndicatorField = getField(stopGainRuleClass, "closePrice", true);
        Object closePriceIndicator = closePriceIndicatorField.get(stopGainRule);
        assertEquals(closePriceIndicatorClass, closePriceIndicator.getClass());
        assertEquals(series, ((ClosePriceIndicator) closePriceIndicator).getBarSeries());

        Field lossPercentageField = getField(stopGainRuleClass, "gainPercentage", true);
        assertEquals(gainPercentage, lossPercentageField.get(stopGainRule));
    }

    @Test
    public void shouldBuildAnIndicatorWithRuleConstructorParameters() throws Exception {
        Class<HighPriceIndicator> highPriceIndicatorClass = HighPriceIndicator.class;
        String highPriceIndicatorJson = formatIndicatorJson(highPriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        int barCount = 20;
        String barCountJson = formatDefaultElementJson(INTEGER, barCount);

        Class<IsFallingRule> isFallingRuleClass = IsFallingRule.class;
        String isFallingRuleJson = formatRule(isFallingRuleClass, asList(highPriceIndicatorJson, barCountJson));

        Class<BooleanRule> booleanRuleClass = BooleanRule.class;
        String booleanRuleJson = formatRule(booleanRuleClass, singletonList(formatDefaultElementJson(BOOLEAN, true)));

        Class<OrRule> orRuleClass = OrRule.class;
        String ruleElement = formatRule(orRuleClass, asList(booleanRuleJson, isFallingRuleJson));

        Rule orRule = ruleBuilder.build(new JSONObject(ruleElement));

        Field booleanRuleField = getField(orRuleClass, "rule1", true);
        Object booleanRule = booleanRuleField.get(orRule);
        assertEquals(booleanRuleClass, booleanRule.getClass());
        Field satisfiedField = getField(booleanRuleClass, "satisfied", true);
        assertEquals(true, satisfiedField.get(booleanRule));

        Field isFallingRuleField = getField(orRuleClass, "rule2", true);
        Object isFallingRule = isFallingRuleField.get(orRule);
        assertEquals(isFallingRuleClass, isFallingRule.getClass());

        Field highPriceIndicatorField = getField(isFallingRuleClass, "ref", true);
        Object highPriceIndicator = highPriceIndicatorField.get(isFallingRule);
        assertEquals(highPriceIndicatorClass, highPriceIndicator.getClass());
        assertEquals(series, ((HighPriceIndicator) highPriceIndicator).getBarSeries());

        Field barCountField = getField(isFallingRuleClass, "barCount", true);
        assertEquals(barCount, barCountField.get(isFallingRule));
    }
}