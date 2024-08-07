package cn.xihan.mibandheartratebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.resource.EncodedResourceResolver
import org.springframework.web.servlet.resource.PathResourceResolver

@SpringBootApplication
class HeartRateBotApplication

fun main(args: Array<String>) {
    runApplication<HeartRateBotApplication>(*args)
}

@Configuration
class WebConfig : WebMvcConfigurationSupport() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 静态资源映射
        registry
            .addResourceHandler("/html/**")
            .addResourceLocations("file:$HTML_PATH")
            .setCachePeriod(3600)
            .resourceChain(true)
            .addResolver(EncodedResourceResolver())
            .addResolver(PathResourceResolver())
        super.addResourceHandlers(registry)
    }

    companion object {
        val HTML_PATH: String = System.getProperty("user.dir") + "/static/"
    }
}