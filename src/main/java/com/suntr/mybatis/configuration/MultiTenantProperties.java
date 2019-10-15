package com.suntr.mybatis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix = "tenant")
public class MultiTenantProperties {


    private Properties properties = new Properties();

    public Properties getProperties() {
        return this.properties;
    }

    public String getColumn() {
        return this.properties.getProperty("table.column");
    }

    public void setColumn(String column) {
        this.properties.setProperty("table.column", column);
    }

}
