package com.data.utils;

import com.mewna.catnip.entity.guild.Guild;
import com.mewna.catnip.entity.guild.Role;

public class GuildUtils {

    public static Role getHighestRole(Guild guild) {
        Role highest = null;
        for ( Role r : guild.roles()) {
            if (highest == null)
                highest = r;
            else if (highest.position() < r.position())
                highest = r;
        }
        return highest;
    }
}
