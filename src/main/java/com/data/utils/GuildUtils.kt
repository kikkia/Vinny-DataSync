package com.data.utils

import com.mewna.catnip.entity.guild.Guild
import com.mewna.catnip.entity.guild.Role

object GuildUtils {
    @JvmStatic
    fun getHighestRole(guild: Guild): Role? {
        var highest: Role? = null
        for (r in guild.roles()) {
            if (highest == null) highest = r else if (highest.position() < r.position()) highest = r
        }
        return highest
    }
}