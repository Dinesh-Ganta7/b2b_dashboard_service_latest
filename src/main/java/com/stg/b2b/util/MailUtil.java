package com.stg.b2b.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import java.util.Properties;
@Component
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static final String MAIL_USER = "mail.user";
    private static JavaMailSender mailSender;
    private static TemplateEngine templateEngine;

    public static void setMailSender(JavaMailSender mailSender) {
        MailUtil.mailSender = mailSender;
    }

    public static void setTemplateEngine(TemplateEngine templateEngine) {
        MailUtil.templateEngine = templateEngine;
    }

    private static MimeMessage getMessage(String toEmail) throws MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty(MAIL_USER, "insightpm");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("insightpm@stgit.com", "Wub31585");
            }
        });
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        mimeMessage.setFrom(new InternetAddress("insightpm@stgit.com"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        return mimeMessage;
    }
    @Async
    public void sendHtmlEmail(String to, String subject, String body, String cc) {
        try {
            MimeMessage message = getMessage(to);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Order Assignment");
            Context context = new Context();
            context.setVariable("name", body);
            context.setVariable("number", subject);
            String htmlContent = templateEngine.process("assignment-template", context);

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc);
            }
            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Mail sent successfully");
        }catch (MessagingException e){
            logger.info("Failed to send mail");
        }

    }
    @Async
    public void sendHtmlEmail(String to, String subject, String body) {
        sendHtmlEmail(to, subject, body, null);
    }
}
