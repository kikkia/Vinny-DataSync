package com.data.db.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ScheduledCommandDAO {

    @Autowired
    private HikariDataSource write;

    public void removeAllScheduledInGuild(String guildId) throws SQLException {
        String query = "DELETE FROM scheduled_command WHERE guild = ?";
        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, guildId);
                statement.execute();
            }
        }
    }

    public void removeAllScheduledInChannel(String channelId) throws SQLException {
        String query = "DELETE FROM scheduled_command WHERE channel = ?";
        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, channelId);
                statement.execute();
            }
        }
    }
}
