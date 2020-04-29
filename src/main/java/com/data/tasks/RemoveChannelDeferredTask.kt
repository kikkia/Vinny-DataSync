package com.data.tasks

import com.data.db.dao.ChannelDAO
import com.mewna.catnip.entity.channel.Channel

class RemoveChannelDeferredTask(private val channel: Channel,
                                private val channelDAO: ChannelDAO): Thread() {

    override fun run() {
        if (!channel.isGuild)
            return

        if (channel.isText)
            channelDAO.removeTextChannel(channel.asTextChannel())
        if (channel.isVoice)
            channelDAO.removeVoiceChannel(channel.asVoiceChannel())
    }
}