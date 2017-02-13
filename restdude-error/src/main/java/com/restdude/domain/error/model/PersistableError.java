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
package com.restdude.domain.error.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.model.ErrorModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @param <PK>
 */
@JsonIgnoreProperties("pk")
public interface PersistableError<PK extends Serializable> extends ErrorModel<PK> {

    public static final int MAX_MSTACKTRACE_LENGTH = 40000;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_MESSAGE_LENGTH = 500;


    User getUser();

    void setUser(User user);

    UserAgent getUserAgent();

    void setUserAgent(UserAgent userAgent);

    ErrorLog getErrorLog();

    void setErrorLog(ErrorLog errorLog);


    LocalDateTime getCreatedDate();

    LocalDateTime getLastModifiedDate();
}
