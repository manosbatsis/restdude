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
package com.restdude.domain.cases;


import com.restdude.domain.UserModel;

import java.util.List;
import java.util.Set;

/**
 * Created by manos on 14/4/2017.
 */
public interface IBaseContext<O extends UserModel, M extends IMembership, MR extends IMembershipRequest> {
    O getOwner();

    void setOwner(O owner);

    String getTitle();

    String getName();

    String getDescription();

    String getAvatarUrl();

    String getBannerUrl();

    com.restdude.domain.cases.model.enums.ContextVisibilityType getVisibility();

    Set<M> getMemberships();

    List<MR> getMembershipRequests();

    void setName(String title);

    void setTitle(String title);

    void setDescription(String description);

    void setAvatarUrl(String avatarUrl);

    void setBannerUrl(String bannerUrl);

    void setVisibility(com.restdude.domain.cases.model.enums.ContextVisibilityType visibility);

    void setMemberships(Set<M> memberships);

    void setMembershipRequests(List<MR> membershipRequests);
}
