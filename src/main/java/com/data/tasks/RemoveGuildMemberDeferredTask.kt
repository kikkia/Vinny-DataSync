package com.data.tasks

import com.data.db.dao.MembershipDAO
import com.mewna.catnip.entity.guild.Member

/**
 * Deferred task to delegate removing a user from a guild
 */
class RemoveGuildMemberDeferredTask(private val member: Member,
                                    private val membershipDAO: MembershipDAO) : Thread() {

    override fun run() {
        membershipDAO.removeUserMembershipToGuild(member.user().id(), member.guild().id())
    }
}