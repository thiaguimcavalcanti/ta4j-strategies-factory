package com.tcrade.utils;

import com.tcrade.enums.JsonElementType;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.tcrade.enums.JsonElementType.INDICATOR;
import static com.tcrade.enums.JsonElementType.RULE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BuilderUtils {

    private static final String ENTITY_JSON_FORMAT = "{\"type\": \"%s\", \"class\": \"%s\", \"parameters\": [%s]}";
    private static final String DEFAULT_JSON_FORMAT = "{\"type\": \"%s\", \"value\": %s}";

    public static String formatRule(Class<?> clazz, List<String> parameters) {
        return String.format(ENTITY_JSON_FORMAT, RULE, clazz.getSimpleName(), String.join(",", parameters));
    }

    public static String formatIndicatorJson(Class<?> clazz, List<String> parameters) {
        return String.format(ENTITY_JSON_FORMAT, INDICATOR, clazz.getSimpleName(), String.join(",", parameters));
    }

    public static String formatDefaultElementJson(JsonElementType type, Object value) {
        return String.format(DEFAULT_JSON_FORMAT, type, value);
    }

    public static String formatDefaultElementJson(JsonElementType type) {
        return String.format(DEFAULT_JSON_FORMAT, type, null);
    }
}
