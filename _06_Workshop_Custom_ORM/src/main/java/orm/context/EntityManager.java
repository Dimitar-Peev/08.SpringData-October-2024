package orm.context;

import orm.core.Column;
import orm.core.Entity;
import orm.core.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityManager<E> implements DbContext<E> {
    public static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES (%s)";
    public static final String DELETE_SQL = "DELETE FROM %s WHERE %s";
    public static final String DELETE_ENTITY_SQL = "DELETE FROM %s WHERE id = %s";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s)";
    public static final String ALTER_TABLE_SQL = "ALTER TABLE %s %s";
    public static final String CHECK_TABLE_SQL = """
            SELECT column_name FROM information_schema.columns
            WHERE table_schema = 'mini_orm' AND table_name = ?;
            """;
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        int idValue = getIdValue(entity);

        if (idValue == 0) {
            return doInsert(entity);
        }
        return doUpdate(entity, idValue);
    }

    @Override
    public void doCreate(Class<E> entityClass) throws SQLException {
        String tableName = getTableName(entityClass);

        if (checkIfTableExists(tableName)) {
            System.out.println("Table " + tableName + " already exists");
        } else {
            String columnDefinitions = getColumnDefinitions(entityClass);

            String sql = String.format(CREATE_TABLE_SQL, tableName, columnDefinitions);

            connection.createStatement().execute(sql);
        }
    }

    @Override
    public void doAlter(Class<E> entityClass) throws SQLException {
        String tableName = getTableName(entityClass);
        // Column Name -> Information_Schema
        List<String> existingColumnNames = getExistingColumnNames(tableName);

        // Core logic -> filter -> not exist in Information_Schema
        String alterDefinitions = getAlterDefinitions(entityClass, existingColumnNames);

        String sql = String.format(ALTER_TABLE_SQL, tableName, alterDefinitions);
        connection.createStatement().execute(sql);
    }

    private List<String> getExistingColumnNames(String tableName) throws SQLException {
        List<String> existingColumnNames = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_TABLE_SQL);
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            existingColumnNames.add(resultSet.getString("column_name"));
        }
        return existingColumnNames;
    }

    private String getAlterDefinitions(Class<E> entityClass, List<String> existingColumnNames) {
        List<String> columnDefinitions = new ArrayList<>();
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().equals(Column.class)))
                .forEach(field -> {
                    String name = field.getAnnotation(Column.class).name();
                    if (!existingColumnNames.contains(name)) {
                        columnDefinitions.add(String.format("ADD COLUMN %s %s", name, getMySQLType(field)));
                    }
                });

        return String.join(", ", columnDefinitions);
    }

    private boolean checkIfTableExists(String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_TABLE_SQL);
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private String getColumnDefinitions(Class<E> entityClass) {
        List<String> columns = new ArrayList<>();

        Arrays.stream(entityClass.getDeclaredFields())
                .forEach(field -> {
                    StringBuilder columnDefinitions = new StringBuilder();
                    if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(Column.class))) {
                        String columnName = field.getAnnotation(Column.class).name();
                        String columnType = getMySQLType(field);
                        columnDefinitions.append(String.format("%s %s", columnName, columnType));
                    }

                    if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(annotation -> annotation.annotationType() == (Id.class))) {
                        columnDefinitions.append(" PRIMARY KEY");
                        if (getMySQLType(field).equals("INT")) {
                            columnDefinitions.append(" AUTO_INCREMENT");
                        }
                    }

                    columns.add(columnDefinitions.toString());
                });
