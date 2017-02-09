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

import com.restdude.domain.base.annotation.model.ModelResource;
import com.restdude.mdd.specifications.IPredicateFactory;
import com.restdude.util.ClassUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Contains metadata for a specific Model class.
 */
public class ModelInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelInfo.class);

    @Getter
    private final Class<?> modelType;
    @Getter
    private final String packageName;
    @Getter
    private final String beansBasePackage;
    @Getter
    private final Class<?> identifierType;

    @Getter
    private final String uriComponent;
    private final String parentPath;
    private final String basePath;

    @Getter @Setter
    private IPredicateFactory predicateFactory;
    @Getter @Setter
    private Class<?> modelControllerType;


    public ModelInfo(Class<?> modelType) {
        Assert.notNull(modelType, "Model type is required");

        // add basic info
        this.modelType = modelType;
        this.packageName = modelType.getPackage().getName();
        this.beansBasePackage = packageName.endsWith(".model") ? packageName.substring(0, packageName.indexOf(".model")) : packageName;
        this.identifierType = ClassUtils.getBeanPropertyType(modelType, "pk", true);

        LOGGER.debug("ModelInfo, domainClass: {}, identifierType: {}", modelType, identifierType);
        // add endpoint info
        ModelResource ar = modelType.getAnnotation(ModelResource.class);
        //ModelRelatedResource anr = domainClass.getAnnotation(ModelRelatedResource.class);

        this.uriComponent = buildUriComponent();
        if(ar != null){
            this.basePath = ar.basePath();
            this.parentPath = ar.parentPath();
        }
        else{
            this.basePath = "";
            this.parentPath = "";
        }
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

    public String getParentPath(String defaultValue) {
        return StringUtils.isNotEmpty(this.parentPath) ? parentPath : defaultValue;
    }

    public String getBasePath(String defaultValue) {
        return StringUtils.isNotEmpty(this.basePath) ? basePath : defaultValue;
    }
}
