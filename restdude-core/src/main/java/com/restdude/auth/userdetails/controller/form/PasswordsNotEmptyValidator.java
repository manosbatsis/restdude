/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.auth.userdetails.controller.form;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
*/
public class PasswordsNotEmptyValidator implements ConstraintValidator<PasswordsNotEmpty, Object> {

	private String validationTriggerFieldName;
	private String passwordFieldName;
	private String passwordVerificationFieldName;

	@Override
	public void initialize(PasswordsNotEmpty constraintAnnotation) {
		validationTriggerFieldName = constraintAnnotation.triggerFieldName();
		passwordFieldName = constraintAnnotation.passwordFieldName();
		passwordVerificationFieldName = constraintAnnotation.passwordVerificationFieldName();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		try {
			Object validationTrigger = ValidatorUtil.getFieldValue(value, validationTriggerFieldName);
			if (validationTrigger == null) {
				return passwordFieldsAreValid(value, context);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Exception occurred during validation", ex);
		}

		return true;
	}

	private boolean passwordFieldsAreValid(Object value, ConstraintValidatorContext context) throws NoSuchFieldException,
			IllegalAccessException {
		boolean passwordWordFieldsAreValid = true;

		String password = (String) ValidatorUtil.getFieldValue(value, passwordFieldName);
		if (isNullOrEmpty(password)) {
			ValidatorUtil.addValidationError(passwordFieldName, context);
			passwordWordFieldsAreValid = false;
		}

		String passwordVerification = (String) ValidatorUtil.getFieldValue(value, passwordVerificationFieldName);
		if (isNullOrEmpty(passwordVerification)) {
			ValidatorUtil.addValidationError(passwordVerificationFieldName, context);
			passwordWordFieldsAreValid = false;
		}

		return passwordWordFieldsAreValid;
	}

	private boolean isNullOrEmpty(String field) {
		return field == null || field.trim().isEmpty();
	}
}