package com.data.db.dao;


import com.data.utils.DislogLogger;
import com.mewna.catnip.entity.guild.Guild;
import com.mewna.catnip.entity.user.User;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class MembershipDAO {
    private static final DislogLogger logger = new DislogLogger(MembershipDAO.class);

    @Autowired
    private HikariDataSource write;

    public void addUserToGuild(User user, Guild guild) {
        String userInsertQuery = "INSERT INTO users (id, name) VALUES(?,?) ON DUPLICATE KEY UPDATE name = name";
        String membershipInsertQuery = "INSERT INTO guild_membership (guild, user_id, can_use_bot) VALUES(?,?,?) ON DUPLICATE KEY UPDATE user_id = user_id";
        addUser(userInsertQuery, user);
        addMembership(membershipInsertQuery, user, guild);
    }

    private void addMembership(String membershipInsertQuery, User user, Guild guild) {
        try (Connection connection = write.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(membershipInsertQuery)) {
                statement.setString(1, guild.id());
                statement.setString(2, user.id());
                statement.setBoolean(3, true);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to add membership for user to guild: " + e.getMessage());
        }
    }

    private void addUser(String userInseryQuery, User user) {
        try (Connection connection = write.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(userInseryQuery)) {
                statement.setString(1, user.id());
                statement.setString(2, user.id());
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to add user to db: " +e.getMessage());
        }
    }

    public void removeUserMembershipToGuild(String userId, String guildId) {
        String query = "DELETE FROM guild_membership WHERE guild = ? AND user_id = ?";
        try (Connection connection = write.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, guildId);
                statement.setString(2, userId);
                statement.execute();
            }
        } catch (SQLException e) {
            logger.error("Failed to remove user membership from guild. " + e.getMessage());
        }
    }
}
