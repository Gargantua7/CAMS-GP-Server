package com.gargantua7.cams.gp.server.dao

import com.gargantua7.cams.gp.server.model.dto.Repair
import org.apache.shiro.crypto.hash.Sha256Hash
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


/**
 * @author Gargantua7
 */
@SpringBootTest
class PersonDaoTest {

    @Autowired
    private lateinit var repairDao: RepairDao

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun selectPersonByUsername() {
//        logger.info(Gson().toJson(personDao.selectPersonByUsername("10001")))
        val password = "123456"
        val random = Random().nextInt().toString()
        logger.info("f775ea89e68fffa1e398b7746691ec0c119615fd154b259d579d1f67ecae3429".length.toString())
        val hash = Sha256Hash(password, random, 10)
        logger.info(hash.toString())
    }

    @Test
    fun uuid() {
        repairDao.insert(Repair(
            title = "Test",
            content = "Test",
            initiator = "10001"
        ))
    }

}
