package com.data.db.models

class InternalGuildMembership(var id: String?, var guildId: String?, private var canUseBot: Boolean) {
    var name: String? = null

    fun canUseBot(): Boolean {
        return canUseBot
    }

    fun setCanUseBot(canUseBot: Boolean) {
        this.canUseBot = canUseBot
    }
}
