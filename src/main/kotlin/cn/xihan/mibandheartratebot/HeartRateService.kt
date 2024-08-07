package cn.xihan.mibandheartratebot

import org.springframework.stereotype.Service

@Service
class HeartRateService {

    // 定义一个Pair类型的变量，用于存储最新的心率数据
    private var latestHeartRate: Pair<Long, Int> = 0L to 0

    // 更新最新的心率数据
    fun updateHeartRate(measuredAt: Long, heartRate: Int) {
        latestHeartRate = measuredAt to heartRate
    }

    // 获取最新的心率数据
    fun getLatestHeartRate(): Pair<Long, Int> {
        return latestHeartRate
    }
}