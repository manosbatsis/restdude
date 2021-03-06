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
package com.restdude.auth.spel.service;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.core.Authentication;

/**
 * Created by manos on 13/12/2016.
 */
public interface SpelEvaluationService {

    Boolean check(String securityExpression);

    Boolean check(String expression, Authentication auth);

    Boolean check(String securityExpression, EvaluationContext securityEvaluationContext);
}
