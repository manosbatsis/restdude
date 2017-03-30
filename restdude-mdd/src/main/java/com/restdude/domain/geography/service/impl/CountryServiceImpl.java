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
package com.restdude.domain.geography.service.impl;

import com.restdude.domain.geography.model.Country;
import com.restdude.domain.geography.repository.CountryRepository;
import com.restdude.domain.geography.service.CountryService;
import com.restdude.mdd.annotation.model.ModelDrivenPreAuth;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Named;
import java.util.Collection;

@Slf4j
@Named("countryService")
public class CountryServiceImpl extends AbstractPersistableModelServiceImpl<Country, String, CountryRepository> implements CountryService {

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    @ModelDrivenPreAuth
    public void create(Collection<Country> resources) {
        Assert.notNull(resources, "Resource can't be null");
        repository.save(resources);
    }

}