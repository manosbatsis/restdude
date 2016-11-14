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
public class PasswordsNotEqualValidator implements ConstraintValidator<PasswordsNotEqual, Object> {

	private String passwordFieldName;

	private String passwordVerificationFieldName;

	@Override
	public void initialize(PasswordsNotEqual constraintAnnotation) {
		this.passwordFieldName = constraintAnnotation.passwordFieldName();
		this.passwordVerificationFieldName = constraintAnnotation.passwordVerificationFieldName();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		try {
			String password = (String) ValidatorUtil.getFieldValue(value, passwordFieldName);
			String passwordVerification = (String) ValidatorUtil.getFieldValue(value, passwordVerificationFieldName);

			if (passwordsAreNotEqual(password, passwordVerification)) {
				ValidatorUtil.addValidationError(passwordFieldName, context);
				ValidatorUtil.addValidationError(passwordVerificationFieldName, context);

				return false;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Exception occurred during validation", ex);
		}

		return true;
	}

	private boolean passwordsAreNotEqual(String password, String passwordVerification) {
		return !(password == null ? passwordVerification == null : password.equals(passwordVerification));
	}
}