//        System.out.println();
        return String.join(", ", columns);
    }

    private String getMySQLType(Field field) {
        return switch (field.getType().getSimpleName()) {
            case "String" -> "VARCHAR(255)";
            case "Integer", "int" -> "INT";
            case "Double", "double" -> "DOUBLE(19,2)";
            case "boolean" -> "BOOLEAN";
            case "LocalDate" -> "DATE";
            default -> "VARCHAR(100)";
        };
    }

    // Get all fields
    // Find field with @Id
    // Get value from field
    private int getIdValue(E entity) throws IllegalAccessException {
        List<Field> idFields = Arrays
                .stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();

        if (idFields.size() != 1) {
            throw new IllegalArgumentException("Entity must have 1 ID field");
        }

        Field idField = idFields.get(0);
        idField.setAccessible(true);
        return (int) idField.get(entity);
    }

    // Find table name
    // Find column names
    // Find column values
    // Generate + Execute sql
    private boolean doInsert(E entity) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        List<String> columnNames = findEntityColumns(entity);
        List<String> columnValues = findEntityValues(entity);

        String insertSQL = String.format(INSERT_SQL,
                tableName,
                String.join(",", columnNames),
                String.join(",", columnValues)
        );

        int updateCount = connection.prepareStatement(insertSQL).executeUpdate();
        return updateCount == 1;
    }

    private List<String> findEntityValues(E entity) throws IllegalAccessException {
        List<String> result = new ArrayList<>();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(entity).toString();
            result.add("'" + value + "'");
        }

        return result;
    }

    private List<String> findEntityColumns(E entity) {
        return Arrays
                .stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(columnAnn -> columnAnn.getAnnotation(Column.class).name())
                .toList();
    }

    private String getTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);

        if (annotation == null) {
            throw new IllegalArgumentException("Entity annotation missing!");
        }

        return annotation.name();
    }

    private boolean doUpdate(E entity, int idValue) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        List<String> columnNames = findEntityColumns(entity);
        List<String> columnValues = findEntityValues(entity);

        List<String> updateColumns = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
            String formatted = String.format("%s=%s",
                    columnNames.get(i),
                    columnValues.get(i));

            updateColumns.add(formatted);
        }

        String updateSQL = String.format("UPDATE %s SET %s WHERE id = %d",
                tableName,
                String.join(",", updateColumns),
                idValue);

        int updateCount = connection.prepareStatement(updateSQL).executeUpdate();
        return updateCount == 1;
    }

    @Override
    public Iterable<E> find(Class<E> table) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return find(table, null);
    }

    @Override
    public Iterable<E> find(Class<E> table, String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String tableName = getTableName(table);

        String selectSingleSQL = String.format("SELECT * FROM %s %s",
                tableName,
                where == null ? "" : where);

        ResultSet resultSet = connection.prepareStatement(selectSingleSQL).executeQuery();

        List<E> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(mapEntity(table, resultSet));
        }

        return result;
    }


    @Override
    public E findFirst(Class<E> table) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(table, null);
    }

    @Override
    public E findFirst(Class<E> table, String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // option 2
        String withLimit = where == null ? "LIMIT 1" : where + " LIMIT 1";

        Iterable<E> es = find(table, withLimit);

        if (!es.iterator().hasNext()) {
            return null;
        }

        return es.iterator().next();

        // option 1
//        String tableName = getTableName(table);
//
//        String selectSingleSQL = String.format("SELECT * FROM %s %s %s",
//                tableName,
//                where == null ? "" : where,
//                "LIMIT 1");
//
//        ResultSet resultSet = connection.prepareStatement(selectSingleSQL).executeQuery();
//
//        if (resultSet.next()) {
//            return mapEntity(table, resultSet);
//        }
//
//        return null;
    }

    @Override
    public int delete(Class<E> table, String where) throws SQLException {
        String tableName = getTableName(table);
        int deletedRows = connection.createStatement().executeUpdate(String.format(DELETE_SQL, tableName, where));
        return deletedRows;
    }

    @Override
    public int delete(E entity) throws SQLException, IllegalAccessException {
        String tableName = getTableName(entity.getClass());
        String sql = String.format(DELETE_ENTITY_SQL, tableName, getIdValue(entity));

        int deletedRows = connection.createStatement().executeUpdate(sql);
        return deletedRows;
    }

    private E mapEntity(Class<E> type, ResultSet dbResult) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        E result = type.getDeclaredConstructor().newInstance();

        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class) &&
                    !field.isAnnotationPresent(Column.class)) {
                continue;
            }

            result = mapField(result, field, dbResult);
        }

        return result;
    }

    private E mapField(E object, Field field, ResultSet dbResult) throws IllegalAccessException, SQLException {
        String column = "id";

        if (field.isAnnotationPresent(Column.class)) {
            column = field.getAnnotation(Column.class).name();
        }

        Object dbValue = mapValue(field, column, dbResult);

        field.setAccessible(true);
        field.set(object, dbValue);

        return object;
    }

    private Object mapValue(Field field, String column, ResultSet dbResult) throws SQLException {
        if (field.getType() == int.class || field.getType() == Integer.class) {
            return dbResult.getInt(column);
        } else if (field.getType() == String.class) {
            return dbResult.getString(column);
        } else if (field.getType() == LocalDate.class) {
            String date = dbResult.getString(column);
            return LocalDate.parse(date);
        } else if (field.getType() == Double.class) {
            return dbResult.getDouble(column);
        }

        throw new IllegalArgumentException("Unsupported type " + field.getType());
    }

}
