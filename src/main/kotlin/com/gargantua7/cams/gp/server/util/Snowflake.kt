package com.gargantua7.cams.gp.server.util

import org.slf4j.LoggerFactory

/**
 * 雪花算法代码实现
 * @author Twitter
 */
class Snowflake(
    private val workerId: Long, // 数据中心(机房) id
    private val datacenterId: Long, // 机器ID
    private var sequence: Long = 0 // 同一时间的序列
) {
    // 开始时间戳（2022-01-01 00:00:00 UTC）
    private val twepoch = 1640995200000L

    // 机房号的ID所占的位数 5个bit 最大:11111(2进制)--> 31(10进制)
    private val datacenterIdBits = 5L

    // 机器ID所占的位数 5个bit 最大:11111(2进制)--> 31(10进制)
    private val workerIdBits = 5L

    // 5 bit最多只能有31个数字，就是说机器id最多只能是32以内
    private val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())

    // 5 bit最多只能有31个数字，机房id最多只能是32以内
    private val maxDatacenterId = -1L xor (-1L shl datacenterIdBits.toInt())

    // 同一时间的序列所占的位数 12个bit 111111111111 = 4095  最多就是同一毫秒生成4096个
    private val sequenceBits = 12L

    // workerId的偏移量
    private val workerIdShift = sequenceBits

    // datacenterId的偏移量
    private val datacenterIdShift = sequenceBits + workerIdBits

    // timestampLeft的偏移量
    private val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits

    // 序列号掩码 4095 (0b111111111111=0xfff=4095)
    // 用于序号的与运算，保证序号最大值在0-4095之间
    private val sequenceMask = -1L xor (-1L shl sequenceBits.toInt())

    // 最近一次时间戳
    private var lastTimestamp = -1L

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        // 合法判断
        require(!(workerId > maxWorkerId || workerId < 0)) {
            "worker Id can't be greater than $maxWorkerId or less than 0"
        }
        require(!(datacenterId > maxDatacenterId || datacenterId < 0)) {
            "datacenter Id can't be greater than $maxDatacenterId or less than 0"
        }
        logger.info(
            "worker starting. timestamp left shift $timestampLeftShift, datacenter id bits $datacenterIdBits, " +
                    "worker id bits $workerIdBits, sequence bits $sequenceBits, workerId $workerId"
        )
    }

    // 获取下一个随机的ID
    @Synchronized
    fun nextId(): Long {
        // 获取当前时间戳，单位毫秒
        var timestamp = timeGen()
        if (timestamp < lastTimestamp) {
            logger.info("clock is moving backwards.  Rejecting requests until $lastTimestamp.")
            throw RuntimeException(
                "Clock moved backwards.  Refusing to generate id for ${lastTimestamp - timestamp} milliseconds"
            )
        }

        // 去重
        if (lastTimestamp == timestamp) {
            sequence = sequence + 1 and sequenceMask

            // sequence序列大于4095
            if (sequence == 0L) {
                // 调用到下一个时间戳的方法
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            // 如果是当前时间的第一次获取，那么就置为0
            sequence = 0
        }

        // 记录上一次的时间戳
        lastTimestamp = timestamp

        // 偏移计算
        return timestamp - twepoch shl timestampLeftShift.toInt() or
                (datacenterId shl datacenterIdShift.toInt()) or
                (workerId shl workerIdShift.toInt()) or
                sequence
    }

    private fun tilNextMillis(lastTimestamp: Long): Long {
        // 获取最新时间戳
        var timestamp = timeGen()
        // 如果发现最新的时间戳小于或者等于序列号已经超4095的那个时间戳
        while (timestamp <= lastTimestamp) {
            // 不符合则继续
            timestamp = timeGen()
        }
        return timestamp
    }

    private fun timeGen(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        val instance by lazy { Snowflake(0, 0) }
    }
}

