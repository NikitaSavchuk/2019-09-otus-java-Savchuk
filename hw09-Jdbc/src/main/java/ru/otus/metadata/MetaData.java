package ru.otus.metadata;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MetaData<T> {
    private String tableName;
    private String idFieldName;
    private String sqlSelect;
    private String sqlInsert;
    private String sqlUpdate;
    private Field id;
    private List<Field> fields = new ArrayList<>();
    private List<Field> allDeclaredFields = new ArrayList<>();
    private Constructor<T> tConstructor;
}
