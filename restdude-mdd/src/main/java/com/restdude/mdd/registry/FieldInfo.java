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
package com.restdude.mdd.registry;

import com.yahoo.elide.annotation.ComputedRelationship;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains metadata for a specific model field
 */
@Slf4j
public class FieldInfo {


    public static FieldInfo create(@NonNull Class<?> modelType,  PropertyDescriptor property) {
        FieldInfo fieldInfo = null;
        Field field = FieldUtils.getField(modelType, property.getName(), true);

        // ensure proper bean property, i.e. read/write
        Method getter = property.getReadMethod();
        Method setter = property.getWriteMethod();
        if(field != null && getter != null && setter != null) {
            log.debug("create, modelType: {}, property: {}", modelType, property);
            fieldInfo = new FieldInfo(modelType, property, field, getter, setter);
        }
        else{
            log.debug("create, ignoring modelType: {}, property: {}", modelType, property);
        }
        return fieldInfo;

    }

    @Getter private final String fieldName;
    @Getter private final Class<?> fieldType;
    @Getter private FieldMappingType fieldMappingType;
    @Getter private Class<?> fieldModelType;
    @Getter private String mappedBy;
    @Getter private CascadeType[] cascadeTypes;
    @Getter private boolean getter;
    @Getter private boolean setter;

    private FieldInfo(@NonNull Class<?> modelType, @NonNull PropertyDescriptor property, @NonNull Field field, @NonNull Method getter, @NonNull Method setter) {
        // add basic info
        this.fieldType = property.getPropertyType();
        this.fieldName = property.getName();


        this.getter = getter != null;
        this.setter = setter != null;

        scanMappings(field, getter, setter);
    }



    private void scanMappings(@NonNull AccessibleObject... fieldsOrMethods){

        // scan for JPA annotations
        //
        Optional<Id> id = Optional.empty();
        Optional<EmbeddedId> embeddedId = Optional.empty();
        Optional<ManyToMany> manyToMany = Optional.empty();
        Optional<ManyToOne> manyToOne = Optional.empty();
        Optional<OneToMany> oneToMany = Optional.empty();
        Optional<OneToOne> oneToOne = Optional.empty();
        Optional<ComputedRelationship> computedRelationship = Optional.empty();
        Optional<Formula> formula = Optional.empty();
        Optional<Transient> tranzient = Optional.empty();

        for(AccessibleObject field : fieldsOrMethods) {
            log.debug("scanMappings, field: {}", field);
            if (field.isAnnotationPresent(Id.class)) {
                id = Optional.ofNullable(field.getAnnotation(Id.class));
                log.debug("scanMappings, found Id field: {}", field);
            }
            if (field.isAnnotationPresent(EmbeddedId.class)) {
                embeddedId = Optional.ofNullable(field.getAnnotation(EmbeddedId.class));
                log.debug("scanMappings, found Id field: {}", field);
            }
            if (field.isAnnotationPresent(ManyToMany.class)) {
                manyToMany = Optional.ofNullable(field.getAnnotation(ManyToMany.class));
            }
            if (field.isAnnotationPresent(ManyToOne.class)) {
                manyToOne = Optional.ofNullable(field.getAnnotation(ManyToOne.class));
            }
            if (field.isAnnotationPresent(OneToMany.class)) {
                oneToMany = Optional.ofNullable(field.getAnnotation(OneToMany.class));
            }
            if (field.isAnnotationPresent(OneToOne.class)) {
                oneToOne = Optional.ofNullable(field.getAnnotation(OneToOne.class));
            }
            if (field.isAnnotationPresent(ComputedRelationship.class)) {
                computedRelationship = Optional.ofNullable(field.getAnnotation(ComputedRelationship.class));
            }
            if (field.isAnnotationPresent(Transient.class)) {
                tranzient = Optional.ofNullable(field.getAnnotation(Transient.class));
            }
        }

        // process field
        boolean isRelation = Stream.of(manyToMany, manyToOne, oneToMany, oneToOne, computedRelationship).anyMatch(x -> x.isPresent());
        if(this.fieldName != null && !this.fieldName.equals("class")) {

            if(id.isPresent() || embeddedId.isPresent()){
                fieldMappingType = FieldMappingType.ID;
            }
            else if(tranzient.isPresent()){
                fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_NONE : FieldMappingType.NONE;
            }
            else if(isRelation) {
                if(oneToMany.isPresent()) {
                    fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_ONE_TO_MANY : FieldMappingType.ONE_TO_MANY;
                    mappedBy = oneToMany.get().mappedBy();
                    cascadeTypes = oneToMany.get().cascade();
                } else if(oneToOne.isPresent()) {
                    fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_ONE_TO_ONE : FieldMappingType.ONE_TO_ONE;
                    mappedBy = oneToOne.get().mappedBy();
                    cascadeTypes = oneToOne.get().cascade();
                } else if(manyToMany.isPresent()) {
                    fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_MANY_TO_MANY : FieldMappingType.MANY_TO_MANY;
                    mappedBy = manyToMany.get().mappedBy();
                    cascadeTypes = manyToMany.get().cascade();
                } else if(manyToOne.isPresent()) {
                    fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_MANY_TO_ONE : FieldMappingType.MANY_TO_ONE;
                    mappedBy = "";
                    cascadeTypes = manyToOne.get().cascade();
                } else {
                    fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_NONE :FieldMappingType.NONE;
                    mappedBy = "";
                    cascadeTypes = new CascadeType[0];
                }
            }
            else if(formula.isPresent()){
                fieldMappingType = FieldMappingType.CALCULATED_SIMPLE;
            }
            else {
                // TODO: simple, formula, list of values
                fieldMappingType = computedRelationship.isPresent() ? FieldMappingType.CALCULATED_SIMPLE :FieldMappingType.SIMPLE;

            }
        }


    }

}
