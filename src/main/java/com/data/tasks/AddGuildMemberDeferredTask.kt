package com.data.tasks

import com.data.db.dao.MembershipDAO
import com.mewna.catnip.entity.guild.Member

/**
 * Deferred task to delegate adding new users to guilds
 */
class AddGuildMemberDeferredTask(private val member: Member,
                                 private val membershipDAO: MembershipDAO) : Thread() {

    override fun run() {
        membershipDAO.addUserToGuild(member.user(), member.guild())
    }
}
