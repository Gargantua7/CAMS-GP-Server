package com.gargantua7.cams.gp.server.model.po

import com.gargantua7.cams.gp.server.model.dto.Person
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * @author Gargantua7
 */
object Persons : Table<PersonEntity>("person") {

    val username = varchar("username").primaryKey().bindTo { it.username }
    val name = varchar("name").bindTo { it.name }
    val majorId = varchar("major_id").bindTo { it.majorId }
    val depId = int("dep_id").bindTo { it.depId }
    val permissionLevel = int("permission_level").bindTo { it.permissionLevel }
    val phone = varchar("phone").bindTo { it.phone }
    val wechat = varchar("wechat").bindTo { it.wechat }

}

interface PersonEntity : Entity<PersonEntity> {

    companion object : Entity.Factory<PersonEntity>()

    var username: String
    var name: String
    var majorId: String
    var depId: Int
    var permissionLevel: Int
    var phone: String?
    var wechat: String?

    val value get() = Person(this)
}
