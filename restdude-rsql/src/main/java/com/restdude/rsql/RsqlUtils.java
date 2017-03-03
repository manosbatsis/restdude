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
package com.restdude.rsql;

import com.restdude.specification.PredicateOperator;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by manos on 2/3/2017.
 */
public class RsqlUtils {

    private static final Map<ComparisonOperator, PredicateOperator> operatorMappings = new HashMap<>();
    static {
        operatorMappings.put(RSQLOperators.EQUAL, PredicateOperator.EQUAL);
        operatorMappings.put(RSQLOperators.GREATER_THAN, PredicateOperator.GREATER_THAN);
        operatorMappings.put(RSQLOperators.GREATER_THAN_OR_EQUAL, PredicateOperator.GREATER_THAN_OR_EQUAL);
        operatorMappings.put(RSQLOperators.IN, PredicateOperator.IN);
        operatorMappings.put(RSQLOperators.LESS_THAN, PredicateOperator.LESS_THAN);
        operatorMappings.put(RSQLOperators.LESS_THAN_OR_EQUAL, PredicateOperator.LESS_THAN_OR_EQUAL);
        operatorMappings.put(RSQLOperators.NOT_EQUAL, PredicateOperator.NOT_EQUAL);
        operatorMappings.put(RSQLOperators.NOT_IN, PredicateOperator.NOT_IN);
    }

    public static PredicateOperator toPredicateOperator(@NonNull ComparisonOperator comparisonOperator){
        return operatorMappings.get(comparisonOperator);
    }

    public static String toRsql(Map<String, String[]> params, String... ignoredNames){
        Set<String> uniqueNames;
        if(ignoredNames != null){
            uniqueNames = new HashSet<String>(Arrays.asList(ignoredNames));
        }
        else{
            uniqueNames = Collections.emptySet();
        }
        return toRsql(params, uniqueNames);
    }

    public static String toRsql(@NonNull Map<String, String[]> params, @NonNull Set<String> ignoredNames){
        StringBuffer rsql = new StringBuffer();
        // iterate parameters
        for(String paramName : params.keySet()){
            // if not reserved name
            if(!ignoredNames.contains(paramName)){
                // get val;ues
                String[] values = params.get(paramName);
                // ensure non-null values
                if(ArrayUtils.isNotEmpty(values)){
                    rsql.append(";").append(paramName);
                    // use equals/in operator for single/multiple values respectively
                    if(values.length == 1){
                        rsql.append("==").append(values[0]);
                    }
                    else{
                        rsql.append("=in=('").append(values[0]).append("'");
                        for(int i = 1; i < values.length; i++){
                            rsql.append(",'").append(values[i]).append("'");
                        }
                        rsql.append(")");
                    }
                }

            }
        }
        String rsqlString = rsql.toString();
        // remove leading comma
        if(StringUtils.isNotEmpty(rsqlString)){
            rsqlString = rsqlString.substring(1);
        }
        return rsqlString;
    }
}
