package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final List<Field> allFields;
    private final Field idField;
    private final List<Field> fieldsWithoutId;
    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.allFields = findAllFields();
        this.idField = findIdField();
        this.fieldsWithoutId = findFieldsWithoutId();
        this.constructor = findConstructor();
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private List<Field> findAllFields() {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    private Field findIdField() {
        if (allFields == null) {
            return null;
        }
        var idFields = allFields.stream()
                .filter(field -> {
                    var annotations = Arrays.asList(field.getAnnotations());
                    return annotations.stream().anyMatch(Id.class::isInstance);
                })
                .toList();
        if (idFields.isEmpty()){
            throw new RuntimeException("Id field is not found in " + clazz.getName() + " class");
        }
        if (idFields.size() == 1){
            return idFields.get(0);
        } else {
            throw new RuntimeException("More than one Id fields are found in " + clazz.getName() + " class");
        }
    }

    private List<Field> findFieldsWithoutId() {
        if (allFields == null) {
            return new ArrayList<>();
        }
        if (idField == null) {
            return allFields;
        }
        return allFields.stream().filter(f -> f != idField).sorted(Comparator.comparing(Field::getName)).toList();
    }

    @SuppressWarnings("unchecked")
    private Constructor<T> findConstructor() {
        return  Arrays.stream((Constructor<T>[]) clazz.getConstructors())
                .filter(el -> el.getParameterCount() == allFields.size())
                .findFirst()
                .orElse(null);
    }

}
