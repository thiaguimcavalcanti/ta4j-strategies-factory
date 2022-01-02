package com.tcrade;

import com.tcrade.builders.RuleBuilder;
import com.tcrade.enums.JsonOperatorType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Rule;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.AndRule;
import org.ta4j.core.rules.BooleanRule;
import org.ta4j.core.rules.OrRule;
import org.ta4j.core.rules.TrailingStopLossRule;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static com.tcrade.enums.JsonElementType.BAR_SERIES;
import static com.tcrade.enums.JsonElementType.INTEGER;
import static com.tcrade.enums.JsonElementType.NUM;
import static com.tcrade.utils.BuilderUtils.formatDefaultElementJson;
import static com.tcrade.utils.BuilderUtils.formatFullPayload;
import static com.tcrade.utils.BuilderUtils.formatIndicatorJson;
import static com.tcrade.utils.BuilderUtils.formatRule;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.reflect.FieldUtils.getField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleParserTest {

    @Mock
    private RuleBuilder ruleBuilder;

    private RuleParser ruleParser;

    @BeforeEach
    void setUp() {
        ruleParser = new RuleParser(ruleBuilder);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    public void shouldReturnBooleanRuleAsTrueWhenAnEmptyPayloadIsSent(String payload) throws Exception {
        Rule parsedRule = ruleParser.parse(payload);

        assertEquals(BooleanRule.class, parsedRule.getClass());
        Field satisfiedField = getField(BooleanRule.class, "satisfied", true);
        assertEquals(true, satisfiedField.get(parsedRule));
    }

    @ParameterizedTest
    @MethodSource("operatorProvider")
    public void shouldParseRuleWithAndOperator(JsonOperatorType operatorType,
                                               Class<?> operatorRuleClass,
                                               boolean operatorExpectResult) throws Exception {
        String ruleElement = getFormattedTrailingStopLossRule();

        String fullPayload = formatFullPayload(operatorType, singletonList(ruleElement));

        TrailingStopLossRule expectedRule = new TrailingStopLossRule(new HighPriceIndicator(new BaseBarSeries()), DoubleNum.valueOf(1));
        when(ruleBuilder.build(any(JSONObject.class))).thenReturn(expectedRule);

        Rule parsedRule = ruleParser.parse(fullPayload);

        assertEquals(operatorRuleClass, parsedRule.getClass());

        Field rule1Field = getField(operatorRuleClass, "rule1", true);
        Object rule1 = rule1Field.get(parsedRule);
        assertEquals(BooleanRule.class, rule1.getClass());
        Field satisfiedField = getField(BooleanRule.class, "satisfied", true);
        assertEquals(operatorExpectResult, satisfiedField.get(rule1));

        Field rule2Field = getField(operatorRuleClass, "rule2", true);
        Object rule2 = rule2Field.get(parsedRule);
        assertEquals(TrailingStopLossRule.class, rule2.getClass());
    }

    private static Stream<Arguments> operatorProvider() {
        return Stream.of(
                Arguments.of(JsonOperatorType.AND, AndRule.class, true),
                Arguments.of(JsonOperatorType.OR, OrRule.class, false)
        );
    }

    /**
     * Build Rule: TrailingStopLossRule class
     *  - First constructor parameter: new HighPriceIndicator(new BaseBarSeries())
     *  - Second constructor parameter: DoubleNum 10
     *  - Third constructor parameter: Integer 20
     *
     * @return TrailingStopLossRule
     */
    private String getFormattedTrailingStopLossRule() {
        Class<HighPriceIndicator> highPriceIndicatorClass = HighPriceIndicator.class;
        String highPriceIndicatorJson = formatIndicatorJson(highPriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        Num lossPercentage = DoubleNum.valueOf(10);
        String lossPercentageJson = formatDefaultElementJson(NUM, lossPercentage);

        int barCount = 20;
        String barCountJson = formatDefaultElementJson(INTEGER, barCount);

        Class<TrailingStopLossRule> trailingStopLossRuleClass = TrailingStopLossRule.class;
        return formatRule(trailingStopLossRuleClass, asList(highPriceIndicatorJson, lossPercentageJson, barCountJson));
    }
}