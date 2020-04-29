package com.data.tasks

import com.data.db.dao.ChannelDAO
import com.mewna.catnip.entity.channel.Channel

class AddChannelDeferredTask (private val channel: Channel,
                              private val channelDAO: ChannelDAO) : Thread() {

    override fun run() {
        if (!channel.isGuild) {
            return
        }
        if (channel.isVoice)
            channelDAO.addVoiceChannel(channel.asVoiceChannel())
        if (channel.isText)
            channelDAO.addTextChannel(channel.asTextChannel())
    }
}