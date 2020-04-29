package com.data.db.models

import com.data.utils.CommandCategories
import com.jagrosh.jdautilities.command.Command
import java.util.*

class InternalGuild(var id: String, var name: String, minBaseRole: String, minModRole: String, minNsfwRole: String, minVoiceRole: String, prefixes: String?, active: Boolean) {
    private val roleRequirements: MutableMap<Command.Category, String>
    val prefixes: String?
    var isActive: Boolean

    fun getRequiredPermission(category: Command.Category): String? { // Overriding the permissions for reddit and meme commands with the ones for GENERAL
        return if (category.name == "reddit" || category.name == "meme") {
            roleRequirements[CommandCategories.GENERAL]
        } else roleRequirements[category]
    }

    val prefixList: ArrayList<String>
        get() = if (prefixes == null) ArrayList() else ArrayList(listOf(*prefixes.split(" ").toTypedArray()))

    init {
        roleRequirements = HashMap()
        roleRequirements[CommandCategories.GENERAL] = minBaseRole
        roleRequirements[CommandCategories.MODERATION] = minModRole
        roleRequirements[CommandCategories.NSFW] = minNsfwRole
        roleRequirements[CommandCategories.VOICE] = minVoiceRole
        this.prefixes = prefixes
        isActive = active
    }
}