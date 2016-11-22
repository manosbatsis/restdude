package com.restdude.auth.userAccount.validation;


import com.restdude.util.ConfigurationFactory;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegistrationCodeValidator implements ConstraintValidator<RegistrationCodeConstraint, String> {

    private Boolean required;

    @Override
    public void initialize(RegistrationCodeConstraint constraintAnnotation) {
        this.required = ConfigurationFactory.getConfiguration().getBoolean(ConfigurationFactory.FORCE_CODES, false);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (this.required) {
            valid = false;
            if (StringUtils.isNotBlank(value)) {
                valid = true;
            }
        }
        return valid;
    }
}