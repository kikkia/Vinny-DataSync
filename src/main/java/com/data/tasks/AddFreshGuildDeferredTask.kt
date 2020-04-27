package com.data.tasks

import com.data.db.dao.ChannelDAO
import com.data.db.dao.GuildDAO
import com.data.db.dao.MembershipDAO
import com.data.utils.DislogLogger
import com.mewna.catnip.entity.channel.TextChannel
import com.mewna.catnip.entity.channel.VoiceChannel
import com.mewna.catnip.entity.guild.Guild

/**
 * Deferred task to delegate adding new guilds to the db and send a welcome message on a separate thread.
 */
class AddFreshGuildDeferredTask(private val guild: Guild,
                                private val guildDAO: GuildDAO,
                                private val membershipDAO: MembershipDAO,
                                private val channelDAO: ChannelDAO) : Thread() {

    private val logger = DislogLogger(AddFreshGuildDeferredTask::class.java)

    override fun run() {
        logger.info("Joining guild: " + guild.name() + " with " + guild.members().size() + " members")

        guildDAO.addFreshGuild(guild)

        for (m in guild.members()) {
            membershipDAO.addUserToGuild(m.user(), guild)
        }
        for (t in guild.channels().filter {channel -> channel.isText}) {
            channelDAO.addTextChannel(t as TextChannel)
        }
        for (v in guild.channels().filter { channel -> channel.isVoice }) {
            channelDAO.addVoiceChannel(v as VoiceChannel)
        }

        logger.info("Completed addition of fresh guild. " + guild.id())
    }
}
