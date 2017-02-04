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
package com.restdude.util.email.service;


import com.restdude.domain.users.model.User;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Locale;

public interface EmailService {

    public static final String QUALIFIER = "emailService";

    /**
     * Send an Account Confirmation email to the given user
     * @param user
     * @throws MessagingException
     */
    void sendAccountConfirmation(User user);

    /**
     * Send an email to the given user, notifying him the account confirmation time window expired
     * @param user
     * @throws MessagingException
     */
    void sendAccountConfirmationExpired(User user);

    /**
     * Send an test email to verify outgoing email settings
     * @param user the user to send the email to
     * @throws MessagingException
     */
    void sendTest(User user);

    /**
     * Send a password reset email to the given user
     * @param user
     * @throws MessagingException
     */
    void sendPasswordResetLink(User user);

    void sendEmailToUser(User user, String subject,
                         String templateName);

    void sendEmail(String subject, String templateName, String emailTo, String emailFrom, Context ctx);

    /**
     * Send HTML mail with attachment. 
     */
    void sendMailWithAttachment(
            String recipientName, String recipientEmail, String attachmentFileName,
            byte[] attachmentBytes, String attachmentContentType, Locale locale)
            throws MessagingException;


    /**
     * Send HTML mail with inline image
     */
    void sendMailWithInline(
            String recipientName, String recipientEmail, String imageResourceName,
            byte[] imageBytes, String imageContentType, Locale locale)
            throws MessagingException;

}