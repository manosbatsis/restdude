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
package com.restdude.domain.users.service.impl;

import com.restdude.auth.model.UserDetailsAuthenticationToken;
import com.restdude.auth.userAccount.model.EmailConfirmationOrPasswordResetRequest;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.auth.userdetails.controller.form.ValidatorUtil;
import com.restdude.auth.userdetails.model.UserDetailsImpl;
import com.restdude.auth.userdetails.util.SecurityUtil;
import com.restdude.domain.MetadatumModel;
import com.restdude.domain.Roles;
import com.restdude.domain.UserDetails;
import com.restdude.domain.UserModel;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.details.contact.service.ContactDetailsService;
import com.restdude.domain.details.contact.service.EmailDetailService;
import com.restdude.domain.users.model.*;
import com.restdude.domain.users.repository.RoleRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.UserCredentialsService;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.service.AbstractPersistableModelServiceImpl;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.HashUtils;
import com.restdude.util.exception.http.BadRequestException;
import com.restdude.util.exception.http.InvalidCredentialsException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.*;

public class UserServiceImpl extends AbstractPersistableModelServiceImpl<User, String, UserRepository>
        implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String USERDTO_CLASS = UserDTO.class.getCanonicalName();

    private final StringKeyGenerator generator = KeyGenerators.string();

    @Value("${restdude.testEmailDomain}")
    private String testEmailDomain;


    private UserCredentialsService credentialsService;
    private ContactDetailsService contactDetailsService;
    private EmailDetailService emailDetailService;


    private RoleRepository roleRepository;
    private UserRegistrationCodeRepository userRegistrationCodeRepository;

    private PasswordEncoder passwordEncoder;
    private Validator validator;


    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;

    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCredentialsService(UserCredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Autowired
    public void setContactDetailsService(ContactDetailsService contactDetailsService) {
        this.contactDetailsService = contactDetailsService;
    }

    @Autowired
    public void setEmailDetailService(EmailDetailService emailDetailService) {
        this.emailDetailService = emailDetailService;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRegistrationCodeRepository(UserRegistrationCodeRepository userRegistrationCodeRepository) {
        this.userRegistrationCodeRepository = userRegistrationCodeRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateLastLogin(UserDetails u){
        this.repository.updateLastLogin(u.getPk());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User findActiveByCredentials(String userNameOrEmail, String password, Map metadata) {

        User user = null;
        user = this.findActiveByCredentials(userNameOrEmail, password);
        if (user != null) {
            if (!CollectionUtils.isEmpty(metadata)) {
                List<MetadatumModel> saved = this.repository.addMetadata(user.getPk(), metadata);
                for (MetadatumModel meta : saved) {
                    user.addMetadatum((UserMetadatum) meta);
                }

            }
        }
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     */
    @Override
    public User findById(String id) {
        return this.initRoles(super.findById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     */
    @Override
    public List<User> findByIds(Set<String> ids) {
        return this.initRoles(super.findByIds(ids));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        return this.initRoles(super.findAll());
    }

    private List<User> initRoles(List<User> all) {
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(all)){
            for(User user : all){
                this.initRoles(user);
            }
        }
        return all;
    }

    /**
     * {@inheritDoc}
     *
     * @param spec
     * @param pageable
     */
    @Override
    public Page<User> findPaginated(Specification<User> spec, Pageable pageable) {
        Page<User> users = super.findPaginated(spec, pageable);
        this.initRoles(users.getContent());
        return users;
    }

    @Override
    @Transactional(readOnly = false)
    public User create(User resource) {
        LOGGER.debug("create resource: {}", resource);
        // create
        resource = this.create(resource, false);

        // force any errors to occur sooner rather than later
        this.repository.flush();

        // sent email confirmation message if no errors occurred
        emailService.sendAccountConfirmation(resource);

        // done
        return resource;
    }

    @Override
    @Transactional(readOnly = false)
    public User createAsConfirmed(User resource) {
        resource = this.create(resource, true);
        this.repository.flush();
        return resource;
    }

    private User create(User resource, boolean skipConfirmation) {

        LOGGER.debug("create resource: {}, skipConfirmation: {}", resource, skipConfirmation);
        // note any credential info
        UserCredentials credentials = this.noteCredentials(resource);
        // note contact details
        ContactDetails contactDetails = this.noteContactDetails(resource);
        // note any registration code info
        UserRegistrationCode code = noteRegistrationCode(credentials);

        // skip or force confirmation etc.
        if (contactDetails.getPrimaryEmail() != null) {
            contactDetails.getPrimaryEmail().setVerified(skipConfirmation);
        }
        credentials.setActive(skipConfirmation);
        credentials.setResetPasswordToken(skipConfirmation ? null : generator.generateKey());

        // create hash and default username if missing
        this.addHashDefaultUsername(resource, contactDetails);

        // set user role in advance if no confirmation is needed,
        // clear roles otherwise
        if (skipConfirmation) {
            Role userRole = roleRepository.findByName(Roles.ROLE_USER);
            resource.addRole(userRole);
        } else {
            resource.setRoles(null);
        }


        LOGGER.debug("create, save user: {}", resource);
        resource = super.create(resource);

        LOGGER.debug("create, attach and persist credentials: {}", credentials);
        credentials.setUser(resource);
        credentials = this.credentialsService.create(credentials);
        resource.setCredentials(credentials);


        LOGGER.debug("create, attach and persist contact details: {}", contactDetails);
        contactDetails.setUser(resource);
        contactDetails = this.contactDetailsService.create(contactDetails);
        resource.setContactDetails(contactDetails);


        LOGGER.debug("create, update registration code: {}", contactDetails);
        if (code != null && code.getPk() != null) {
            code.setCredentials(resource.getCredentials());
            this.userRegistrationCodeRepository.patch(code);
        }

        // force any errors to occur sooner rather than later
        this.repository.flush();

        return initRoles(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public User patch(User resource) {
        return initRoles(super.patch(resource));
    }

    protected UserRegistrationCode noteRegistrationCode(UserCredentials credentials) {
        UserRegistrationCode code = credentials.getRegistrationCode();
        credentials.setRegistrationCode(null);
        if (code != null && code.getPk() != null) {
            boolean available = this.userRegistrationCodeRepository.isAvailable(code.getPk());
            if (!available) {
                throw new BadRequestException("Invalid registration code");
            }
        } else {
            code = null;
        }
        return code;
    }


    protected UserCredentials noteCredentials(User resource) {
        UserCredentials credentials = resource.getCredentials();
        // init credentials if empty
        if (credentials == null) {
            credentials = new UserCredentials();
        }

        // encrypt password if provided
        if (credentials.getPassword() != null) {
            LOGGER.debug("createAsConfirmed, new password: " + credentials.getPassword());
            credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        }

        // clean transient/unpersisted relationship
        resource.setCredentials(null);

        return credentials;
    }

    protected ContactDetails noteContactDetails(User resource) {
        ContactDetails contactDetails = resource.getContactDetails();
        // init credentials if empty
        if (contactDetails == null) {
            contactDetails = new ContactDetails();
        } else if (contactDetails.getPrimaryEmail() != null && StringUtils.isNotBlank(contactDetails.getPrimaryEmail().getEmail())) {
            resource.setEmailHash(HashUtils.md5Hex(contactDetails.getPrimaryEmail().getEmail()));
        }

        // clean transient/unpersisted relationship
        resource.setContactDetails(null);

        return contactDetails;
    }


    private void addHashDefaultUsername(User resource, ContactDetails contactDetails) {

        // add email hash
        if (contactDetails.getPrimaryEmail() != null) {
            resource.setEmailHash(HashUtils.md5Hex(contactDetails.getPrimaryEmail().getValue()));
        }

        // fallback username
        if (!StringUtils.isNotBlank(resource.getUsername())) {
            String username = contactDetails.getPrimaryEmail().getValue();
            if (StringUtils.isNotBlank(username)) {
                username = username.replace("@", "_").replaceAll("\\.", "_");
            }
            resource.setUsername(username);
        }
    }


    @Override
    @Transactional(readOnly = false)
    public void expireConfirmationOrPasswordResetTokens() {
        // get a hibernate session suitable for read-only access to large datasets
        StatelessSession session = ((Session) this.repository.getEntityManager().getDelegate()).getSessionFactory().openStatelessSession();
        Date yesterday = DateUtils.addDays(new Date(), -1);

        // send email notifications for account confirmation tokens that expired
        org.hibernate.Query query = session.createQuery(UserRepository.SELECT_USERDTO + " FROM User u "
                + "WHERE u.credentials.resetPasswordTokenCreated IS NOT NULL and u.credentials.resetPasswordTokenCreated  < :yesterday");
        query.setParameter("yesterday", yesterday);
        query.setFetchSize(Integer.valueOf(1000));
        query.setReadOnly(true);
        query.setLockMode("a", LockMode.NONE);
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        while (results.next()) {
            UserDTO dto = (UserDTO) results.get(0);
            // send expiration email
            this.emailService.sendAccountConfirmationExpired(new User(dto));
        }
        results.close();
        session.close();

        // expire tokens, including password reset requests
        this.repository.expireResetPasswordTokens(yesterday);
    }

    @Override
    @Transactional(readOnly = false)
    public User handleConfirmationOrPasswordResetToken(EmailConfirmationOrPasswordResetRequest passwordResetRequest) {


        // require email and token
        if (StringUtils.isBlank(passwordResetRequest.getEmailOrUsername()) || StringUtils.isBlank(passwordResetRequest.getResetPasswordToken())) {
            throw new BadRequestException("The following parameters are required: email, resetPasswordToken");
        }

        // ensure the user exists
        User user = this.findOneByUserNameOrEmail(passwordResetRequest.getEmailOrUsername());
        if (user == null) {
            throw new InvalidCredentialsException("Could not match a user: " + passwordResetRequest.getEmailOrUsername());
        }

        // validate the provided token
        UserCredentials credentials = user.getCredentials();
        if (!passwordResetRequest.getResetPasswordToken().equals(credentials.getResetPasswordToken())) {
            throw new InvalidCredentialsException("Could not match token: " + passwordResetRequest.getEmailOrUsername());
        }

        // token is valid, clear matching record
        credentials.setResetPasswordToken(null);
        credentials.setResetPasswordTokenCreated(null);

        // update password if given, ensure a password exists otherwise
        String password = passwordResetRequest.getPassword();
        if (StringUtils.isNotBlank(password)) {
            credentials.setPassword(this.passwordEncoder.encode(password));
        } else if (StringUtils.isBlank(credentials.getPassword())) {
            throw new BadRequestException("The following parameters are required: password, passwordConfirmation");
        }

        // activate user, set (verified) user role and verify email if this is a registration confirmation
        if (!credentials.getActive()) {
            credentials.setActive(true);
            EmailDetail email = user.getContactDetails().getPrimaryEmail();
            user.getContactDetails().setPrimaryEmail(this.emailDetailService.forceVerify(email));

            Role userRole = roleRepository.findByName(Roles.ROLE_USER);
            user.addRole(userRole);
            user = this.repository.save(user);
        }

        // persist
        credentials = this.credentialsService.update(credentials);
        //this.credentialsService.flush();
        user.setCredentials(credentials);
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public void handlePasswordResetRequest(String userNameOrEmail) {
        Assert.notNull(userNameOrEmail);
        User user = this.findActiveByUserNameOrEmail(userNameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("Could not match username/email to an active user: " + userNameOrEmail);
        }
        // keep any existing token
        UserCredentials credentials = user.getCredentials();
        if (credentials.getResetPasswordToken() == null) {
            credentials.setResetPasswordToken(this.generator.generateKey());
            credentials.setResetPasswordTokenCreated(LocalDateTime.now());
            credentials = this.credentialsService.update(credentials);
            user.setCredentials(credentials);
        }
        emailService.sendPasswordResetLink(user);

    }

    @Override
    @PreAuthorize(" hasRole('ROLE_USER') ")
    @Transactional(readOnly = false)
    public User updateUsername(UsernameChangeRequest usernameChangeRequest) {
        Set<ConstraintViolation<UsernameChangeRequest>> constraintViolations = validator.<UsernameChangeRequest>validate(usernameChangeRequest);
        ValidatorUtil.throwIfNonEmpty(constraintViolations, UsernameChangeRequest.class.getCanonicalName());

        User user = this.findActiveByCredentials(this.getPrincipal().getUsername(), usernameChangeRequest.getPassword());
        if (user == null) {
            throw new InvalidCredentialsException("Invalid password");
        }
        LOGGER.debug("updateUsername user: {}", user);
        // Update username
        user.setUsername(usernameChangeRequest.getUsername());
        user = this.repository.save(user);
        LOGGER.debug("updateUsername updated user: {}", user);
        return user;


    }


    //	@Override
//	@Transactional(readOnly = false)
//	public User confirmPrincipal(String confirmationToken) {
//		Assert.notNull(confirmationToken);
//		//LoggedInUserDetails loggedInUserDetails = new LoggedInUserDetails();
//		User original = this.userRepository.findByConfirmationToken(confirmationToken);
//		if (original != null) {
//			// enable and update user
//			original.setResetPasswordToken(null);
//			original = this.userRepository.save(original);
//		} else {
//			LOGGER.warn("Could not find any user matching confirmation token: " + confirmationToken);
//		}
//
//		LOGGER.info("create returning local user: " + original);
//		return original;
//	}


    /**
     * {@inheritDoc}
     */
    @Override
    public User findOneByUserNameOrEmail(String... tokens) {
        User found = null;
        if (tokens != null) {
            String userNameOrEmail;
            for (int i = 0; found == null && i < tokens.length; i++) {
                userNameOrEmail = tokens[i];
                if (StringUtils.isNotBlank(userNameOrEmail)) {
                    found = userNameOrEmail.contains("@")
                            ? this.repository.findByEmail(userNameOrEmail)
                            : this.repository.findByUsername(userNameOrEmail);
                }
            }
        }
        return initRoles(found);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findActiveByCredentials(String userNameOrEmail, String password) {
        User found = null;
        if (StringUtils.isNotBlank(userNameOrEmail) && StringUtils.isNotBlank(password)) {
            User unmatched = this.findActiveByUserNameOrEmail(userNameOrEmail);
            // match password
            if (unmatched != null && passwordEncoder.matches(password, unmatched.getCredentials().getPassword())) {
                found = unmatched;
            }
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findActiveByUsername(String username) {
        return initRoles(this.repository.findActiveByUsername(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findActiveByEmail(String email) {
        return initRoles(this.repository.findActiveByEmail(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findActiveById(String id) {
        return initRoles(this.repository.findActiveById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findActiveByUserNameOrEmail(String userNameOrEmail) {
        User found = null;
        if (StringUtils.isNotBlank(userNameOrEmail)) {
            found = userNameOrEmail.contains("@")
                    ? this.findActiveByEmail(userNameOrEmail)
                    : this.findActiveByUsername(userNameOrEmail);
        }
        return initRoles(found);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getActiveByCredentials(String username, String password){
        return Optional.ofNullable(this.findActiveByCredentials(username, password));

    }


    @Override
    public Optional<User> getActiveByUsername(String username) {
        return Optional.ofNullable(this.findActiveByUsername(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public UserModel createForImplicitSignup(User userAccountData) {
        // simplify further condition checks
        if (userAccountData.getCredentials() == null) {
            userAccountData.setCredentials(new UserCredentials());
        }

        UserModel existing = this.getPrincipalLocalUser();
        if (existing == null) {
            String email = userAccountData.getContactDetails().getPrimaryEmail() != null ? userAccountData.getContactDetails().getPrimaryEmail().getValue() : null;
            if (StringUtils.isNotBlank(email) && email.contains("@")) {
                existing = this.repository.findByEmail(email);
            }
        }
        if (existing == null) {

            userAccountData.getCredentials().setPassword(this.passwordEncoder.encode(this.generator.generateKey()));
            userAccountData.getCredentials().setActive(true);
            existing = this.createAsConfirmed(userAccountData);
        }

        return existing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public User changePassword(String userNameOrEmail, String oldPassword, String newPassword,
                               String newPasswordConfirm) {

        // make sure we have all params

        String[] params = {userNameOrEmail, oldPassword, newPassword, newPasswordConfirm};
        if (StringUtils.isAnyBlank(userNameOrEmail, oldPassword, newPassword, newPasswordConfirm)) {
            throw new BadRequestException("Required parameters: username/email, currentPassword, password, passwordConfirmation");
        }

        // make sure new password and confirm match
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new BadRequestException("Both password and password confirmation are required and must be equal");
        }
        // make sure a user matching the credentials is found
        User u = this.findActiveByCredentials(userNameOrEmail, oldPassword);
        if (u == null) {
            throw new BadRequestException("Failed updating user pass: A user could not be found with the given credentials");
        }
        UserCredentials credentials = u.getCredentials();
        // update password and return user
        LOGGER.debug("changePassword, new password: " + newPassword);
        credentials.setPassword(this.passwordEncoder.encode(newPassword));
        credentials.setLastPassWordChangeDate(LocalDateTime.now());
        credentials = this.credentialsService.update(credentials);
        //this.credentialsService.flush();
        u.setCredentials(credentials);
        return u;
    }

    @Override
    @Transactional(readOnly = false)
    @PreAuthorize(" hasRole('ROLE_USER') ")
    public UserInvitationResultsDTO inviteUsers(UserInvitationsDTO invitations) {
        UserInvitationResultsDTO results = new UserInvitationResultsDTO();
        // add from list
        if(!CollectionUtils.isEmpty(invitations.getRecepients())){
            // Proces recepients
            for(UserDTO dto : invitations.getRecepients()){
                User u = this.repository.findByEmail(dto.getEmail());
                if(u == null){
                    if(results.getInvited().contains(dto.getEmail())){
                        // Ignore duplicate user
                        results.getDuplicate().add(dto.getEmail());
                    }
                    else{
                        // invite user
                        results.getInvited().add(this.create(dto.toUser()).getContactDetails().getPrimaryEmail().getValue());
                    }
                }
                else{
                    // skip existing user email
                    results.getExisting().add(dto.getEmail());
                }
            }
        }
        // add from string of addresses
        if(StringUtils.isNotBlank(invitations.getAddressLines())){
            List<String> addresses = Arrays.asList(invitations.getAddressLines().replaceAll("\\r?\\n", ",").split(","));
            if(!CollectionUtils.isEmpty(addresses)){
                for(String sAddress : addresses){
                    sAddress = sAddress.toLowerCase();
                    InternetAddress email = isValidEmailAddress(sAddress);
                    if(email != null){
                        if(results.getInvited().contains(email.getAddress())){
                            // ignore duplicate email
                            results.getDuplicate().add(email.getAddress());
                        }
                        else{
                            User u = this.repository.findByEmail(email.getAddress());
                            if(u == null){
                                u = new User();
                                u.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail(email.getAddress())).build());
                                String personal = email.getPersonal();
                                if(StringUtils.isNotBlank(personal)){
                                    String[] names = personal.split(" ");
                                    if(names.length > 0){
                                        if(StringUtils.isNotBlank(names[0])){
                                            u.setFirstName(names[0]);
                                        }
                                        if(names.length > 1 && StringUtils.isNotBlank(names[1])){
                                            u.setLastName(names[1]);
                                        }
                                        // handle middle name
                                        if(names.length > 2 && StringUtils.isNotBlank(names[2])){
                                            u.setLastName(names[2]);
                                        }
                                    }
                                }
                                // invite user
                                results.getInvited().add(this.create(u).getContactDetails().getPrimaryEmail().getValue());
                            }
                        }
                    }
                    else{
                        // ignore invalid email
                        results.getInvalid().add(sAddress);
                    }
                }
            }

        }

        return results;

    }

    /**
     * {@inheritDoc}
     *
     * @param systemUser
     */
    @Override
    protected void initDataOverride(User systemUser) {

        LOGGER.debug("run");
        if(this.count() == 0){


            Role adminRole = this.roleRepository.findByIdOrName(Roles.ROLE_ADMIN);
            Role operatorRole = roleRepository.findByIdOrName(Roles.ROLE_SITE_OPERATOR);

            LocalDateTime now = LocalDateTime.now();
            String domain = this.getTestEmailDomain();

            User system = new User();
            system.setUsername("system");
            system.setFirstName("System");
            system.setLastName("User");
            system.setCredentials(new UserCredentials.Builder().password("system").build());
            system.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("system@" + domain)).build());
            system.setLastVisit(now);
            system = this.createAsConfirmed(system);

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setLastVisit(now);
            adminUser.addRole(adminRole);
            adminUser.setCredentials(new UserCredentials.Builder().password("admin").build());
            adminUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("admin@" + domain)).build());
//			adminUser.setCreatedBy(system);
            adminUser = this.createAsConfirmed(adminUser);

            SecurityContextHolder.getContext().setAuthentication(
                    new UserDetailsAuthenticationToken(UserDetailsImpl.fromUser(adminUser)));
            LOGGER.debug("run, principal: {}", SecurityUtil.getPrincipal());


            User adminFriend = new User();
            adminFriend.setUsername("adminFriend");
            adminFriend.setFirstName("Admins");
            adminFriend.setLastName("Friend");
            adminFriend.setLastVisit(now);
            adminFriend.setCredentials(new UserCredentials.Builder().password("adminFriend").build());
            adminFriend.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("adminFriend@" + domain)).build());
//			adminUser.setCreatedBy(system);
            adminFriend = this.createAsConfirmed(adminFriend);


            User opUser = new User();
            opUser.setUsername("operator");
            opUser.setFirstName("Operator");
            opUser.setLastName("User");
            opUser.setCredentials(new UserCredentials.Builder().password("operator").build());
            opUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("operator@" + domain)).build());
            opUser.setLastVisit(now);
            opUser.addRole(operatorRole);
//			opUser.setCreatedBy(system);
            opUser = this.createAsConfirmed(opUser);

            User simpleUser = new User();
            simpleUser.setUsername("simple");
            simpleUser.setFirstName("Simple");
            simpleUser.setLastName("User");
            simpleUser.setCredentials(new UserCredentials.Builder().password("simple").build());
            simpleUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("simple@" + domain)).build());
            simpleUser.setLastVisit(now);
//			simpleUser.setCreatedBy(system);
            simpleUser = this.createAsConfirmed(simpleUser);

            User userControllerIt = new User();
            userControllerIt.setUsername("usercontrollerit");
            userControllerIt.setFirstName("Usercontrollerit");
            userControllerIt.setLastName("User");
            userControllerIt.setCredentials(new UserCredentials.Builder().password("usercontrollerit").build());
            userControllerIt.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("usercontrollerit@" + domain)).build());
            userControllerIt.setLastVisit(now);
//			simpleUser.setCreatedBy(system);
            userControllerIt = this.createAsConfirmed(userControllerIt);


            int usersMax = 5;
            int usersCreated = 0;
            while (usersCreated < usersMax) {
                for (String fullName : this.getTenNames()) {
                    String userName = fullName.toLowerCase().replace(" ", "") + usersCreated;
                    User u = new User();
                    u.setUsername(userName);
                    u.setFirstName(fullName.substring(0, fullName.indexOf(" ")));
                    u.setLastName(fullName.substring(fullName.indexOf(" ") + 1));
                    u.setCredentials(new UserCredentials.Builder().password(userName).build());
                    u.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail(userName + "@" + testEmailDomain)).build());
                    u.setLastVisit(now);
//					u.setCreatedBy(system);
                    u = this.createAsConfirmed(u);

                    usersCreated++;
                    LOGGER.info("Created user: " + u);
                    if (usersCreated >= usersMax) {
                        break;
                    }
                }
            }
        }
    }

    public static InternetAddress isValidEmailAddress(String email) {
        InternetAddress emailAddr = null;
        try {
            emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            emailAddr = null;
        }
        return emailAddr;
    }

    @Override
    @Transactional(readOnly = false)
    public User updateFiles(String id, MultipartHttpServletRequest request, HttpServletResponse response) {
        return initRoles(super.updateFiles(id, request, response));
    }

    private String[] getTenNames() {
        try {
            URL namey = new URL("http://namey.muffinlabs.com/name.json?count=10&with_surname=true");
            URLConnection yc = namey.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String[] names = in.readLine().replace("[", "").replace("]", "").replace("\"", "").split(",");
            return names;
        } catch (Exception e) {
            String[] names = {"Linda Hernandez", "David Ellis", "Nancy Morgan", "Elizabeth White", "Richard Collins",
                    "David Sanchez", "Michael Cox", "Karen Moore", "John Gray", "Carol Garcia"};
            return names;
        }
    }

    protected String getTestEmailDomain() {
        Configuration config = ConfigurationFactory.getConfiguration();
        return config.getString("restdude.testEmailDomain", this.testEmailDomain);
    }




}