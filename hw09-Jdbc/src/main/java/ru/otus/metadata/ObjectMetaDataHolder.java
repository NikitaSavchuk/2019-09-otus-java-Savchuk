package ru.otus.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;
import ru.otus.api.metadata.MetaDataHolder;
import ru.otus.api.DbExecutorException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class ObjectMetaDataHolder implements MetaDataHolder {
    private static Logger LOG = LoggerFactory.getLogger(ObjectMetaDataHolder.class);
    private MetaData metaData = new MetaData();

    public ObjectMetaDataHolder(Class clazz) {
        saveObjectMetadata(clazz);
        LOG.info("'{}' metadata is saved.", clazz.getSimpleName());
    }

    @Override
    public void saveObjectMetadata(Class className) {
        List<Field> fields = new ArrayList<>();
        List<Field> allDeclaredFields = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        metaData.setTableName(className.getSimpleName());

        Arrays.stream(className.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            allDeclaredFields.add(field);
            if (field.isAnnotationPresent(Id.class)) {
                metaData.setId(field);
                metaData.setIdFieldName(field.getName());
            } else {
                fields.add(field);
                columnNames.add(field.getName());
            }
        });
        if (Objects.isNull(metaData.getId())) throw new DbExecutorException("no Id annotation!");
        try {
        metaData.setTConstructor(className.getDeclaredConstructor());
        } catch (NoSuchMethodException nse) {
            nse.printStackTrace();
        }

        metaData.setFields(fields);
        metaData.setAllDeclaredFields(allDeclaredFields);
        metaData.setSqlSelect(format("SELECT * FROM %s WHERE %s =?", metaData.getTableName(), metaData.getIdFieldName()));
        metaData.setSqlInsert(format("insert into %s(%s,%s) values (?,?)", metaData.getTableName(), columnNames.get(0), columnNames.get(1)));
        metaData.setSqlUpdate(format("update %s set %s = ?, %s = ? where %s = ?", metaData.getTableName(), columnNames.get(0), columnNames.get(1), metaData.getIdFieldName()));

    }


    @Override
    public MetaData getObjectMetaData(Class className) {
        return metaData;
    }
}
