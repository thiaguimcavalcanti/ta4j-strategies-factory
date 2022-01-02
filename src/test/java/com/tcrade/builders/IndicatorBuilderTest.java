package com.tcrade.builders;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AroonUpIndicator;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.indicators.helpers.DifferencePercentage;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

import java.lang.reflect.Field;

import static com.tcrade.enums.JsonElementType.BAR_SERIES;
import static com.tcrade.enums.JsonElementType.INTEGER;
import static com.tcrade.enums.JsonElementType.NUMBER;
import static com.tcrade.utils.BuilderUtils.formatDefaultElementJson;
import static com.tcrade.utils.BuilderUtils.formatIndicatorJson;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.reflect.FieldUtils.getField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndicatorBuilderTest {

    private IndicatorBuilder indicatorBuilder;

    private BarSeries series;

    @BeforeEach
    public void setUp() {
        series = new BaseBarSeries();
        indicatorBuilder = new IndicatorBuilder(series);
    }

    @Test
    public void shouldNotBuildWhenNoJsonElementIsSent() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> indicatorBuilder.build(null));
        assertEquals("An element is missing during parsing of its parent!", exception.getMessage());
    }

    @Test
    public void shouldBuildAnIndicatorWithBarSeriesAndIntegerConstructorParameters() throws Exception {
        int barCount = 20;
        String barCountJson = formatDefaultElementJson(INTEGER, barCount);

        Class<CCIIndicator> cciIndicatorClass = CCIIndicator.class;
        String indicatorElement = formatIndicatorJson(cciIndicatorClass, asList(formatDefaultElementJson(BAR_SERIES), barCountJson));

        Indicator<?> cciIndicator = indicatorBuilder.build(new JSONObject(indicatorElement));

        assertEquals(series, cciIndicator.getBarSeries());
        assertEquals(cciIndicatorClass.getSimpleName() + " barCount: " + barCount, cciIndicator.toString());
    }

    @Test
    public void shouldBuildAnIndicatorWithIndicatorConstructorParameters() throws Exception {
        Class<HighPriceIndicator> highPriceIndicatorClass = HighPriceIndicator.class;
        String highPriceIndicatorJson = formatIndicatorJson(highPriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        int barCount = 20;
        String barCountJson = formatDefaultElementJson(INTEGER, barCount);

        Class<AroonUpIndicator> aroonUpIndicatorClass = AroonUpIndicator.class;
        String indicatorElement = formatIndicatorJson(aroonUpIndicatorClass, asList(highPriceIndicatorJson, barCountJson));

        Indicator<?> aroonUpIndicator = indicatorBuilder.build(new JSONObject(indicatorElement));

        assertEquals(series, aroonUpIndicator.getBarSeries());
        assertEquals(aroonUpIndicatorClass.getSimpleName() + " barCount: " + barCount, aroonUpIndicator.toString());

        Field highPriceIndicatorField = getField(aroonUpIndicatorClass, "highPriceIndicator", true);
        Object highPriceIndicator = highPriceIndicatorField.get(aroonUpIndicator);
        assertEquals(highPriceIndicatorClass, highPriceIndicator.getClass());
        assertEquals(series, ((HighPriceIndicator) highPriceIndicator).getBarSeries());
    }

    @Test
    public void shouldBuildAnIndicatorWithNumberConstructorParameters() throws Exception {
        Class<HighPriceIndicator> highPriceIndicatorClass = HighPriceIndicator.class;
        String highPriceIndicatorJson = formatIndicatorJson(highPriceIndicatorClass, singletonList(formatDefaultElementJson(BAR_SERIES)));

        Num percentageThreshold = DecimalNum.valueOf(20);
        String percentageThresholdJson = formatDefaultElementJson(NUMBER, percentageThreshold.doubleValue());

        Class<DifferencePercentage> differencePercentageClass = DifferencePercentage.class;
        String indicatorElement = formatIndicatorJson(differencePercentageClass, asList(highPriceIndicatorJson, percentageThresholdJson));

        Indicator<?> differentPercentage = indicatorBuilder.build(new JSONObject(indicatorElement));

        assertEquals(differencePercentageClass, differentPercentage.getClass());
        assertEquals(series, differentPercentage.getBarSeries());

        Field highPriceIndicatorField = getField(differencePercentageClass, "indicator", true);
        Object highPriceIndicator = highPriceIndicatorField.get(differentPercentage);
        assertEquals(highPriceIndicatorClass, highPriceIndicator.getClass());
        assertEquals(series, ((HighPriceIndicator) highPriceIndicator).getBarSeries());

        Field percentageThresholdField = getField(differencePercentageClass, "percentageThreshold", true);
        assertEquals(percentageThreshold, percentageThresholdField.get(differentPercentage));
    }
}