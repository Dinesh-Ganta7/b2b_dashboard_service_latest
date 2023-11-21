package com.stg.b2b.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * For reading the properties from application.properties
 * use those properties wherever required
 */
@Configuration
public class PropertiesConfig {

    @Value("${orders.folder.path}")
    private String folderPath;

    /**
     * Getting the folderPath
     * @return folderPath as String
     */
    public String getFolderPath() {
        return folderPath;
    }
}
