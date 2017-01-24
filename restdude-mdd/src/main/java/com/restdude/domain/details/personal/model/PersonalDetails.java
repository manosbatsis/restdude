/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.details.personal.model;

import com.restdude.domain.base.model.AbstractPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.uischema.annotation.FormSchemaEntry;
import com.restdude.mdd.uischema.annotation.FormSchemas;
import io.swagger.annotations.ApiModel;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import java.time.LocalDate;

@ShallowReference
@Entity
@ApiModel(description = "PersonalDetails")
@Table(name = "details_personal")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonalDetails extends AbstractPersistable<String> implements CalipsoPersistable<String> {

    @Id
    private String id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private User user;

    @Column(name = "birthday")
    @FormSchemas({@FormSchemaEntry(json = FormSchemaEntry.TYPE_DATE)})
    private LocalDate birthDay;

    public PersonalDetails() {
    }

    /**
     * Get the entity's primary key
     *
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public String getPk() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the pk to set
     */
    public void setPk(String id) {
        this.id = id;
    }

    @Override
    public void preSave() {

    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getPk();
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
