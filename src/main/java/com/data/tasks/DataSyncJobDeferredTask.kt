package com.data.tasks

import com.data.db.dao.ChannelDAO
import com.data.db.dao.GuildDAO
import com.data.db.dao.MembershipDAO
import com.data.utils.DislogLogger
import com.data.utils.FormattingUtils.msToMinSec
import com.mewna.catnip.Catnip

class DataSyncJobDeferredTask(private val catnip: Catnip,
                              private val channelDAO: ChannelDAO,
                              private val guildDAO: GuildDAO,
                              private val membershipDAO: MembershipDAO) : Thread() {

    private val logger = DislogLogger(DataSyncJobDeferredTask::class.java)

    override fun run() {
        var guildCount = 0
        val startTime = System.currentTimeMillis()

        for (guild in catnip.cache().guilds()) {
            if ((guildCount % 100) == 0) {
                logger.info("DATASYNC PROGRESS: Syncing guild $guildCount")
            }

            // Ensure guild is in the db
            guildDAO.addFreshGuild(guild)

            // Ensure all memberships exist
            for (member in guild.members()) {
                membershipDAO.addUserToGuild(member.user(), guild)
            }

            // Ensure channels exist
            for (channel in guild.channels()) {
                if (channel.isVoice)
                    channelDAO.addVoiceChannel(channel.asVoiceChannel())
                if (channel.isText)
                    channelDAO.addTextChannel(channel.asTextChannel())
            }

            guildCount++
        }
        val totalTime = msToMinSec(System.currentTimeMillis() - startTime)

        logger.info("DATASYNC PROGRESS: Finished sync of $guildCount guilds. Elapsed time: $totalTime")
    }
}