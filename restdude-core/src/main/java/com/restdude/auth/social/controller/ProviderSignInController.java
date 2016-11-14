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
package com.restdude.auth.social.controller;

import com.restdude.auth.userdetails.controller.form.RegistrationForm;
import com.restdude.auth.userdetails.integration.UserDetailsConfig;
import com.restdude.auth.userdetails.model.ICalipsoUserDetails;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.DuplicateEmailException;
import com.restdude.auth.userdetails.util.SimpleUserDetailsConfig;
import com.restdude.auth.userdetails.util.SocialMediaService;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.util.ConfigurationFactory;
import io.swagger.annotations.Api;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.inject.Inject;


//@Controller
@Api(tags = "OAuth", description = "Service provider connection operations")
@SessionAttributes("user")
@RequestMapping(value = {"/api/auth/oauth/signin"})
public class ProviderSignInController extends org.springframework.social.connect.web.ProviderSignInController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderSignInController.class);

	protected static final String VIEW_NAME_REMEMBER_PAGE = "user/remember";
	protected static final String VIEW_NAME_REGISTRATION_PAGE = "register";

	protected static final String ERROR_CODE_EMAIL_EXIST = "NotExist.user.email";
	protected static final String MODEL_NAME_REGISTRATION_DTO = "user";

	private UserDetailsService userService;

	private UserDetailsConfig userDetailsConfig = new SimpleUserDetailsConfig();

	@Inject
	public void setUserDetailsConfig(UserDetailsConfig userDetailsConfig) {
		this.userDetailsConfig = userDetailsConfig;
	}

	@Inject
	@Qualifier("userDetailsService") // somehow required for CDI to work on 64bit JDK?
	public void setUserService(UserDetailsService userService) {
		this.userService = userService;
	}

	/**
	 * Creates a new provider sign-in controller.
	 * @param connectionFactoryLocator the locator of {@link ConnectionFactory connection factories} used to support provider sign-in.
	 * Note: this reference should be a serializable proxy to a singleton-scoped target instance.
	 * This is because {@link ProviderSignInAttempt} are session-scoped objects that hold ConnectionFactoryLocator references.
	 * If these references cannot be serialized, NotSerializableExceptions can occur at runtime.
	 * @param usersConnectionRepository the global store for service provider connections across all users.
	 * Note: this reference should be a serializable proxy to a singleton-scoped target instance.
	 * @param signInAdapter handles user sign-in
	 */
	@Inject
	public ProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository,
			SignInAdapter signInAdapter) {
		super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);

		Configuration config = ConfigurationFactory.getConfiguration();
		String applicationUrl = config.getString("calipso.baseurl");
		LOGGER.info("Setting applicationUrl to " + applicationUrl);
		//this.setApplicationUrl(applicationUrl);
	}

	@RequestMapping(value = "/popup/remember", method = RequestMethod.GET)
	public String showRememberPage() {
		LOGGER.debug("showRememberPage");
		return VIEW_NAME_REMEMBER_PAGE;
	}

	// SEE http://stackoverflow.com/questions/32461011/spring-social-providersigninutils-getconnection-is-returning-error-cannot-find
//	/**
//	* Renders the registration page.
//	*/
//	@RequestMapping(value = "/register", method = { RequestMethod.GET })
//	//@RequestMapping(value = "/popup/register", method = { RequestMethod.GET, RequestMethod.POST })
//	public String showRegistrationForm(WebRequest request, Model model) {
//		LOGGER.debug("showRegistrationForm, userAccountData: {}", model);
//
//		Connection<?> connection = ProviderSignInUtils.getConnection(request);
//
//		RegistrationForm registration = createRegistrationDTO(connection);
//		LOGGER.debug("Rendering registration form with information: {}", registration);
//
//		model.addAttribute(MODEL_NAME_REGISTRATION_DTO, registration);
//
//		return VIEW_NAME_REGISTRATION_PAGE;
//	}

