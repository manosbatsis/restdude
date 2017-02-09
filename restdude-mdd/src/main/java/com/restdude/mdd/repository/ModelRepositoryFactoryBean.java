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
package com.restdude.mdd.repository;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import javax.validation.Validator;
import java.io.Serializable;

//@Component
public class ModelRepositoryFactoryBean<R extends JpaRepository<T, PK>, T extends CalipsoPersistable<PK>, PK extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, PK> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelRepositoryFactoryBean.class);

	private Validator validator;

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;

	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

		return new RepositoryFactory(entityManager, this.validator);
	}

    private static class RepositoryFactory<T extends CalipsoPersistable<PK>, PK extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;
		private Validator validator;

		public RepositoryFactory(EntityManager entityManager, Validator validator) {
			super(entityManager);
			this.entityManager = entityManager;
			this.validator = validator;
		}

		private Object getModelRepository(RepositoryInformation information, EntityManager entityManager) {
            return new BaseRepositoryImpl<T, PK>((Class<T>) information.getDomainType(), entityManager, this.validator);
        }

		@Override
		protected Object getTargetRepository(RepositoryInformation information) {
			return this.getModelRepository(information, this.entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

			// The RepositoryMetadata can be safely ignored, it is used by the
			// JpaRepositoryFactory
			// to check for QueryDslJpaRepository's which is out of scope.
			return ModelRepository.class;
		}
	}
}
