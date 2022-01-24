package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Major
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.varchar

/**
 * @author Gargantua7
 */
object Majors : BaseTable<Major>("major") {

    val id = varchar("major_id").primaryKey()
    val name = varchar("major_name")
    val collageId = varchar("collage_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Major(
        row[id].orEmpty(),
        row[name].orEmpty(),
        row[collageId].orEmpty()
    )

}
