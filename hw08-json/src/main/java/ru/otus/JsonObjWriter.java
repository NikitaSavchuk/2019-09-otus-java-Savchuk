package ru.otus;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

class JsonObjWriter {

    String toJsonString(Object obj) {
        if (obj == null) {
            return JsonValue.NULL.toString();
        }
        JsonValue jsonValue = createJsonValue(obj.getClass(), obj);
        return jsonValue == null ? null : jsonValue.toString();
    }

    private JsonValue createJsonValue(Class<?> type, Object obj) {
        boolean isNull = Objects.isNull(obj);

        if (isPrimitiveWrapper(type)) {
            return createPrimitiveValue(obj);
        } else if (type.isArray()) {
            return isNull ? JsonArray.EMPTY_JSON_ARRAY : createArrayValue(obj);
        } else if (Collection.class.isAssignableFrom(type)) {
            Collection collection = (Collection) obj;
            return isNull ? JsonArray.EMPTY_JSON_ARRAY : createArrayValue(collection.toArray());
        } else if (Map.class.isAssignableFrom(type)) {
            return isNull ? JsonArray.EMPTY_JSON_OBJECT : createMapValue(obj);
        } else {
            try {
                return createJsonObject(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return JsonValue.NULL;
    }

    private JsonValue createJsonObject(Object obj) throws IllegalAccessException {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            Object object = field.get(obj);
            if (object != null && (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers))) {
                jsonObjectBuilder.add(field.getName(), createJsonValue(field.getType(), object));
            }
        }
        return jsonObjectBuilder.build();
    }

    private JsonValue createMapValue(Object obj) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        Map<?, ?> map = (Map<?, ?>) obj;
        map.forEach((k, v) -> jsonObjectBuilder.add(k.toString(), createJsonValue(v.getClass(), v)));
        return jsonObjectBuilder.build();
    }

    private JsonValue createArrayValue(Object obj) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        IntStream.range(0, Array.getLength(obj))
                .mapToObj(i -> Array.get(obj, i))
                .map(o -> createJsonValue(o.getClass(), o))
                .forEach(jsonArrayBuilder::add);
        return jsonArrayBuilder.build();
    }

    private JsonValue createPrimitiveValue(Object obj) {
        if (obj instanceof Number) {
            Number number = (Number) obj;
            if (number instanceof Double || number instanceof Float) {
                return Json.createValue(number.doubleValue());
            } else {
                return Json.createValue(number.longValue());
            }
        } else if (obj instanceof Boolean) {
            return obj.equals(true) ? JsonValue.TRUE : JsonValue.FALSE;
        } else {
            return Json.createValue(obj.toString());
        }
    }

    private boolean isPrimitiveWrapper(Class<?> type) {
        return type.isPrimitive() ||
                String.class.isAssignableFrom(type) ||
                Number.class.isAssignableFrom(type) ||
                Boolean.class.isAssignableFrom(type) ||
                Character.class.isAssignableFrom(type);
    }
}
