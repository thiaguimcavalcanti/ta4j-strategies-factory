package com.tcrade.builders;

import com.tcrade.enums.JsonElementType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcrade.constants.JsonAttributeConstants.CLASS;
import static com.tcrade.constants.JsonAttributeConstants.PARAMETERS;
import static com.tcrade.constants.JsonAttributeConstants.TYPE;
import static org.apache.commons.lang3.StringUtils.firstNonBlank;
import static org.apache.commons.lang3.reflect.ConstructorUtils.getMatchingAccessibleConstructor;

@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<T> {

    private final Map<String, Object> cachedObjects = new HashMap<>();

    public T build(JSONObject jsonElement) throws Exception {
        String key = jsonElement.toString();

        T cachedEntity = (T) cachedObjects.get(key);
        if (cachedEntity != null) {
            return cachedEntity;
        }

        T newEntity = buildNewEntity(jsonElement.getString(CLASS), jsonElement.getJSONArray(PARAMETERS));

        cachedObjects.put(key, newEntity);

        return newEntity;
    }

    public Map<String, Object> getCachedObjects() {
        return cachedObjects;
    }

    protected T buildNewEntity(String entityClassAsString, JSONArray jsonParameters) throws Exception {
        Class<T> clazz = (Class<T>) getEntityClass(entityClassAsString);
        List<Class<?>> paramTypes = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        for (int i = 0; i < jsonParameters.length(); i++) {
            JSONObject jsonParameter = jsonParameters.getJSONObject(i);
            JsonElementType jsonElementType = jsonParameter.getEnum(JsonElementType.class, TYPE);
            populateConstructorParams(jsonParameter, jsonElementType, paramTypes, params);
        }

        Constructor<T> constructor = getMatchingAccessibleConstructor(clazz, paramTypes.toArray(new Class[0]));
        return constructor.newInstance(params.toArray(new Object[0]));
    }

    protected Class<?> getEntityClass(String entityClassSimpleName) throws ClassNotFoundException {
        String entityClassFullName = getClassMappingMap().get(entityClassSimpleName);
        return Class.forName(firstNonBlank(entityClassFullName, entityClassSimpleName));
    }

    protected abstract void populateConstructorParams(JSONObject jsonParameter,
                                                      JsonElementType jsonElementType,
                                                      List<Class<?>> paramTypes,
                                                      List<Object> params) throws Exception;

    protected abstract Map<String, String> getClassMappingMap();
}
