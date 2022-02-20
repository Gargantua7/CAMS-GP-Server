package com.gargantua7.cams.gp.server.util

/**
 * @author Gargantua7
 */
val <E> List<E>.response: Any get() = object {
    val list = this@response
}
