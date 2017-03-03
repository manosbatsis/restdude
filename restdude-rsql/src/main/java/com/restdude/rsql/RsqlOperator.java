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
import lombok.Getter;

/**
 * Enum of RSQL parser operators
 */
public enum RsqlOperator {
    EQUAL(PredicateOperator.EQUAL),
    NOT_EQUAL(PredicateOperator.NOT_EQUAL),
    GREATER_THAN(PredicateOperator.GREATER_THAN),
    GREATER_THAN_OR_EQUAL(PredicateOperator.GREATER_THAN_OR_EQUAL),
    LESS_THAN(PredicateOperator.LESS_THAN),
    LESS_THAN_OR_EQUAL(PredicateOperator.LESS_THAN_OR_EQUAL),
    IN(PredicateOperator.IN),
    NOT_IN(PredicateOperator.NOT_IN);

    @Getter private final PredicateOperator predicateOperator;

    private RsqlOperator(PredicateOperator operator) {
        this.predicateOperator = operator;
    }

}