package com.gargantua7.cams.gp.server.config

import org.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


/**
 * @author Gargantua7
 */
@Configuration
class Config {

    @Bean
    fun database(@Autowired dataSource: DataSource) = Database.connectWithSpringSupport(dataSource)

}
