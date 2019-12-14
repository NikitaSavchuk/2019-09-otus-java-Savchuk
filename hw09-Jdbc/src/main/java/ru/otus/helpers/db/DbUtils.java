package ru.otus.helpers.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

import static java.lang.String.format;

public class DbUtils {
    private static Logger LOG = LoggerFactory.getLogger(DbUtils.class);

    public static void createTable(DataSource dataSource, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
        String tableName = sql.substring(sql.indexOf("table") + 6, sql.indexOf("("));
        LOG.info("Table {} created", tableName);
    }

    public static void selectAllRecords(DataSource dataSource, String tableName) throws SQLException {
        String selectAllQuery = format("select * from %s", tableName);
        String dataBaseName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println(format("Database %s, table %s", dataBaseName, tableName));
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    System.out.println(format("%s\t\t", metaData.getColumnName(i)));
                    if (i == count) System.out.println();
                }
                while (resultSet.next()) {
                    for (int i = 1; i <= count; i++) {
                        Object object = resultSet.getObject(i);
                        System.out.println(format("%s\t\t", object == null ? "NULL" : object.toString()));
                    }
                    System.out.println(format("%n"));
                }
            }
        }
    }
}
