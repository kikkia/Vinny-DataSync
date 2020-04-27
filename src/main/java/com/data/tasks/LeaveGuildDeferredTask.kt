package com.data.tasks

import com.data.db.dao.GuildDAO
import com.data.db.dao.MembershipDAO
import com.data.db.dao.ScheduledCommandDAO
import com.data.utils.DislogLogger
import com.mewna.catnip.entity.guild.Guild

class LeaveGuildDeferredTask(private val guild: Guild,
                             private val membershipDAO: MembershipDAO,
                             private val guildDAO: GuildDAO,
                             private val scheduledCommandDAO: ScheduledCommandDAO) : Thread() {

    private val logger: DislogLogger = DislogLogger(this.javaClass)

    override fun run() {
        logger.info("Kicking off Leave guild task for ${guild.name()}")

        for (m in guild.members()) {
            membershipDAO.removeUserMembershipToGuild(m.user().id(), guild.id())
        }

        guildDAO.setGuildActive(guild.id(), false)
        scheduledCommandDAO.removeAllScheduledInGuild(guild.id())

        logger.info("Left guild: " + guild.name() + " with " + guild.members().size() + " members")
    }
}
