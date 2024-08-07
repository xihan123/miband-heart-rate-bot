package cn.xihan.mibandheartratebot

import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HeartRateController(
    private val heartRateService: HeartRateService
) {

    // 接收数据
    @PostMapping("/receive_data")
    fun receiveData(@RequestBody string: String): ResponseEntity<Map<String, Any>> {
        // 将接收到的字符串转换为JSON对象
        val json = JSONObject(string)
        // 从JSON对象中获取心率数据
        val heartRate = json.optJSONObject("data").optInt("heart_rate")
        // 从JSON对象中获取测量时间
        val measurementAt = json.optLong("measured_at")
        // 更新心率数据
        heartRateService.updateHeartRate(measurementAt, heartRate)
        // 返回成功信息
        return ResponseEntity(mapOf("message" to "ok"), HttpStatus.OK)
    }

    // 获取最新心率数据
    @GetMapping("/latest-heart-rate")
    fun getData(): ResponseEntity<Map<String, Any>> {
        // 获取最新心率数据
        val latestHeartRate = heartRateService.getLatestHeartRate()
        // 返回最新心率数据
        return ResponseEntity(
            mapOf(
                "timestamp" to latestHeartRate.first, "heartRate" to latestHeartRate.second
            ), HttpStatus.OK
        )
    }

    /**
     * 返回服务状态
     */
    @GetMapping("/")
    fun index(): String {
        return "Service is running"
    }

}