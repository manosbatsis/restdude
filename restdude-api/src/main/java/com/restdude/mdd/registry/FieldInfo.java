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

import java.util.Optional;

/**
 * Contains metadata for a specific model field
 */
public interface FieldInfo {

    String getFieldName();

    Class<?> getFieldType();

    FieldMappingType getFieldMappingType();

    Class<?> getFieldModelType();

    Optional<String> getReverseFieldName();
    void setReverseFieldName(String reverseFieldName);

    javax.persistence.CascadeType[] getCascadeTypes();

    java.lang.reflect.Method getGetterMethod();

    java.lang.reflect.Method getSetterMethod();

    boolean isGetter();

    boolean isSetter();

    boolean isLazy();

    ModelInfo getRelatedModelInfo();
    void setRelatedModelInfo(ModelInfo related);

    boolean isLinkableResource();

    boolean isInverse();
}
