/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
import com.restdude.util.ConfigurationFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailService.class);

    public abstract String getDefaultMailFrom();

    public abstract Boolean getMailEnabled();

    public abstract String getBaseUrl();

    public abstract JavaMailSender getMailSender();

    public abstract TemplateEngine getTemplateEngine();

    public abstract MessageSource getMessageSource();

    /**
     * Send an Account Confirmation email to the given user
     * @param user
     * @throws MessagingException
     */
    public void sendAccountConfirmation(final User user) {
        final String subject = getMessageSource().getMessage("email.accountconfirmation.subject", null, user.getLocaleObject());
        final String templateName = "email-account-confirmation.html";
        sendEmailToUser(user, subject, templateName);
    }

    /**
     * Send an email to the given user, notifying him the account confirmation time window expired
     * @param user
     * @throws MessagingException
     */
    public void sendAccountConfirmationExpired(final User user) {
        final String subject = getMessageSource().getMessage("email.accountconfirmation.expired.subject", null, user.getLocaleObject());
        final String templateName = "email-account-confirmation-expired.html";
        sendEmailToUser(user, subject, templateName);
    }

    /**
     * Send an test email to verify outgoing email settings
     * @param user the user to send the email to
     * @throws MessagingException
     */
    public void sendTest(final User user) {
        final String subject = "[ignore] Email Server Test";
        final String templateName = "email-test.html";
        sendEmailToUser(user, subject, templateName);
    }

    /**
     * Send a password reset email to the given user
     * @param user
     * @throws MessagingException
     */

    public void sendPasswordResetLink(final User user) {
        final String subject = getMessageSource().getMessage("email.passwordreset.subject", null, user.getLocaleObject());
        final String templateName = "email-password-reset.html";

        sendEmailToUser(user, subject, templateName);
    }

    public void sendEmailToUser(final User user, final String subject,
                                final String templateName) {
        String emailTo = user.getContactDetails().getPrimaryEmail().getValue();
        String emailFrom = getDefaultSender(user);
        // Prepare the evaluation context
        String locale = StringUtils.isNoneBlank(user.getLocale()) ? user.getLocale() : "en";
        final Context ctx = new Context(new Locale(locale));
        ctx.setVariable("user", user);

        sendEmail(subject, templateName, emailTo, emailFrom, ctx);
    }

    protected String getDefaultSender(final User user) {
        String emailFrom = new StringBuffer("\"")
                .append(getMessageSource().getMessage("email.from", null, user.getLocaleObject()))
                .append("\" <")
                .append(getDefaultMailFrom())
                .append('>')
                .toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getDefaultSender: {}", emailFrom);
        }
        return emailFrom;
    }

    @Async
    public void sendEmail(final String subject, final String templateName, String emailTo, String emailFrom, final Context ctx) {
        try {
            // Prepare message using a Spring helper
            final MimeMessage mimeMessage = getMailSender().createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject(subject);
            message.setFrom(emailFrom);
            message.setTo(emailTo);
            ctx.setVariable("baseUrl", getBaseUrl());
            // Create the HTML body using Thymeleaf
            final String htmlContent = getTemplateEngine().process(templateName, ctx);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sending email, to: {}, body: \n {}", emailTo, htmlContent);
            }
            message.setText(htmlContent, true /* isHtml */);

            // Send email
            Configuration config = ConfigurationFactory.getConfiguration();
            if (getMailEnabled() && StringUtils.isNotBlank(config.getString("mail.server.host"))) {
                getMailSender().send(mimeMessage);
            } else {
                LOGGER.warn("Skipped sending email as mail.server.host property is empty");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send email: ", e);
        }


    }


    /* 
     * Send HTML mail with attachment. 
     */
    @Async
    public void sendMailWithAttachment(
            final String recipientName, final String recipientEmail, final String attachmentFileName,
            final byte[] attachmentBytes, final String attachmentContentType, final Locale locale)
            throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = getMailSender().createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with attachment");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = getTemplateEngine().process("email-withattachment.html", ctx);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending email body: \n {}", htmlContent);
        }
        message.setText(htmlContent, true /* isHtml */);

        // Add the attachment
        final InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);
        message.addAttachment(attachmentFileName, attachmentSource, attachmentContentType);

        // Send mail
        getMailSender().send(mimeMessage);

    }


    /* 
     * Send HTML mail with inline image
     */
    @Async
    public void sendMailWithInline(
            final String recipientName, final String recipientEmail, final String imageResourceName,
            final byte[] imageBytes, final String imageContentType, final Locale locale)
            throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = getMailSender().createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with inline image");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = getTemplateEngine().process("email-inlineimage.html", ctx);
        LOGGER.info("Sending email body: " + htmlContent);
        message.setText(htmlContent, true /* isHtml */);

        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);

        // Send mail
        getMailSender().send(mimeMessage);

    }


}