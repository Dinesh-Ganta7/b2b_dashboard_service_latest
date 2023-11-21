package com.stg.b2b.config;

import com.stg.b2b.security.RecruiterDto;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapConfig {

    private static final Logger logger = LoggerFactory.getLogger(LdapConfig.class);

     LdapConfig() {
    }

    /**
     * Fetching all the recruiters by calling the required methods
     * so that we can access the details externally
     *
     * @return List of recruiters
     */
    public static List<RecruiterDto> getAllHRDetails() {
        LdapContext ldapContext = getLdapContext();
        SearchControls searchControls = getSearchControls();
        return getAllHREmployees(ldapContext, searchControls);
    }

    /**
     * Establishing LDAP Connection by setting required properties
     *
     * @return ldapContext
     *
     */
    private static LdapContext getLdapContext() {
        InitialLdapContext ctx = null;

        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
            env.put("java.naming.security.authentication", "Simple");
            env.put("java.naming.security.principal", "CN=Board Room,OU=ITIS,dc=stg,dc=com");
            env.put("java.naming.security.credentials", "Reset123");
            env.put("java.naming.provider.url", "ldap://stg.com:3268/DC=stg,DC=com");
            ctx = new InitialLdapContext(env, (Control[]) null);
            logger.info("LDAP Connection: COMPLETE");
        }
        catch (NamingException var2) {
            logger.error("LDAP Connection: FAILED");
            var2.printStackTrace();
        }

        return ctx;
    }

    /**
     * Fetching all the recruiters after establishing LDAP connection
     *
     * @param ldapContext
     * @param searchControls
     * @return List of Recruiters
     *
     */
    private static List<RecruiterDto> getAllHREmployees( LdapContext ldapContext, SearchControls searchControls) {
        List<RecruiterDto> res = new ArrayList<>();
        try {
            NamingEnumeration<SearchResult> values = ldapContext.search("ldap://stg.com:3268/ou=STG India,dc=stg,dc=com",
                    "(sAMAccountName=*)"  , searchControls);

            StringBuilder titleBuilder = new StringBuilder("HR & Talent Acquisition");
            Set<Object> enumerations = new HashSet<>();
            while (values.hasMoreElements()) {
                SearchResult result = values.next();
                Attributes attribs = result.getAttributes();
                getHRList(titleBuilder, enumerations, attribs);
            }
            for (Object val : enumerations) {
                JSONObject jsonObject = (JSONObject) val;
                if (jsonObject.keySet().contains("givenName")) {
                    res.add(new RecruiterDto(jsonObject.getString("givenName"), "HR & Talent Acquisition", jsonObject.getString("sAMAccountName")));
                }
            }


        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return res;

    }

    private static void getHRList(StringBuilder titleBuilder, Set<Object> enumerations, Attributes attribs) throws NamingException {
        if (attribs != null) {
            String atributeName = "title";
            Attribute value = attribs.get(atributeName);
            if (value != null && value.toString().contains(titleBuilder)) {
                List<JSONObject> jsonObjects = new ArrayList<>();
                JSONObject currentObject = new JSONObject();
                for (NamingEnumeration ae = attribs.getAll(); ae.hasMoreElements(); ) {
                    Attribute atr = (Attribute) ae.next();
                    String[] keyValue = atr.toString().split(":");

                    currentObject.put(keyValue[0], keyValue[1].trim());

                    if (keyValue[0].equals(atributeName)) {
                        jsonObjects.add(new JSONObject(currentObject.toString()));
                        currentObject = new JSONObject();
                    }
                }
                for (JSONObject jsonObject : jsonObjects) {
                    enumerations.add(jsonObject);
                }

            }
        }
    }

    /**
     * Here we are setting the attributes to the SearchControls
     * So that we can search based on that attributes
     *
     * @return SearchControls with given Attributes
     */
    private static SearchControls getSearchControls() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(2);
        String[] attrIDs = new String[] { "givenname", "sAMAccountName", "title" };
        searchControls.setReturningAttributes(attrIDs);
        return searchControls;
    }

}
