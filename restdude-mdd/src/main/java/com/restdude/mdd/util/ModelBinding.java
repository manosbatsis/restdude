/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.util;


import com.yahoo.elide.annotation.ComputedAttribute;
import com.yahoo.elide.annotation.ComputedRelationship;
import com.yahoo.elide.annotation.Exclude;
import com.yahoo.elide.annotation.OnCommit;
import com.yahoo.elide.annotation.OnCreate;
import com.yahoo.elide.annotation.OnDelete;
import com.yahoo.elide.annotation.OnRead;
import com.yahoo.elide.annotation.OnUpdate;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.core.EntityPermissions;
import com.yahoo.elide.core.Initializer;
import com.yahoo.elide.core.RelationshipType;
import com.yahoo.elide.core.exceptions.DuplicateMappingException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Hints for converting between application models and JSON-API documents.
 *
 * @see com.yahoo.elide.annotation.Include#type
 */
class ModelBinding {

    private static final List<Method> OBJ_METHODS = Arrays.asList(Object.class.getMethods());

    public final Class<?> modelType;
    public final String jsonApiType;
    @Getter private AccessibleObject idField;
    @Getter private String idFieldName;
    @Getter private Class<?> modelIdType;
    @Getter @Setter private Initializer initializer;

    public final EntityPermissions entityPermissions;
    public final List<String> attributes;
    public final List<String> relationships;
    public final ConcurrentLinkedDeque<String> attributesDeque = new ConcurrentLinkedDeque<>();
    public final ConcurrentLinkedDeque<String> relationshipsDeque = new ConcurrentLinkedDeque<>();

    public final ConcurrentHashMap<String, RelationshipType> relationshipTypes = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, String> relationshipToInverse = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, CascadeType[]> relationshipToCascadeTypes = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, AccessibleObject> fieldsToValues = new ConcurrentHashMap<>();
    public final MultiValuedMap<Pair<Class, String>, Method> fieldsToTriggers = new HashSetValuedHashMap<>();
    public final ConcurrentHashMap<String, Class<?>> fieldsToTypes = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, String> aliasesToFields = new ConcurrentHashMap<>();

    public final ConcurrentHashMap<Class<? extends Annotation>, Annotation> annotations = new ConcurrentHashMap<>();

    public static final ModelBinding EMPTY_BINDING = new ModelBinding();

    /* empty binding constructor */
    private ModelBinding() {
        jsonApiType = null;
        idField = null;
        modelIdType = null;
        attributes = null;
        relationships = null;
        modelType = null;
        entityPermissions = EntityPermissions.EMPTY_PERMISSIONS;
    }

    public ModelBinding(EntityDictionary dictionary, Class<?> cls, String type) {
        modelType = cls;
        jsonApiType = type;

        // Map id's, attributes, and relationships
        List<AccessibleObject> fieldOrMethodList = new ArrayList<>();
        fieldOrMethodList.addAll(Arrays.asList(cls.getFields()));
        fieldOrMethodList.addAll(Arrays.asList(cls.getMethods()));

        bindEntityFields(cls, type, fieldOrMethodList);

        attributes = dequeToList(attributesDeque);
        relationships = dequeToList(relationshipsDeque);
        entityPermissions = new EntityPermissions(dictionary, cls, fieldOrMethodList);
    }

