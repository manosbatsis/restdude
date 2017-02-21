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

import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.model.Model;
import com.restdude.mdd.specifications.IPredicateFactory;
import com.restdude.mdd.util.EntityUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.hateoas.ExposesResourceFor;

import javax.persistence.Entity;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains metadata for a specific Model class.
 */
@Slf4j
public class ModelInfoImpl<T extends Model<PK>, PK extends Serializable> implements ModelInfo<T, PK> {

    @Getter private final Class<T> modelType;
    @Getter private final String packageName;
    @Getter private final String beansBasePackage;

    @Getter private final String uriComponent;
    @Getter private final String parentApplicationPath;
    @Getter private final String basePath;
    @Getter private final boolean jpaEntity;
    @Getter private FieldInfo idField;
    @Getter private final Set<String> allFieldNames = new HashSet<>();
    @Getter private final Set<String> simpleFieldNames = new HashSet<>();
    @Getter private final Set<String> toOneFieldNames = new HashSet<>();
    @Getter private final Set<String> toManyFieldNames = new HashSet<>();

    private final ConcurrentHashMap<String, FieldInfo> fields = new ConcurrentHashMap<>();

    @Getter @Setter
    private IPredicateFactory predicateFactory;
    @Getter @Setter
    private Class<?> modelControllerType;
    private Boolean linkableResource;


    public ModelInfoImpl(@NonNull Class<T> modelType) {

        // add basic info
        this.modelType = modelType;
        this.packageName = modelType.getPackage().getName();
        this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        this.jpaEntity = modelType.isAnnotationPresent(Entity.class);

        // add endpoint info
        ModelResource ar = modelType.getAnnotation(ModelResource.class);
        this.uriComponent = buildUriComponent();
        if(ar != null){
            this.basePath = ar.basePath();
            this.parentApplicationPath = ar.parentPath();
        }
        else{
            this.basePath = "";
            this.parentApplicationPath = "";
        }

        // add fields info
        BeanInfo componentBeanInfo = EntityUtil.getBeanInfo(modelType);
        PropertyDescriptor[] properties = componentBeanInfo.getPropertyDescriptors();
        for (int p = 0; p < properties.length; p++) {
            log.debug("ModelInfo, property: '{}'", properties[p]);
            if(!"class".equals(properties[p].getName())){
                FieldInfo fieldInfo = FieldInfoImpl.create(modelType, properties[p]);
                if(fieldInfo != null){
                    this.fields.put(fieldInfo.getFieldName(), fieldInfo);
                    if(fieldInfo.getFieldMappingType().isId()){
                        this.idField = fieldInfo;
                    }
                    else if(fieldInfo.getFieldMappingType().isSimple()){
                        this.simpleFieldNames.add(fieldInfo.getFieldName());
                    }
                    else if(fieldInfo.getFieldMappingType().isToOne()){
                        this.toOneFieldNames.add(fieldInfo.getFieldName());
                    }
                    else if(fieldInfo.getFieldMappingType().isToMany()){
                        this.toManyFieldNames.add(fieldInfo.getFieldName());
                    }
                }
            }
        }

        log.debug("ModelInfo, domainClass: {}, idField: {}", modelType, this.idField);
    }

    protected String buildUriComponent() {
        ModelResource meta = this.getModelType().getAnnotation(ModelResource.class);
        String endpointPathName = meta != null ? meta.pathFragment() : null;
        if(StringUtils.isBlank(endpointPathName)){
            endpointPathName = this.getModelType().getSimpleName();
            endpointPathName = endpointPathName.toLowerCase().charAt(0) + endpointPathName.substring(1) + "s";
        }
        return endpointPathName;
    }

    @Override
    public String getParentPath(String defaultValue) {
        return StringUtils.isNotEmpty(this.parentApplicationPath) ? parentApplicationPath : defaultValue;
    }

    @Override
    public String getBasePath(String defaultValue) {
        return StringUtils.isNotEmpty(this.basePath) ? basePath : defaultValue;
    }

    @Override
    public FieldInfo getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    @Override
    public Boolean isLinkableResource(){
        if(this.linkableResource == null){
            boolean hasController = this.modelControllerType != null;
            ExposesResourceFor exposesResourceFor = hasController ? this.modelControllerType.getAnnotation(ExposesResourceFor.class) : null;
            log.debug("isLinkableResource, name: {}, modelControllerType: {}, exposes resources: {}", this.getModelType(), modelControllerType, exposesResourceFor);
            if(exposesResourceFor != null){
                this.linkableResource = Boolean.TRUE;
            }
            else{
                this.linkableResource = Boolean.FALSE;
            }
        }
        return this.linkableResource;
    }

}
