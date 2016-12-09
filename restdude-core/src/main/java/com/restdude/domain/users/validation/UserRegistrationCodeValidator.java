package com.restdude.domain.users.validation;


import com.restdude.domain.users.model.UserRegistrationCode;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validate the registration code, if any, is available
 */
@Component
public class UserRegistrationCodeValidator implements ConstraintValidator<UserRegistrationCodeConstraint, UserRegistrationCode> {

    private UserRegistrationCodeRepository userRegistrationCodeRepository;

    public void setUserRegistrationCodeRepository(UserRegistrationCodeRepository userRegistrationCodeRepository) {
        this.userRegistrationCodeRepository = userRegistrationCodeRepository;
    }

    @Override
    public void initialize(UserRegistrationCodeConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserRegistrationCode value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            valid = false;
            if (StringUtils.isNotBlank(value.getId())) {
                UserRegistrationCode code = this.userRegistrationCodeRepository.findOne(value.getId());
                valid = code != null && code.getAvailable();
            }
        }
        return valid;
    }
}