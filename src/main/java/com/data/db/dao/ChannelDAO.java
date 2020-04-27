package com.data.db.dao;

import com.data.utils.DislogLogger;
import com.mewna.catnip.entity.channel.TextChannel;
import com.mewna.catnip.entity.channel.VoiceChannel;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ChannelDAO {
    private static final DislogLogger logger = new DislogLogger(ChannelDAO.class);

    @Autowired
    private HikariDataSource write;

    @Autowired
    private AliasDAO aliasDAO;

    public void addVoiceChannel(VoiceChannel voiceChannel) {
        String query = "INSERT INTO voice_channel(id, guild, name) VALUES (?,?,?) ON DUPLICATE KEY UPDATE name=VALUES(name)";
        try (Connection connection = write.getConnection()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voiceChannel.id());
                preparedStatement.setString(2, voiceChannel.guild().id());
                preparedStatement.setString(3, voiceChannel.name());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to add voice channel to the db " +e.getMessage());
        }
    }

    public void addTextChannel(TextChannel textChannel) {
        String query = "INSERT INTO text_channel(id, guild, name, nsfw_enabled) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE nsfw_enabled=VALUES(nsfw_enabled)";

        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, textChannel.id());
                statement.setString(2, textChannel.guildId());
                statement.setString(3, textChannel.name());
                statement.setBoolean(4, textChannel.nsfw());
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to add text channel to the db: " + e.getMessage());
        }
    }

    public void removeVoiceChannel(VoiceChannel channel) {
        String query = "DELETE FROM voice_channel WHERE id = ?";
        try (Connection connection = write.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, channel.id());
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to remove a voice channel from db: " +e.getMessage());
        }
    }

    public void removeTextChannel(TextChannel channel) {
        String query = "DELETE FROM text_channel WHERE id = ?";

        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, channel.id());
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to remove a text channel from db: " +e.getMessage());
        }
    }

    // Updates a voice channels enabled status, creates the channel as a failsafe
    public boolean setVoiceChannelEnabled(VoiceChannel channel, boolean enabled) {
        String query = "INSERT INTO voice_channel(id, name, guild, voice_enabled) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE voice_enabled = VALUES(voice_enabled)";

        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, channel.id());
                statement.setString(2, channel.name());
                statement.setString(3, channel.guildId());
                statement.setBoolean(4, enabled);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to update channel voice: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean setTextChannelNSFW(TextChannel textChannel, boolean enabled) {
        String query = "INSERT INTO text_channel (id, name, guild, nsfw_enabled) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE nsfw_enabled = VALUES(nsfw_enabled)";

        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, textChannel.id());
                statement.setString(2, textChannel.name());
                statement.setString(3, textChannel.guildId());
                statement.setBoolean(4, enabled);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to update channel nsfw: " + e.getMessage());
            return false;
        }
        return true;
    }
}
