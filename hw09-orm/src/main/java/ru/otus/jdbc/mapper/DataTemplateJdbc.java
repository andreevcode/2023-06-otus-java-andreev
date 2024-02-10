package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData<T> entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
                    try {
                        if (rs.next()) {
                            return createObject(rs);
                        }
                        return null;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var modelList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            modelList.add(createObject(rs));
                        }
                        return modelList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T model) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), splitToParamsWithoutId(model));
    }

    @Override
    public void update(Connection connection, T model) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), splitToParamsWithLastId(model));
    }

    private List<Object> splitToParamsWithoutId(T model) {
        List<Object> params = new ArrayList<>();
        entityClassMetaData.getFieldsWithoutId().forEach(field -> params.add(getValue(field, model)));
        return params;
    }

    private List<Object> splitToParamsWithLastId(T model) {
        List<Object> params = new ArrayList<>();
        var fields = new ArrayList<>(entityClassMetaData.getFieldsWithoutId());
        fields.add(entityClassMetaData.getIdField());
        fields.forEach(field -> params.add(getValue(field, model)));
        return params;
    }

    private Object getValue(Field field, T model) {
        try {
            if (!field.canAccess(model)) {
                field.setAccessible(true);
                var value = field.get(model);
                field.setAccessible(false);
                return value;
            } else {
                return field.get(model);
            }
        } catch (IllegalAccessException e) {
            throw new DataTemplateException("Error during preparing model params", e);
        }
    }

    private T createObject(ResultSet rs) throws SQLException {
        var rsMetaData = rs.getMetaData();
        var args = new Object[rsMetaData.getColumnCount()];
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
            args[i - 1] = rs.getObject(i);
        }
        try {
            return entityClassMetaData.getConstructor().newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException("Error during creating object from resultSet", e);
        }
    }
}
