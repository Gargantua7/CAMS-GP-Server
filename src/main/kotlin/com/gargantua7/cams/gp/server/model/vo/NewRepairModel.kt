package com.gargantua7.cams.gp.server.model.vo

import com.gargantua7.cams.gp.server.exception.BadRequestException

/**
 * @author Gargantua7
 */
data class NewRepairModel(
    val title: String,
    val content: String,
    val private: Boolean = false
) {
    fun require() {
        if (title.length !in 1..20)
            throw BadRequestException.RequestParamFormatException("Title Too Long Or Too Short")
    }
}
