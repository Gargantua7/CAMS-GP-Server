package com.gargantua7.cams.gp.server.model.vo

/**
 * @author Gargantua7
 */
data class NewRepairModel(
    val title: String,
    val content: String,
    val private: Boolean = false
) {
    init {
        require(title.length !in 1..20) { "Title Too Long Or Too Short" }
    }
}