    /**
     * Bind fields of an entity including the Id field, attributes, and relationships.
     *
     * @param cls Class type to bind fields
     * @param type JSON API type identifier
     * @param fieldOrMethodList List of fields and methods on entity
     */
    private void bindEntityFields(Class<?> cls, String type, Collection<AccessibleObject> fieldOrMethodList) {

        for (AccessibleObject fieldOrMethod : fieldOrMethodList) {
            if (fieldOrMethod.isAnnotationPresent(Id.class)) {
                bindEntityId(cls, type, fieldOrMethod);
            } else if (fieldOrMethod.isAnnotationPresent(Transient.class)
                    && !fieldOrMethod.isAnnotationPresent(ComputedAttribute.class)
                    && !fieldOrMethod.isAnnotationPresent(ComputedRelationship.class)) {
                continue; // Transient. Don't serialize
            } else if (!fieldOrMethod.isAnnotationPresent(Exclude.class)) {
                if (fieldOrMethod instanceof Field && Modifier.isTransient(((Field) fieldOrMethod).getModifiers())) {
                    continue; // Transient. Don't serialize
                }
                if (fieldOrMethod instanceof Method && Modifier.isTransient(((Method) fieldOrMethod).getModifiers())) {
                    continue; // Transient. Don't serialize
                }
                if (fieldOrMethod instanceof Field
                        && !fieldOrMethod.isAnnotationPresent(Column.class)
                        && Modifier.isStatic(((Field) fieldOrMethod).getModifiers())) {
                    continue; // Field must have Column annotation?
                }
                bindAttrOrRelation(cls, fieldOrMethod);
            }
        }
    }

    /**
     * Bind an id field to an entity.
     *
     * @param cls Class type to bind fields
     * @param type JSON API type identifier
     * @param fieldOrMethod Field or method to bind
     */
    private void bindEntityId(Class<?> cls, String type, AccessibleObject fieldOrMethod) {
        String fieldName = getFieldName(fieldOrMethod);
        Class<?> fieldType = getFieldType(fieldOrMethod);

        //Add id field to type map for the entity
        fieldsToTypes.put(fieldName, fieldType);

        //Set id field, type, and name
        idField = fieldOrMethod;
        modelIdType = fieldType;
        idFieldName = fieldName;

        fieldsToValues.put(fieldName, fieldOrMethod);

        if (idField != null && !fieldOrMethod.equals(idField)) {
            throw new DuplicateMappingException(type + " " + cls.getName() + ":" + fieldName);
        }
    }

    /**
     * Convert a deque to a list.
     *
     * @param deque Deque to convert
     * @return Deque as a list
     */
    private static List<String> dequeToList(final Deque<String> deque) {
        ArrayList<String> result = new ArrayList<>();
        deque.stream().forEachOrdered(result::add);
        result.sort(String.CASE_INSENSITIVE_ORDER);
        return Collections.unmodifiableList(result);
    }

