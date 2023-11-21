package com.stg.b2b.security;

import com.stg.b2b.config.LdapConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ldap")
public class LdapController {

    private static final Logger logger = LoggerFactory.getLogger(LdapController.class);


    /**
     * In this we are accessing getAllHRDetails() static method from LdapConfig
     * to fetch the list of recruiters
     *
     * @return List of recruiters
     */
    @GetMapping("/recruiters")
    public List<RecruiterDto> getAllEmployees() {
        logger.info("Fetching HR details from LDAP");
        return LdapConfig.getAllHRDetails();
    }
}
