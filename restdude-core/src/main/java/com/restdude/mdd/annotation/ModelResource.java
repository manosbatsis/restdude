/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.annotation;

import com.restdude.domain.base.controller.AbstractNoDeleteModelController;
import com.restdude.domain.base.controller.ModelController;

import java.io.Serializable;
import java.lang.annotation.*;


//Make the annotation available at runtime:
@Retention(RetentionPolicy.RUNTIME)
//Allow to use only on types:
@Target(ElementType.TYPE)
@Documented
/**
 * <p>Marks a Model as candidate for tiers code generation (Controller, Service, Repository)</p>
 * 
 *  <p>For example:</p>
 *  
 *  <pre class="code">
 * &#064;ModelResource(
 * 		path = "countries", 
 * 		apiName = "Countries", 
 * 		apiDescription = "Operations about countries", 
 * 		controllerSuperClass = AbstractModelController.class
 * 	)
 * </pre>
 * 
 *  <p>Will generate the following controller:</p>
 *  
 *  <pre class="code">
 * 
 * import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 * import org.springframework.stereotype.Controller;
 * import org.springframework.web.bind.annotation.RequestMapping;
 * 
 * import gr.abiss.calipso.model.geography.Country;
 * import com.restdude.domain.geography.service.CountryService;
 * import com.restdude.domain.base.controller.AbstractModelController;
 * import io.swagger.annotations.Api;
 * 
 * &#064;Controller
 * &#064;Api(tags = "Countries", description = "Operations about countries")
 * &#064;RequestMapping(
 *  value = "/api/rest/countries", 
 * 	produces = { "application/json", "application/xml" }
 * )
 * public class CountryController extends AbstractModelController<Country, String, CountryService> {
 * 
 * 		private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);
 * 
 * }
 * </pre>
 * 
 * 
 * 
 * 
 */
public @interface ModelResource {

    Class<? extends Serializable> idClass() default String.class;
    /**
     * The superclass for the generated controller. must implement 
     * {@link ModelController}. The default
     * is {@link AbstractNoDeleteModelController}.
     */
    @SuppressWarnings("rawtypes")
	Class<? extends ModelController> controllerSuperClass() default AbstractNoDeleteModelController.class;

    /**
     * The request mapping path for the generated controller
     */
    String path();
    /**
     * 
     * The API (grouping) name for the generated controller. Used for swagger documentation.
     */
    String apiName() default "";

    /**
     * 
     * The API description for the generated controller. Used for swagger documentation.
     */
    String apiDescription() default "";
    
    /**
     * 
     * Hint to enable (javers auditing b
     */
    boolean auditable() default false;
    
    
}
