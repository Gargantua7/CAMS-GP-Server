package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Collage
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.varchar

/**
 * @author Gargantua7
 */
object Collages : BaseTable<Collage>("collage") {

    val id = varchar("collage_id").primaryKey()
    val name = varchar("collage_name")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Collage(
        row[id].orEmpty(),
        row[name].orEmpty()
    )
}

