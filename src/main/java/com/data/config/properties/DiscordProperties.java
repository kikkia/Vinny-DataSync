package com.data.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private String token;
    private String ownerId;
    private String botId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }
}
