package com.tcrade.builders;

import com.tcrade.enums.JsonElementType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;

import java.util.List;
import java.util.Map;

import static com.tcrade.constants.JsonAttributeConstants.CLASS;
import static com.tcrade.constants.JsonAttributeConstants.VALUE;
import static com.tcrade.constants.Ta4jClassMapping.INDICATOR_CLASS_MAPPING;

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
            case BAR_SERIES -> {
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

    @Override
    protected Map<String, String> getClassMappingMap() {
        return INDICATOR_CLASS_MAPPING;
    }
}
