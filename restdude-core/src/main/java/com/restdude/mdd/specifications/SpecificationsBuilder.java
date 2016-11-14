package com.restdude.mdd.specifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class SpecificationsBuilder<T, ID extends Serializable>{

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
	public Specification<T> getMatchAll(Class<T> domainClass, final Map<String, String[]> searchTerms) {

		LOGGER.debug("matchAll, entity: {}, searchTerms: {}" , domainClass.getSimpleName(), searchTerms);
	
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
		
		// build a list of criteria/predicates
		LinkedList<Predicate> predicates = buildSearchPredicates(clazz, searchTerms, root, cb);
		
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
	 * @param clazz the entity type to query for
	 * @param searchTerms the search terms to match
	 * @param root the criteria root
	 * @param cb the criteria builder
	 * @return the list of predicates corresponding to the search terms
	 */
	protected LinkedList<Predicate> buildSearchPredicates(Class<T> domainClass, final Map<String, String[]> searchTerms, Root<T> root, CriteriaBuilder cb) {
		
		LinkedList<Predicate> predicates = new LinkedList<Predicate>();
		
		if (!CollectionUtils.isEmpty(searchTerms)) {
			Set<String> propertyNames = searchTerms.keySet();
			// storage for nested junctions
			NestedJunctions junctions = new NestedJunctions();
			for (String propertyName : propertyNames) {
				String[] values = searchTerms.get(propertyName);
				// store if nested junction or add a predicate
				if (!junctions.addIfNestedJunction(propertyName, values)) {
					addPredicate(domainClass, root, cb, predicates, values, propertyName);
				}
			}
			// add stored junctions
			Map<String, Map<String, String[]>> andJunctions = junctions.getAndJunctions();
			addNestedJunctionPredicates(domainClass, root, cb, predicates, andJunctions, GenericSpecifications.AND);
			Map<String, Map<String, String[]>> orJunctions = junctions.getOrJunctions();
			addNestedJunctionPredicates(domainClass, root, cb, predicates, orJunctions, GenericSpecifications.OR);
		}
		// return the list of predicates
		return predicates;
	}
	


	/**
	 * Add a predicate to the given list if valid
	 * @param clazz the entity type to query for
	 * @param root the criteria root
	 * @param cb the criteria builder
	 * @param predicates the list to add the predicate into
	 * @param propertyValues the predicate values
	 * @param propertyName the predicate name
	 */
	protected void addPredicate(Class<T> domainClass, Root<T> root, CriteriaBuilder cb,
			LinkedList<Predicate> predicates, String[] propertyValues, String propertyName) {
		// dot notation only supports toOne.toOne.id
		if (propertyName.contains(".")) {
			predicates.add(GenericSpecifications.anyToOneToOnePredicateFactory.getPredicate(root, cb, propertyName, null, propertyValues));
		} else {// normal single step predicate
			Field field = GenericSpecifications.getField(domainClass, propertyName);
			if (field != null) {
				Class fieldType = field.getType();
				IPredicateFactory predicateFactory = GenericSpecifications.getPredicateFactoryForClass(field);
				if (predicateFactory != null) {
					predicates.add(predicateFactory.getPredicate(root, cb, propertyName, fieldType, propertyValues));
				}
			}
		}
	}
	
	//TODO refactor nested junctions and add operators
	protected void addNestedJunctionPredicates(Class<T> domainClass, Root<T> root, CriteriaBuilder cb,
			LinkedList<Predicate> predicates, Map<String, Map<String, String[]>> andJunctions, String mode) {
		if (!CollectionUtils.isEmpty(andJunctions)) {
			String[] searchMode = { mode };
			for (Map<String, String[]> params : andJunctions.values()) {
				params.put(GenericSpecifications.SEARCH_MODE, searchMode);
				// TODO
				Predicate nestedPredicate = buildRootPredicate(domainClass, params, root, cb/*, true*/);
				if (nestedPredicate != null) {
					predicates.add(nestedPredicate);
				}
			}
		}
	}
}
