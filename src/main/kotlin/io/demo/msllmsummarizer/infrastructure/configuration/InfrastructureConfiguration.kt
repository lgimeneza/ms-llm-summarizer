package io.demo.msllmsummarizer.infrastructure.configuration

import io.demo.msllmsummarizer.domain.SummarizationService
import io.demo.msllmsummarizer.infrastructure.services.ChatClientSummarizationService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfrastructureConfiguration {
    @Bean fun chatClient(chatModel: ChatModel): ChatClient = ChatClient.builder(chatModel).build()

    @Bean
    fun chatClientSummarizationService(chatClient: ChatClient): SummarizationService =
        ChatClientSummarizationService(chatClient)
}
