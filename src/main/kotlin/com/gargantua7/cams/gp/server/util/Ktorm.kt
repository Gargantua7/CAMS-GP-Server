package com.gargantua7.cams.gp.server.util

import com.gargantua7.cams.gp.server.exception.ForbiddenException
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.add
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Table
import org.springframework.dao.DuplicateKeyException

/**
 * @author Gargantua7
 */
fun <E : Entity<E>, T : Table<E>> EntitySequence<E, T>.addIfAbsent(entity: E): Int {
    try {
        return add(entity)
    } catch (e: DuplicateKeyException) {
        throw ForbiddenException("Resource Already Exists")
    }
}
