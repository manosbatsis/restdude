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
package com.restdude.mdd.registry;

import com.restdude.domain.base.annotation.model.ModelResource;
import com.yahoo.elide.annotation.Exclude;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.core.exceptions.DuplicateMappingException;
import com.yahoo.elide.security.checks.Check;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by manos on 9/2/2017.
 */
@Slf4j
public class EntityDictionary extends com.yahoo.elide.core.EntityDictionary{



    public EntityDictionary() {
        super(new HashMap<String, Class<? extends Check>>());
    }

}
