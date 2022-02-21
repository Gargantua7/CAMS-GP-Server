package com.gargantua7.cams.gp.server.util

/**
 * @author Gargantua7
 */
class ListResponse<E>(val list: List<E>)

val <E> List<E>.response: ListResponse<E> get() = ListResponse(this@response)
