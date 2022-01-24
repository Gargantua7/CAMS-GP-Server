package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Department
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author Gargantua7
 */
object Departments : BaseTable<Department>("Department") {

    val id = int("dep_id").primaryKey()
    val name = varchar("dep_name")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Department(
        row[id] ?: 0,
        row[name].orEmpty()
    )
}
