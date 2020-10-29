package com.wuxp.querydsl.core.support;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.ReflectionUtils.AnnotationFieldFilter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author wuxp
 */
public class AbstractEntityMetadata implements EntityMetadata {

    private static final AnnotationFieldFilter CREATED_BY_FILTER = new AnnotationFieldFilter(CreatedBy.class);
    private static final AnnotationFieldFilter CREATED_DATE_FILTER = new AnnotationFieldFilter(CreatedDate.class);
    private static final AnnotationFieldFilter LAST_MODIFIED_BY_FILTER = new AnnotationFieldFilter(LastModifiedBy.class);
    private static final AnnotationFieldFilter LAST_MODIFIED_DATE_FILTER = new AnnotationFieldFilter(
            LastModifiedDate.class);

    static final List<String> SUPPORTED_DATE_TYPES;

    static {

        List<String> types = new ArrayList<>(5);
        types.add("org.joda.time.DateTime");
        types.add("org.joda.time.LocalDateTime");
        types.add(Date.class.getName());
        types.add(Long.class.getName());
        types.add(long.class.getName());

        SUPPORTED_DATE_TYPES = Collections.unmodifiableList(types);
    }

    private final Class<?> entityClass;

    private final Optional<Field> idField;

    private final Optional<Field> createdByField;

    private final Optional<Field> createdTimeField;

    private final Optional<Field> lastModifiedByField;

    private final Optional<Field> lastModifiedDTimeField;

    public AbstractEntityMetadata(Class<?> entityClass, Class<? extends Annotation> idAnnotationType) {
        Assert.notNull(entityClass, "Given entityClass must not be null!");
        this.entityClass = entityClass;
        this.idField = Optional.ofNullable(ReflectionUtils.findField(entityClass, new AnnotationFieldFilter(idAnnotationType)));
        this.createdByField = Optional.ofNullable(ReflectionUtils.findField(entityClass, CREATED_BY_FILTER));
        this.createdTimeField = Optional.ofNullable(ReflectionUtils.findField(entityClass, CREATED_DATE_FILTER));
        this.lastModifiedByField = Optional.ofNullable(ReflectionUtils.findField(entityClass, LAST_MODIFIED_BY_FILTER));
        this.lastModifiedDTimeField = Optional.ofNullable(ReflectionUtils.findField(entityClass, LAST_MODIFIED_DATE_FILTER));

        assertValidDateFieldType(createdTimeField);
        assertValidDateFieldType(lastModifiedDTimeField);
    }


    @Override
    public Class<?> getDomainClass() {
        return entityClass;
    }


    @Override
    public Optional<Field> getIdField() {
        return idField;
    }

    @Override
    public Optional<Field> getCreateTimeField() {
        return createdTimeField;
    }

    @Override
    public Optional<Field> getLastModifiedTimeField() {
        return this.lastModifiedDTimeField;
    }

    @Override
    public Optional<Field> getCreateByField() {
        return this.createdByField;
    }

    @Override
    public Optional<Field> getLastModifiedByField() {
        return this.lastModifiedByField;
    }

    /**
     * Checks whether the given field has a type that is a supported date type.
     *
     * @param field
     */
    private void assertValidDateFieldType(Optional<Field> field) {

        field.ifPresent(it -> {

            if (SUPPORTED_DATE_TYPES.contains(it.getType().getName())) {
                return;
            }

            Class<?> type = it.getType();

            if (Jsr310Converters.supports(type)) {
                return;
            }

            throw new IllegalStateException(String.format(
                    "Found created/modified date field with type %s but only %s as well as java.time types are supported!", type,
                    SUPPORTED_DATE_TYPES));
        });
    }
}
