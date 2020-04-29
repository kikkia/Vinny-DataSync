package com.data.db.dao;

import com.data.utils.DislogLogger;
import com.data.utils.GuildUtils;
import com.mewna.catnip.entity.channel.Channel;
import com.mewna.catnip.entity.channel.GuildChannel;
import com.mewna.catnip.entity.channel.TextChannel;
import com.mewna.catnip.entity.channel.VoiceChannel;
import com.mewna.catnip.entity.guild.Guild;
import com.mewna.catnip.entity.guild.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Component
public class GuildDAO {
    private static final DislogLogger logger = new DislogLogger(GuildDAO.class);
    private final int DEFAULT_VOLUME = 100;

    @Autowired
    private HikariDataSource write;

    @Autowired
    private MembershipDAO membershipDAO;

    @Autowired
    private ChannelDAO channelDAO;

    // We throw on this one so if we cant add a guild to the db we just leave the guild to avoid greater problems
    private boolean addGuild(Guild guild) {
        String query = "INSERT INTO guild(id, name, default_volume, min_base_role_id, min_mod_role_id, min_nsfw_role_id, min_voice_role_id, active) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE active=1";
        try (Connection connection = write.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, guild.id());
                statement.setString(2, guild.name());
                statement.setInt(3, DEFAULT_VOLUME);

                statement.setString(4, guild.role(guild.idAsLong()).id());
                statement.setString(5, GuildUtils.getHighestRole(guild).id());
                statement.setString(6, guild.role(guild.idAsLong()).id());
                statement.setString(7, guild.role(guild.idAsLong()).id());
                statement.setBoolean(8, true);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to add guild to db: " + guild.id() + " " + e.getMessage());
            return false;
        }
        return true;
    }

    public void addFreshGuild(Guild guild) {

        addGuild(guild);
        for (Member m : guild.members()) {
            membershipDAO.addUserToGuild(m.user(), guild);
        }
        for (GuildChannel t : guild.channels().stream().filter(Channel::isText).collect(Collectors.toList())) {
            channelDAO.addTextChannel((TextChannel) t);
        }
        for (GuildChannel v : guild.channels().stream().filter(c -> !c.isText()).collect(Collectors.toList())) {
            channelDAO.addVoiceChannel((VoiceChannel) v);
        }

        logger.info("Completed addition of fresh guild. " + guild.id());
    }

    public void setGuildActive(String guildId, boolean active) {
        String query = "UPDATE guild SET active = ? WHERE id = ?";
        try (Connection connection = write.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setBoolean(1, active);
                statement.setString(2, guildId);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to update active flag for guild: " + guildId + " " + e.getMessage());
        }
    }
}