//	/**
//	* Processes the form submissions of the registration form.
//	*/
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public String registerUserAccount(@Valid @ModelAttribute("user") RegistrationForm userAccountData, BindingResult result,
//			WebRequest request, HttpServletRequest nativeRquest, HttpServletResponse response) throws DuplicateEmailException {
//		LOGGER.debug("registerUserAccount with data: " + userAccountData);
//		if (result.hasErrors()) {
//			LOGGER.debug("Validation errors found. Rendering form view.");
//			return VIEW_NAME_REGISTRATION_PAGE;
//		}
//
//		LOGGER.debug("registerUserAccount, No validation errors found. Continuing registration process.");
//
//		ICalipsoUserDetails registered = createUserAccount(userAccountData, result);
//		LOGGER.debug("registerUserAccount, registered: " + registered);
//		
//		//If email address was already found from the database, render the form view.
//		if (registered == null) {
//			LOGGER.debug("An email address was found from the database. Rendering form view.");
//			return VIEW_NAME_REGISTRATION_PAGE;
//		}
//
//		//Logs the user in.
//		SecurityUtil.login(nativeRquest, response, registered, this.userDetailsConfig, this.userService);
//		//If the user is signing in by using a social provider, this method call stores
//		//the connection to the UserConnection table. Otherwise, this method does not
//		//do anything.
//		ProviderSignInUtils.handlePostSignUp(registered.getEmail(), request);
//
//		return "redirect:/client";
//	}

	/**
	* Creates a new user account by calling the service method. If the email address is found
	* from the database, this method adds a field error to the email field of the form object.
	*/
	private ICalipsoUserDetails createUserAccount(RegistrationForm userAccountData, BindingResult result) {
		LOGGER.debug("createUserAccount, userAccountData: {}", userAccountData);
		ICalipsoUserDetails registered = null;

		try {
			User user = new User();
            user.setCredentials(new UserCredentials());
            user.setEmail(userAccountData.getEmail());
			user.setFirstName(userAccountData.getFirstName());
			user.setLastName(userAccountData.getLastName());
            user.getCredentials().setUsername(userAccountData.getUserName());
            user.getCredentials().setPassword(userAccountData.getPassword());

			registered = userService.createForImplicitSignup(user);
		} catch (DuplicateEmailException ex) {
			LOGGER.debug("An email address: {} exists.", userAccountData.getEmail());
			addFieldError(MODEL_NAME_REGISTRATION_DTO, RegistrationForm.FIELD_NAME_EMAIL, userAccountData.getEmail(),
					ERROR_CODE_EMAIL_EXIST, result);
		}

		return registered;
	}

	private void addFieldError(String objectName, String fieldName, String fieldValue, String errorCode, BindingResult result) {
		LOGGER.debug("Adding field error object's: {} field: {}", objectName, fieldName);
		FieldError error = new FieldError(objectName, fieldName, fieldValue, false, new String[] { errorCode }, new Object[] {}, errorCode);

		result.addError(error);
		LOGGER.debug("Added field error: {} to binding result: {}", error, result);
	}

	/**
	* Creates the form object used in the registration form.
	* @param connection
	* @return If a user is signing in by using a social provider, this method returns a form
	* object populated by the values given by the provider. Otherwise this method returns
	* an empty form object (normal form registration).
	*/
	private RegistrationForm createRegistrationDTO(Connection<?> connection) {

		LOGGER.info("createRegistrationDTO, connection: " + connection);
		RegistrationForm dto = new RegistrationForm();

		if (connection != null) {

			LOGGER.debug("createRegistrationDTO connection: " + connection);
			UserProfile socialMediaProfile = connection.fetchUserProfile();
			dto.setEmail(socialMediaProfile.getEmail());
			dto.setFirstName(socialMediaProfile.getFirstName());
			dto.setLastName(socialMediaProfile.getLastName());
			dto.setUserName(socialMediaProfile.getUsername());

			ConnectionKey providerKey = connection.getKey();
			dto.setSignInProvider(SocialMediaService.valueOf(providerKey.getProviderId().toUpperCase()));

			LOGGER.debug("createRegistrationDTO prepopulated form: " + dto);
		} else {
			LOGGER.debug("createRegistrationDTO: no connection was found");
		}

		return dto;
	}
}
