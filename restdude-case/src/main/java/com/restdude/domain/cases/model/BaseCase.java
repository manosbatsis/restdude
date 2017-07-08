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
package com.restdude.domain.cases.model;

import static com.restdude.domain.CommentableModel.MAX_DETAIL_LENGTH;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.cases.CaseUtil;
import com.restdude.domain.cms.model.Tag;
import com.restdude.domain.users.model.User;
import com.restdude.mdd.annotation.model.ModelResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Formula;
import org.javers.core.metamodel.annotation.DiffIgnore;

@Slf4j
@Entity
@Table(name = "case_base")
@ModelResource(pathFragment = BaseCase.API_PATH_FRAGMENT,
        apiDescription = "Cases management")
@ApiModel(description = BaseCase.API_MODEL_DESCRIPTION)
public  class BaseCase<C extends BaseCase<C, CC>, CC extends BaseCaseComment<C, CC>>
        extends RetreivableResource<SpaceCasesApp, BaseCase>
        /*implements ICaseModel<SpaceCasesApp, CC>*/ {

    public static final String API_PATH_FRAGMENT = "cases";
    public static final String API_MODEL_DESCRIPTION = "A model representing a case, such as an issue, ticket, note etc.";

    @ApiModelProperty(value = "The address the request originated from", readOnly = true)
    @Column(name = "remote_address", updatable = false, length = MAX_DETAIL_LENGTH)
    @Getter @Setter
    private String remoteAddress;

    @Formula(" (select count(*) from case_comment_base where case_comment_base.parent_case = id ) ")
    @ApiModelProperty(value = "The number of open cases")
    @Getter @Setter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer commentsCount = 0;

    @ApiModelProperty(value = "The case priority")
    @Getter @Setter
    private String priority = "Normal";


    @Column(name = "entry_index")
    @Getter @Setter
    private Integer entryIndex;


    @ApiModelProperty(value = "List of tags")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "issue_tags", joinColumns = {@JoinColumn(name = "tag")}, inverseJoinColumns = {
            @JoinColumn(name = "issue")})
    @Getter @Setter
    private List<Tag> tags;

    @ApiModelProperty(value = "Current case status", allowableValues = "OPEM, CLOSED, [CUSTOM_VALUE]")
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    @Getter @Setter
    private CaseStatus status;

    @ApiModelProperty(value = "Assigned to", readOnly = true, hidden = true)
    @ManyToOne(fetch = FetchType.EAGER  )
    @JoinColumn(name = "assigned_to", referencedColumnName = "id", updatable = false)
    @Getter @Setter
    private User assignee;

    @DiffIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_app", referencedColumnName = "id", nullable = false)
    @Getter @Setter
    private SpaceCasesApp parentApplication;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "target", nullable = true)
    @ApiModelProperty(value = "The target resource for this case", required = false)
    @Getter @Setter
    private CaseTarget target;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCase", orphanRemoval = true)
    @ApiModelProperty(value = "The case comments")
    @Getter @Setter
    private List<BaseCaseComment> comments;
/*
    @JsonIgnore
    public CaseStatus getStatus() {
        return status;
    }

    @JsonGetter("status")
    public CaseStatustInfo getStatusrDTO() {
        return CaseStatustInfo.from(this.status);
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }
*/
    public BaseCase() {
        super();
    }


    public BaseCase(String title, String detail) {
        super();
        this.setTitle(title);
        this.setDetail(detail);
    }

    //TODO: move this to a spring adapter or something
    public void addRequestInfo(HttpServletRequest request) {
        // get request info
        if (request != null) {

            if (this.remoteAddress == null) {
                this.remoteAddress = CaseUtil.getRemoteAddress(request);
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", this.getName())
                .append("entryIndex", this.getEntryIndex())
                .append("title", this.getTitle())
                .append("status", this.getStatus())
                .append("parentApplication", this.getParentApplication())

                .toString();
    }

    @Override
    public void preSave() {

        if (StringUtils.isNotEmpty(this.remoteAddress) && this.remoteAddress.length() > MAX_DETAIL_LENGTH) {
            this.remoteAddress = StringUtils.abbreviate(this.remoteAddress, MAX_DETAIL_LENGTH);
        }

        // TODO: tmp parent pointer to app, should point to comment
        if(Objects.isNull(this.getParent())){
            this.setParent(this.getParentApplication());
        }
        else if(Objects.isNull(this.getParentApplication())){
            this.setParentApplication((SpaceCasesApp) this.getParent());
        }
        if(StringUtils.isBlank(this.getName())){
            this.setName(UUID.randomUUID().toString());
            log.debug("preSave, tmp name: {}", this.getName());
        }
        log.debug("preSave, before: {}", this);
        super.preSave();


        log.debug("preSave, after: {}", this);
    }

}
