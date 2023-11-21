package com.stg.b2b.config;

import com.stg.b2b.util.MailUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
@Configuration
public class MailConfig {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @PostConstruct
    public void init() {
        MailUtil.setMailSender(mailSender);
        MailUtil.setTemplateEngine(templateEngine);
    }
}
