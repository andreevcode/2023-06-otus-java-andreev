package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private static final String DELIMITER = ", ";
    private final EntityClassMetaData<T> classMetaData;
    private final String tableName;
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
        this.tableName = classMetaData.getName();
        this.selectAllSql = "select " + classMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(DELIMITER)) +
                " from " + tableName;

        this.selectByIdSql = findSelectByIdSql();
        this.insertSql = findInsertSql();
        this.updateSql = findUpdateSql();
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private String findSelectByIdSql() {
        return selectAllSql + getPrimaryKeyCondition();
    }

    private String findInsertSql() {
        var sql = new StringBuilder()
                .append("insert into ")
                .append(tableName);

        var columns = "(" + mapWithDelimiter(classMetaData.getFieldsWithoutId(), Field::getName) + ") ";
        var params = "values(" + mapWithDelimiter(classMetaData.getFieldsWithoutId(), field -> "?") + ")";

        return sql
                .append(columns)
                .append(params)
                .toString();
    }

    private String findUpdateSql() {
        var sql = new StringBuilder()
                .append("update ")
                .append(tableName)
                .append(" set ");

        var columnsAndParams = mapWithDelimiter(classMetaData.getFieldsWithoutId(),
                field -> field.getName() + " = ?") + " ";

        return sql
                .append(columnsAndParams)
                .append(getPrimaryKeyCondition())
                .toString();
    }

    private String mapWithDelimiter(List<Field> fields, Function<Field, String> mapper) {
        return fields.stream()
                .map(mapper)
                .sorted()
                .collect(Collectors.joining(DELIMITER));
    }

    private String getPrimaryKeyCondition() {
        return " where " + classMetaData.getIdField().getName() + " = ?";
    }
}
