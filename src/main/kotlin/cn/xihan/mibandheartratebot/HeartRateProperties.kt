package cn.xihan.mibandheartratebot

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "heart-rate")
data class HeartRateProperties(
    @Value("\${heart-rate.enable}")
    val enable: Boolean = false,
    @Value("\${heart-rate.threshold}")
    val threshold: Int = 0,
    val heartRateStates: List<HeartRateState>,
    val heartRateTrend: List<HeartRateTrend>,
    val groups: List<Long>
)

data class HeartRateState(
    val minHeartRate: Int,
    val maxHeartRate: Int,
    val descriptions: List<String>
)

data class HeartRateTrend(
    val trend: String,
    val descriptions: List<String>
)


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HeartRateProperties::class)
class HeartRateConfiguration