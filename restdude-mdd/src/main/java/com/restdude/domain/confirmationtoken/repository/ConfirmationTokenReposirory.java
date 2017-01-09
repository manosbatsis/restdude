/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.confirmationtoken.repository;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.confirmationtoken.model.ConfirmationToken;
import org.springframework.data.jpa.repository.Query;

public interface ConfirmationTokenReposirory extends ModelRepository<ConfirmationToken, String> {

    @Query("select token from ConfirmationToken token where token.tokenValue = ?1 and token.targetId = ?2")
    ConfirmationToken findByTokenValueAndRTargetId(String tokenValue, String targetId);

    @Query("select token from ConfirmationToken token where token.targetId = ?1")
    ConfirmationToken findByTargetId(String targetId);

}
