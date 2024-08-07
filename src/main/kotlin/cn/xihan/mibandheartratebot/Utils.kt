package cn.xihan.mibandheartratebot

import com.alibaba.fastjson2.annotation.JSONField
import com.alibaba.fastjson2.parseObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 配置文件
 */
private val configFile by lazy {
    File("config.json").apply {
        if (!exists()) createNewFile()
    }
}

private val configModel by lazy { configFile.readText().parseObject<ConfigModel>() }

// 定义一个延迟初始化的变量，用于存储configModel.heartRate.enable的值
val heartRateEnable by lazy { configModel.heartRate.enable }
// 定义一个延迟初始化的变量，用于存储configModel.heartRate.threshold的值
val heartRateThreshold by lazy { configModel.heartRate.threshold }
// 定义一个延迟初始化的变量，用于存储configModel.heartRate.heartRateStates的值
val heartRateStates by lazy { configModel.heartRate.heartRateStates }
// 定义一个延迟初始化的变量，用于存储configModel.heartRate.heartRateTrend的值
val heartRateTrends by lazy { configModel.heartRate.heartRateTrend }
// 定义一个延迟初始化的变量，用于存储configModel.groups的值
val groups by lazy { configModel.groups }


/**
 * 根据心率确定心率状态
 */
fun determineHeartRateState(heartRate: Int): String {
    // 在心率状态列表中查找与当前心率匹配的状态
    val state = heartRateStates.firstOrNull { heartRate in it.minHeartRate..it.maxHeartRate }
    // 返回匹配状态中的随机状态，如果没有匹配状态则返回"未知状态"
    return state?.states?.random() ?: "未知状态"
}

/**
 * 上次心率
 */
private var innerPreviousHeartRate = 70


/**
 * 根据当前心率、前一次心率、阈值判断心率趋势
 * @param currentHeartRate 当前心率
 * @param previousHeartRate 前一次心率
 * @param threshold 阈值
 */
fun determineHeartRateTrend(
    currentHeartRate: Int, previousHeartRate: Int = innerPreviousHeartRate, threshold: Int = heartRateThreshold
): String {
//    println("currentHeartRate: $currentHeartRate, previousHeartRate: $previousHeartRate, threshold: $threshold")
    // 根据当前心率、前一次心率、阈值判断心率趋势
    val trend = when {
        currentHeartRate >= (previousHeartRate + threshold) -> heartRateTrends.firstOrNull { it.trend == "上升" }
        currentHeartRate <= (previousHeartRate - threshold) -> heartRateTrends.firstOrNull { it.trend == "下降" }
        else -> heartRateTrends.firstOrNull { it.trend == "稳定" }
    }
    // 更新前一次心率
    innerPreviousHeartRate = currentHeartRate
    // 返回随机的心率趋势描述
    return trend?.descriptions?.random() ?: "未知趋势"
}

/**
 * 将Long类型的时间戳格式化为字符串
 */
fun Long.formatTimestamp(): String {
    // 将Long类型的时间戳转换为Date类型
    val date = Date(this)
    // 创建SimpleDateFormat对象，指定日期格式
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    // 设置时区为默认时区
    format.timeZone = TimeZone.getDefault()
    // 返回格式化后的日期字符串
    return format.format(date)
}

data class ConfigModel(
    @JSONField(name = "groups")
    var groups: List<Long> = listOf(),
    @JSONField(name = "heart_rate")
    var heartRate: HeartRateModel = HeartRateModel()
) {
    data class HeartRateModel(
        @JSONField(name = "enable")
        var enable: Boolean = false,
        @JSONField(name = "heart_rate_states")
        var heartRateStates: List<HeartRateStateModel> = listOf(),
        @JSONField(name = "heart_rate_trend")
        var heartRateTrend: List<HeartRateTrendModel> = listOf(),
        @JSONField(name = "threshold")
        var threshold: Int = 0
    ) {
        data class HeartRateStateModel(
            @JSONField(name = "maxHeartRate")
            var maxHeartRate: Int = 0,
            @JSONField(name = "minHeartRate")
            var minHeartRate: Int = 0,
            @JSONField(name = "states")
            var states: List<String> = listOf()
        )

        data class HeartRateTrendModel(
            @JSONField(name = "descriptions")
            var descriptions: List<String> = listOf(),
            @JSONField(name = "trend")
            var trend: String = ""
        )
    }
}
