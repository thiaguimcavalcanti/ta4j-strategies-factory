package com.tcrade.builders;

import com.tcrade.enums.JsonElementType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;

import java.util.List;

import static com.tcrade.constants.JsonAttributeConstants.CLASS;
import static com.tcrade.constants.JsonAttributeConstants.VALUE;

@RequiredArgsConstructor
public class IndicatorBuilder extends AbstractBuilder<Indicator<?>> {

    private final BarSeries series;

    protected void populateConstructorParams(JSONObject jsonParameter,
                                             JsonElementType jsonElementType,
                                             List<Class<?>> paramTypes,
                                             List<Object> params) throws Exception {
        switch (jsonElementType) {
            case INDICATOR -> {
                paramTypes.add(Class.forName(jsonParameter.getString(CLASS)));
                params.add(build(jsonParameter));
            }
            case TIME_SERIES -> {
                paramTypes.add(BarSeries.class);
                params.add(series);
            }
            case NUMBER -> {
                paramTypes.add(Number.class);
                params.add(jsonParameter.getDouble(VALUE));
            }
            case INTEGER -> {
                paramTypes.add(Integer.TYPE);
                params.add(jsonParameter.getInt(VALUE));
            }
        }
    }
}
