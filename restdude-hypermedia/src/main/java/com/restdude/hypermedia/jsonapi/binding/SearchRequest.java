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
package com.restdude.hypermedia.jsonapi.binding;


import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.core.exceptions.InvalidPredicateException;
import com.yahoo.elide.core.filter.dialect.*;
import com.yahoo.elide.core.filter.expression.FilterExpression;
import com.yahoo.elide.core.pagination.Pagination;
import com.yahoo.elide.core.sort.Sorting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

/**
 * Request scope object for relaying request-related data to various subsystems.
 */
@Slf4j
public class SearchRequest {
    @Getter private final EntityDictionary dictionary;
    @Getter private final Optional<MultivaluedMap<String, String>> queryParams;
    @Getter private final Map<String, Set<String>> sparseFields;
    @Getter private final Pagination pagination;
    @Getter private final Sorting sorting;
    @Getter private final String path;
    @Getter private final boolean useFilterExpressions;
    @Getter private final MultipleFilterDialect filterDialect;
    @Getter private final Map<String, FilterExpression> expressionsByType;

    /* Used to filter across heterogeneous types during the first load */
    private FilterExpression globalFilterExpression;


    @Override
    public String toString() {
        return "SearchRequest{" +
                "dictionary=" + dictionary +
                ", queryParams=" + queryParams +
                ", sparseFields=" + sparseFields +
                ", pagination=" + pagination +
                ", sorting=" + sorting +
                ", path='" + path + '\'' +
                ", useFilterExpressions=" + useFilterExpressions +
                ", filterDialect=" + filterDialect +
                ", expressionsByType=" + expressionsByType +
                ", globalFilterExpression=" + globalFilterExpression +
                '}';
    }

    /**
     *
     * @param path
     * @param dictionary
     * @param queryParams
     * @param useFilterExpressions
     */
    public SearchRequest(String path,
                        EntityDictionary dictionary,
                        MultivaluedMap<String, String> queryParams,
                        boolean useFilterExpressions) {
        this.path = path;
        this.dictionary = dictionary;


        List<JoinFilterDialect> joinFilterDialects = new ArrayList<>();
        joinFilterDialects.add(new DefaultFilterDialect(dictionary));
        List<SubqueryFilterDialect> subqueryFilterDialects = new ArrayList<>();
        subqueryFilterDialects.add(new DefaultFilterDialect(dictionary));
        this.filterDialect = new MultipleFilterDialect(joinFilterDialects, subqueryFilterDialects);

        this.useFilterExpressions = useFilterExpressions;

        this.globalFilterExpression = null;
        this.expressionsByType = new HashMap<>();
        this.queryParams = (queryParams == null || queryParams.size() == 0)
                ? Optional.empty()
                : Optional.of(queryParams);

        if (this.queryParams.isPresent()) {

            /* Extract any query param that starts with 'filter' */
            MultivaluedMap filterParams = getFilterParams(queryParams);

            String errorMessage = "";
            if (! filterParams.isEmpty()) {

                /* First check to see if there is a global, cross-type filter */
                try {
                    globalFilterExpression = filterDialect.parseGlobalExpression(path, filterParams);
                } catch (ParseException e) {
                    errorMessage = e.getMessage();
                }

                /* Next check to see if there is are type specific filters */
                try {
                    expressionsByType.putAll(filterDialect.parseTypedExpression(path, filterParams));
                } catch (ParseException e) {

                    /* If neither dialect parsed, report the last error found */
                    if (globalFilterExpression == null) {

                        if (errorMessage.isEmpty()) {
                            errorMessage = e.getMessage();
                        } else if (! errorMessage.equals(e.getMessage())) {

                            /* Combine the two different messages together */
                            errorMessage = errorMessage + "\n" + e.getMessage();
                        }

                        throw new InvalidPredicateException(errorMessage);
                    }
                }
            }

            this.sparseFields = parseSparseFields(queryParams);
            this.sorting = Sorting.parseQueryParams(queryParams);
            this.pagination = Pagination.parseQueryParams(queryParams);
        } else {
            this.sparseFields = Collections.emptyMap();
            this.sorting = Sorting.getDefaultEmptyInstance();
            this.pagination = Pagination.getDefaultPagination();
        }

    }

    /**
     * Parses queryParams and produces sparseFields map.
     * @param queryParams The request query parameters
     * @return Parsed sparseFields map
     */
    private static Map<String, Set<String>> parseSparseFields(MultivaluedMap<String, String> queryParams) {
        Map<String, Set<String>> result = new HashMap<>();

        for (Map.Entry<String, List<String>> kv : queryParams.entrySet()) {
            String key = kv.getKey();
            if (key.startsWith("fields[") && key.endsWith("]")) {
                String type = key.substring(7, key.length() - 1);

                LinkedHashSet<String> filters = new LinkedHashSet<>();
                for (String filterParams : kv.getValue()) {
                    Collections.addAll(filters, filterParams.split(","));
                }

                if (!filters.isEmpty()) {
                    result.put(type, filters);
                }
            }
        }

        return result;
    }

    /**
     * Get filter expression for a specific collection type.
     * @param type The name of the type
     * @return The filter expression for the given type
     */
    public Optional<FilterExpression> getFilterExpressionByType(String type) {
        return Optional.ofNullable(expressionsByType.get(type));
    }

    /**
     * Get the global/cross-type filter expression.
     * @param loadClass
     * @return The global filter expression evaluated at the first load
     */
    public Optional<FilterExpression> getLoadFilterExpression(Class<?> loadClass) {
        Optional<FilterExpression> globalFilterExpressionOptional = null;
        if (globalFilterExpression == null) {
            String typeName = dictionary.getJsonAliasFor(loadClass);
            globalFilterExpressionOptional =  getFilterExpressionByType(typeName);
        } else {
            globalFilterExpressionOptional = Optional.of(globalFilterExpression);
        }


        return globalFilterExpressionOptional;
    }

    /**
     * Extracts any query params that start with 'filter'.
     * @param queryParams
     * @return extracted filter params
     */
    private static MultivaluedMap<String, String> getFilterParams(MultivaluedMap<String, String> queryParams) {
        MultivaluedMap<String, String> returnMap = new MultivaluedHashMap<>();

        queryParams.entrySet()
                .stream()
                .filter((entry) -> entry.getKey().startsWith("filter"))
                .forEach((entry) -> {
                    returnMap.put(entry.getKey(), entry.getValue());
                });
        return returnMap;
    }

    /**
     * Whether or not to use Elide 3.0 filter expressions for DataStoreTransaction calls
     * @return
     */
    public boolean useFilterExpressions() {
        return useFilterExpressions;
    }
}
