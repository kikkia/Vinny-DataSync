package com.data.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "db")
public class DBProperties {

    private String uri;
    private String user;
    private String password;
    private String schema;
    private int poolSize;


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
