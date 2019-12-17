package ru.otus.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.DbExecutorException;
import ru.otus.api.metadata.MetaDataHolder;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.metadata.MetaData;
import ru.otus.helpers.reflect.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class DbExecutor<T> {
    private static Logger LOG = LoggerFactory.getLogger(DbExecutor.class);
    private SessionManager sessionManager;
    private MetaDataHolder metaDataHolder;
    private MetaData<T> metaData;


    public DbExecutor(SessionManager sessionManager, MetaDataHolder metaDataHolder) {
        this.sessionManager = sessionManager;
        this.metaDataHolder = metaDataHolder;
    }

    private long insertRecord(String sql, List<Object> params) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            sessionManager.commitSession();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            LOG.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private Optional<T> selectRecord(Connection connection, String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rsHandler.apply(rs));
                } else {
                    throw new DbExecutorException("No object found.");
                }
            }
        }
    }

    public void create(T objectData) {
        if (Objects.isNull(metaData)) metaData = metaDataHolder.getObjectMetaData(objectData.getClass());
        try {
            insertRecord(metaData.getSqlInsert(), ReflectionHelper.getColumnValues(metaData.getFields(), objectData));
        } catch (SQLException e) {
            LOG.error("Не удалось сохранить {}!", metaData.getTableName());
            e.printStackTrace();
        }
        LOG.info("{} сохранен в базе!", metaData.getTableName());
    }

    public void update(T objectData) {
        if (Objects.isNull(metaData)) metaData = metaDataHolder.getObjectMetaData(objectData.getClass());
        Integer idValue = getObjectIdFromDatabase(objectData, metaData);
        List<Object> columnValues = ReflectionHelper.getColumnValues(metaData.getFields(), objectData);
        columnValues.add(idValue);
        try {
            updateRecord(metaData.getSqlUpdate(), columnValues);
        } catch (SQLException e) {
            LOG.error("Объект '{}' не обновлен в базе !", objectData.getClass().getSimpleName());
            e.printStackTrace();
        }
        LOG.info("Объект '{}' обновлен в базе !", objectData.getClass().getSimpleName());
    }

    public T load(long id, Class<T> clazz) {
        sessionManager.beginSession();
        Optional<T> optionalT = Optional.empty();
        try {
            T instance = metaData.getTConstructor().newInstance();
            optionalT = selectRecord(sessionManager.getConnection(), metaData.getSqlSelect(), id, resultSet -> {
                try {
                    for (Field field : metaData.getAllDeclaredFields()) {
                        field.setAccessible(true);
                        field.set(instance, resultSet.getObject(field.getName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return instance;
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionManager.close();
        }
        return optionalT.orElseThrow(() -> new DbExecutorException("Cant load."));
    }

    private void updateRecord(String sqlUpdate, List<Object> columnValues) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate, Statement.NO_GENERATED_KEYS)) {
            for (int i = 0; i < columnValues.size(); i++) {
                preparedStatement.setObject(i + 1, columnValues.get(i));
            }
            preparedStatement.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException e) {
            sessionManager.rollbackSession();
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    private int getObjectIdFromDatabase(T object, MetaData metaData) {
        String id = null;
        try {
            id = String.valueOf(metaData.getId().get(object));
        } catch (IllegalAccessException ie) {
            ie.printStackTrace();
        }
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(metaData.getSqlSelect())) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LOG.info("Object '{}' with id={}  found in DB", metaData.getTableName(), id);
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.info("Object '{}' with id={} not found!", metaData.getTableName(), id);
        throw new DbExecutorException("No object found");
    }
}
