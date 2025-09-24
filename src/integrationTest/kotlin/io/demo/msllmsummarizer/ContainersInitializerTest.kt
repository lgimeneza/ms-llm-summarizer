package io.demo.msllmsummarizer

import io.demo.msllmsummarizer.infrastructure.helper.DockerComposeHelper
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class ContainersInitializerTest : ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        val composeContainer = DockerComposeHelper.start()
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        DockerComposeHelper.setSystemProperties(composeContainer)
    }
}
