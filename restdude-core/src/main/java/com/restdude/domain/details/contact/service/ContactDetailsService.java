package com.restdude.domain.details.contact.service;


import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.details.contact.model.*;
import org.springframework.security.access.method.P;

public interface ContactDetailsService extends ModelService<ContactDetails, String> {
    public static final String BEAN_ID = "contactDetailsService";

    /**
     * Set detail as primary
     */
    public ContactDetails setPrimary(@P("contactDetails") ContactDetails contactDetails, @P("detail") ContactDetail detail);

    /**
     * Set detail as primary
     */
    public ContactDetails setPrimary(@P("contactDetails") ContactDetails contactDetails, @P("detail") EmailDetail detail);

    /**
     * Set detail as primary
     */
    public ContactDetails setPrimary(@P("contactDetails") ContactDetails contactDetails, @P("detail") CellphoneDetail detail);

    /**
     * Set detail as primary
     */
    public ContactDetails setPrimary(@P("contactDetails") ContactDetails contactDetails, @P("detail") PostalAddressDetail detail);
}
