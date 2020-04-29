package com.data.utils

import com.jagrosh.jdautilities.command.Command

object CommandCategories {
    val VOICE = Command.Category("voice")
    val GENERAL = Command.Category("general")
    val NSFW = Command.Category("nsfw")
    val MODERATION = Command.Category("moderation")
    // Derivatives of General Category for more granularity
    val REDDIT = Command.Category("reddit")
    val MEME = Command.Category("meme")
    val OWNER = Command.Category("owner")
}