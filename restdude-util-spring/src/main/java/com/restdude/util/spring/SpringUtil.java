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
package com.restdude.util.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by manos on 28/4/2017.
 */
public class SpringUtil {

    public static ClassPathScanningCandidateComponentProvider createComponentScanner(Class... annotations) {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        for (Class annotation : annotations) {
            provider.addIncludeFilter(new AnnotationTypeFilter(annotation));
        }
        return provider;
    }

    public static Set<BeanDefinition> findCandidateComponents(Iterable<String> basePackages, Class... annotations) {
        ClassPathScanningCandidateComponentProvider provider = SpringUtil.createComponentScanner(annotations);
        Set<BeanDefinition> entities = new HashSet<>();
        for (String basePackage : basePackages) {
            entities.addAll(provider.findCandidateComponents(basePackage));
        }
        return entities;
    }
}
