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
package com.restdude.domain.base.annotation.controller;

import com.restdude.domain.base.annotation.model.ModelResource;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ResponseBody
public @interface ModelController {

    /**
     * Use in your @RequestMapping path to have replaced with your common API base path,
     * i.e. the pathFragment specified by the restdude.api.basePath application configuration property
     *
     * <pre>
     * {@code
     *
     * @RequestMapping(ModelController.BASE_PATH_DEFAULT + "/parent/path/myModels")
     * public class MyModelController{
     *    //...
     * }
     *
     * }
     * </pre>
     *
     */
    public static final String BASE_PATH_WILDCARD = "{BASE_PATH_WILDCARD}";

    /**
     * Use in your @RequestMapping path to have replaced with your Model's default parent application path,
     * i.e. it's {@link ModelResource#parentPath()} annotation configuration
     *
     * <pre>
     * {@code
     *
     * @RequestMapping("/base/path" + ModelController.APP_PARENT_PATH_WILDCARD + "/myModels")
     * public class MyModelController{
     *    //...
     * }
     *
     * }
     * </pre>
     *
     */
    public static final String APP_PARENT_PATH_WILDCARD = "{APP_PARENT_PATH_WILDCARD}";

    /**
     * Use in your @RequestMapping path to have replaced with your Model's default URI component
     * i.e. it's {@link ModelResource#pathFragment()} annotation configuration
     *
     * <pre>
     * {@code
     *
     * @RequestMapping("/base/path/parent/path" + ModelController.MODEL_URI_COMPONENT_WILDCARD)
     * public class MyModelController{
     *    //...
     * }
     *
     * }
     * </pre>
     *
     */
    public static final String MODEL_URI_COMPONENT_WILDCARD = "{MODEL_URI_COMPONENT_WILDCARD}";

    /**
     * Use in your @RequestMapping path to have replaced with a path composed by your API base path, 
     * the Model's default parent path and the Model's default URI fragment. Used by default in 
     * controllers that generated or otherwise annotated with {@link ModelController} , in other words:
     *
     * <pre>
     * {@code
     *
     * @ModelController
     * public class MyModelController{
     *    //...
     * }
     *
     * }
     * </pre>
     *
     * is equivalent to:
     *
     * <pre>
     * {@code
     *
     * @RestController
     * @RequestMapping(ModelController.BASE_PATH_DEFAULT)
     * public class MyModelController{
     *    //...
     * }
     *
     * }
     * </pre>
     *
     */
    public static final String MODEL_MAPPING_WILDCARD = "{MODEL_MAPPING_WILDCARD}";

    /**
     * The pathFragment may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * Equivalent via {@link AliasFor} to  {@link Component#value()}
     * @return the suggested component name, if any
     * @since 4.0.1
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

}
