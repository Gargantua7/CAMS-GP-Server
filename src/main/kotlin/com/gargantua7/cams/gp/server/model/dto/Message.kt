package com.gargantua7.cams.gp.server.model.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author Gargantua7
 */
sealed class Message(
    val sender: String,
    val recipient: String,
    val type: String,
    val content: String,
    val time: LocalDateTime = LocalDateTime.now()
) : Serializable {

    class Normal(sender: String, recipient: String, content: String) : Message(sender, recipient, "Normal", content)

    class Repair(recipient: String, id: Long) : Message("10001", recipient, "Repair", id.toString())

    class Reply(sender: String, recipient: String, id: Long) : Message(sender, recipient, "Reply", id.toString())

    class Event(recipient: String, id: Long) : Message("10001", recipient, "Reply", id.toString())
}