    /**
     * Bind an attribute or relationship.
     *
     * @param cls Class type to bind fields
     * @param fieldOrMethod Field or method to bind
     */
    private void bindAttrOrRelation(Class<?> cls, AccessibleObject fieldOrMethod) {
        boolean manyToMany = fieldOrMethod.isAnnotationPresent(ManyToMany.class);
        boolean manyToOne = fieldOrMethod.isAnnotationPresent(ManyToOne.class);
        boolean oneToMany = fieldOrMethod.isAnnotationPresent(OneToMany.class);
        boolean oneToOne = fieldOrMethod.isAnnotationPresent(OneToOne.class);
        boolean computedRelationship = fieldOrMethod.isAnnotationPresent(ComputedRelationship.class);
        boolean isRelation = manyToMany || manyToOne || oneToMany || oneToOne;

        String fieldName = getFieldName(fieldOrMethod);

        if (fieldName == null || fieldName.equals("id")
                || fieldName.equals("class") || OBJ_METHODS.contains(fieldOrMethod)) {
            return; // Reserved. Not attributes.
        }

        Class<?> fieldType = getFieldType(fieldOrMethod);

        ConcurrentLinkedDeque<String> fieldList;
        if (isRelation) {
            fieldList = relationshipsDeque;
            RelationshipType type;
            String mappedBy;
            CascadeType [] cascadeTypes;
            if (oneToMany) {
                type = computedRelationship ? RelationshipType.COMPUTED_ONE_TO_MANY : RelationshipType.ONE_TO_MANY;
                mappedBy = fieldOrMethod.getAnnotation(OneToMany.class).mappedBy();
                cascadeTypes = fieldOrMethod.getAnnotation(OneToMany.class).cascade();
            } else if (oneToOne) {
                type = computedRelationship ? RelationshipType.COMPUTED_ONE_TO_ONE : RelationshipType.ONE_TO_ONE;
                mappedBy = fieldOrMethod.getAnnotation(OneToOne.class).mappedBy();
                cascadeTypes = fieldOrMethod.getAnnotation(OneToOne.class).cascade();
            } else if (manyToMany) {
                type = computedRelationship ? RelationshipType.COMPUTED_MANY_TO_MANY : RelationshipType.MANY_TO_MANY;
                mappedBy = fieldOrMethod.getAnnotation(ManyToMany.class).mappedBy();
                cascadeTypes = fieldOrMethod.getAnnotation(ManyToMany.class).cascade();
            } else if (manyToOne) {
                type = computedRelationship ? RelationshipType.COMPUTED_MANY_TO_ONE : RelationshipType.MANY_TO_ONE;
                mappedBy = "";
                cascadeTypes = fieldOrMethod.getAnnotation(ManyToOne.class).cascade();
            } else {
                type = computedRelationship ? RelationshipType.COMPUTED_NONE : RelationshipType.NONE;
                mappedBy = "";
                cascadeTypes = new CascadeType[0];
            }
            relationshipTypes.put(fieldName, type);
            relationshipToInverse.put(fieldName, mappedBy);
            relationshipToCascadeTypes.put(fieldName, cascadeTypes);
        } else {
            fieldList = attributesDeque;
        }

        fieldList.push(fieldName);
        fieldsToValues.put(fieldName, fieldOrMethod);
        fieldsToTypes.put(fieldName, fieldType);
    }

    /**
     * Returns name of field whether public member or method.
     *
     * @param fieldOrMethod field or method
     * @return field or method name
     */
    public static String getFieldName(AccessibleObject fieldOrMethod) {
        if (fieldOrMethod instanceof Field) {
            return ((Field) fieldOrMethod).getName();
        } else {
            Method method = (Method) fieldOrMethod;
            String name = method.getName();

            if (name.startsWith("get") && method.getParameterCount() == 0) {
                name = WordUtils.uncapitalize(name.substring("get".length()));
            } else if (name.startsWith("is") && method.getParameterCount() == 0) {
                name = WordUtils.uncapitalize(name.substring("is".length()));
            } else {
                return null;
            }
            return name;
        }
    }

    /**
     * Returns type of field whether public member or method.
     *
     * @param fieldOrMethod field or method
     * @return field type
     */
    private static Class<?> getFieldType(AccessibleObject fieldOrMethod) {
        if (fieldOrMethod instanceof Field) {
            return ((Field) fieldOrMethod).getType();
        } else {
            return ((Method) fieldOrMethod).getReturnType();
        }
    }


    public <A extends Annotation> Collection<Method> getTriggers(Class<A> annotationClass, String fieldName) {
        Collection<Method> methods = fieldsToTriggers.get(Pair.of(annotationClass, fieldName));
        return methods == null ? Collections.emptyList() : methods;
    }

    /**
     * Cache placeholder for no annotation.
     */
    private static final Annotation NO_ANNOTATION = new Annotation() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    };

    /**
     * Return annotation from class, parents or package.
     *
     * @param annotationClass the annotation class
     * @param <A> annotation type
     * @return the annotation
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        Annotation annotation = annotations.get(annotationClass);
        if (annotation == null) {
            annotation = EntityDictionary.getFirstAnnotation(modelType, Collections.singletonList(annotationClass));
            if (annotation == null) {
                annotation = NO_ANNOTATION;
            }
            annotations.putIfAbsent(annotationClass, annotation);
        }
        return annotation == NO_ANNOTATION ? null : (A) annotation;
    }
}
