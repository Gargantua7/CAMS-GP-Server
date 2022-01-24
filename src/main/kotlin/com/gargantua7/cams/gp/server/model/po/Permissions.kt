package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Permission
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author Gargantua7
 */
object Permissions : BaseTable<Permission>("permission") {

    val level = int("level").primaryKey()
    val title = varchar("title")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Permission(
        row[level] ?: 0,
        row[title].orEmpty()
    )
}
