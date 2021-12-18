package com.tcrade.builders;

import com.tcrade.enums.JsonElementType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Rule;

import java.util.List;

import static com.tcrade.constants.JsonAttributeConstants.CLASS;
import static com.tcrade.constants.JsonAttributeConstants.VALUE;

@RequiredArgsConstructor
public class RuleBuilder extends AbstractBuilder<Rule> {

    private final IndicatorBuilder indicatorBuilder;

    protected void populateConstructorParams(JSONObject jsonParameter,
                                            JsonElementType jsonElementType,
                                            List<Class<?>> paramTypes,
                                            List<Object> params) throws Exception {
        switch (jsonElementType) {
            case RULE -> {
                paramTypes.add(Class.forName(jsonParameter.getString(CLASS)));
                params.add(build(jsonParameter));
            }
            case INDICATOR -> {
                paramTypes.add(Class.forName(jsonParameter.getString(CLASS)));
                params.add(indicatorBuilder.build(jsonParameter));
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
