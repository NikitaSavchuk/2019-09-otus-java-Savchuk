package ru.otus.helpers.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionHelper {
    public static List<Object> getColumnValues(List<Field> fields, Object object) {
        List<Object> columnValues = new ArrayList<>();
        try {
            for (Field field : fields) columnValues.add(field.get(object));
        } catch (IllegalAccessException ie) {
            ie.printStackTrace();
        }
        return columnValues;
    }
}
