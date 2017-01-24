/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.mdd.specifications;

import com.restdude.domain.base.model.CalipsoPersistable;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;


public class SpecificationsBuilder<T extends CalipsoPersistable<ID>, ID extends Serializable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationsBuilder.class);
    private Class<T> domainClass;


    public SpecificationsBuilder(Class<T> domainClass2) {
        this.domainClass = domainClass;
    }

    /**
     * Dynamically create specification for the given class and search
     * parameters. This is the entry point for query specifications construction
     * by repositories.
     *
     * @param domainClass the entity type to query for
     * @param searchTerms the search terms to match
     * @return the result specification
     */
    public Specification<T> getMatchAll(final Class<T> domainClass, final Map<String, String[]> searchTerms) {

        LOGGER.debug("getMatchAll, entity: {}, searchTerms: {}", domainClass.getSimpleName(), searchTerms);

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(@SuppressWarnings("rawtypes") Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return buildRootPredicate(domainClass, searchTerms, root, cb);
            }
        };
    }

    /**
     * Get the root predicate, either a conjunction or disjunction
     * @param clazz the entity type to query for
     * @param searchTerms the search terms to match
     * @param root the criteria root
     * @param cb the criteria builder
     * @return the resulting predicate
     */
    protected Predicate buildRootPredicate(Class<T> clazz, final Map<String, String[]> searchTerms,
                                           Root<T> root, CriteriaBuilder cb) {

        LOGGER.debug("buildRootPredicate, clazz: {}, searchTerms: {}", clazz, searchTerms);
        Map<String, String[]> normalizedSearchTerms = new HashMap<>();
        Iterator<String> keyIterator = searchTerms.keySet().iterator();

        String propertyName;
        String newPropertyName;
        while (keyIterator.hasNext()) {
            propertyName = keyIterator.next();
            newPropertyName = propertyName;
            if (propertyName.endsWith(".pk")) {
                newPropertyName = propertyName.substring(0, propertyName.length() - 3);
            }
            normalizedSearchTerms.put(newPropertyName, searchTerms.get(propertyName));
        }

        LOGGER.debug("buildRootPredicate, normalizedSearchTerms: {}", normalizedSearchTerms);

        // build a list of criteria/predicates
        LinkedList<Predicate> predicates = buildSearchPredicates(clazz, normalizedSearchTerms, root, cb);
        LOGGER.debug("buildRootPredicate, predicates: {}", predicates);

        // wrap list in AND/OR junction
        Predicate predicate;
        if (searchTerms.containsKey(GenericSpecifications.SEARCH_MODE) && searchTerms.get(GenericSpecifications.SEARCH_MODE)[0].equalsIgnoreCase(GenericSpecifications.OR)
                // A disjunction of zero predicates is false so...
                && predicates.size() > 0) {
            predicate = cb.or(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            predicate = cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        // return the resulting junction
        return predicate;
    }

    /**
     * Build the list of predicates corresponding to the given search terms
     * @param domainClass the entity type to query for
     * @param searchTerms the search terms to match
     * @param root the criteria root
     * @param cb the criteria builder
     * @return the list of predicates corresponding to the search terms
     */
    protected LinkedList<Predicate> buildSearchPredicates(final Class<T> domainClass, final Map<String, String[]> searchTerms, Root<T> root, CriteriaBuilder cb) {

        LOGGER.debug("buildSearchPredicates, domainClass: {}, searchTerms: {}", domainClass, searchTerms);
        LinkedList<Predicate> predicates = new LinkedList<Predicate>();

        if (!MapUtils.isEmpty(searchTerms)) {
            Set<String> propertyNames = searchTerms.keySet();
            for (String propertyName : propertyNames) {
                String[] values = searchTerms.get(propertyName);
                addPredicate(domainClass, root, cb, predicates, values, propertyName);
            }
        }
        // return the list of predicates
        return predicates;
    }


    /**
     * Add a predicate to the given list if valid
     * @param domainClass the entity type to query for
     * @param root the criteria root
     * @param cb the criteria builder
     * @param predicates the list to add the predicate into
     * @param propertyValues the predicate values
     * @param propertyName the predicate name
     */
    protected void addPredicate(Class<T> domainClass, Root<T> root, CriteriaBuilder cb,
                                LinkedList<Predicate> predicates, String[] propertyValues, String propertyName) {

        LOGGER.debug("addPredicate, domainClass: {}, propertyName: {}", domainClass, propertyName);
        Class fieldType = GenericSpecifications.getMemberType(domainClass, propertyName);
        IPredicateFactory predicateFactory = null;
        if (fieldType != null) {

            LOGGER.debug("addPredicate, found field type for domainClass: {}, propertyName: {}, fieldType: {}", domainClass, propertyName, fieldType);
            predicateFactory = GenericSpecifications.getPredicateFactoryForClass(fieldType);
            if (predicateFactory != null) {
                LOGGER.debug("addPredicate, found predicate factory: {}", predicateFactory);
                predicates.add(predicateFactory.getPredicate(root, cb, propertyName, fieldType, propertyValues));
            } else {
                LOGGER.debug("addPredicate, could not find predicate factory");
            }

        } else {
            LOGGER.debug("addPredicate, field type not found for domainClass: {}, propertyName: {}, fieldType: {}", domainClass, propertyName, fieldType);
        }

    }

}
