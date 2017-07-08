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
package com.restdude.domain.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.mdd.annotation.model.ModelResource;
import com.restdude.mdd.controller.AbstractReadOnlyPersistableModelController;
import com.restdude.mdd.model.AbstractPersistableModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.javers.core.metamodel.annotation.ShallowReference;


@ShallowReference
@Entity
@Table(name = "user_totals_count")
@ModelResource(pathFragment = UserTotalsCount.API_PATH_FRAGMENT, apiDescription = UserTotalsCount.API_MODEL_DESCRIPTION, linkable = false, controllerSuperClass = AbstractReadOnlyPersistableModelController.class)
@ApiModel(description = UserTotalsCount.API_MODEL_DESCRIPTION)
public class UserTotalsCount extends AbstractPersistableModel<String> {

    public static final String API_PATH_FRAGMENT = "userTotalsCounts";
    public static final String API_MODEL_DESCRIPTION = "Basic totals info for users";

    @Id
    @Getter @Setter
    private String id;


    @Column(name = "times_logged_as_activity_subject", nullable = false)
    @Getter @Setter
    private Long timesLoggedAsActivitySubject = Long.valueOf(0);

    @Column(name = "memberships_count", nullable = false)
    @Getter @Setter
    private Long membershipsCount = Long.valueOf(0);

    @Column(name = "friends_count", nullable = false)
    @Getter @Setter
    private Long friendsCount = Long.valueOf(0);

    @JsonIgnore
    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private User user;

    public UserTotalsCount() {
        super();
    }

    public UserTotalsCount(String id, Long timesLoggedAsActivitySubject, Long membershipsCount, Long friendsCount, User user) {
        super(id);
        this.timesLoggedAsActivitySubject = timesLoggedAsActivitySubject;
        this.membershipsCount = membershipsCount;
        this.friendsCount = friendsCount;
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("timesLoggedAsActivitySubject", this.getTimesLoggedAsActivitySubject())
                .append("membershipsCount", this.getMembershipsCount())
                .append("friendsCount", this.getFriendsCount())
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return null == this.getId();
    }

}