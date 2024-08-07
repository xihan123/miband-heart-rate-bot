package cn.xihan.mibandheartratebot

import com.mikuac.shiro.annotation.GroupMessageHandler
import com.mikuac.shiro.annotation.MessageHandlerFilter
import com.mikuac.shiro.annotation.common.Shiro
import com.mikuac.shiro.common.utils.MsgUtils
import com.mikuac.shiro.core.Bot
import com.mikuac.shiro.dto.event.message.GroupMessageEvent
import org.springframework.stereotype.Component
import java.util.regex.Matcher

@Shiro
@Component
class Group(
    private val heartRateService: HeartRateService
) {

    // 处理群组消息，当消息内容为“查询心率”时触发
    @GroupMessageHandler
    // 消息过滤器，当消息内容为“查询心率”时触发
    @MessageHandlerFilter(
        cmd = "查询心率",
    )
    fun fun0(
        // 机器人对象
        bot: Bot,
        // 群组消息事件
        event: GroupMessageEvent,
        // 匹配器
        matcher: Matcher
    ) {
        // 如果心率功能未启用，则返回
        if (!heartRateEnable) return
        // 获取群组ID
        val groupId = event.groupId
        // 如果群组ID不在允许的群组列表中，则返回
        if (groupId !in groups) return
        // 获取匹配到的内容
        val action = matcher.group()
//        println("action: $action")
        // 如果匹配到的内容为空，则返回
        if (action.isNullOrBlank()) return
        // 获取消息ID
        val messageId = event.messageId
        // 获取最新的心率数据
        val queryText = heartRateService.getLatestHeartRate()
        // 构造消息文本
        val msgText = """
            查询时间: ${queryText.first.formatTimestamp()}
            心率: ${queryText.second}
            状态: ${determineHeartRateState(queryText.second)}
            心率趋势: ${determineHeartRateTrend(queryText.second)}
            """.trimIndent()
        // 构造消息对象
        val msg = MsgUtils.builder().reply(messageId).text(msgText).build()
        // 发送消息
        bot.sendGroupMsg(groupId, msg, false)
    }


}