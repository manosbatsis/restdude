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
    public String getId() {
        return id;
    }

    /**
     * Set the entity's primary key
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    public boolean isNew() {
        return null == getId();
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
