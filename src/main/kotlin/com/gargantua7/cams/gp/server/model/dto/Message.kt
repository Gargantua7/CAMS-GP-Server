package com.gargantua7.cams.gp.server.model.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
data class Message(
    val sender: String,
    val recipient: String,
    val type: Type = Type.NORMAL,
    val content: String,
    val time: LocalDateTime = LocalDateTime.now()
): Serializable {

    enum class Type(value: String) {
        NORMAL("normal"),
        REPAIR("repair"),
        REPLY("reply"),
        EVENT("event")
    }

}
