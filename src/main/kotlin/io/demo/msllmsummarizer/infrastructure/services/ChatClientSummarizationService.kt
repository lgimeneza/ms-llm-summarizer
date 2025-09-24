package io.demo.msllmsummarizer.infrastructure.services

import io.demo.msllmsummarizer.domain.OriginalText
import io.demo.msllmsummarizer.domain.SummarizationService
import io.demo.msllmsummarizer.domain.Summary
import org.springframework.ai.chat.client.ChatClient

class ChatClientSummarizationService(private val chatClient: ChatClient) : SummarizationService {
    override fun summarize(original: OriginalText): Summary {
        val prompt =
            listOf(
                    "Summarize the following text into a very short tagline.",
                    "It must be one sentence.",
                    "Style must be marketing oriented, catchy, engaging.",
                    "Language must be English.",
                    "Text:",
                    original.value)
                .joinToString(" \n")
        val content = chatClient.prompt(prompt).call().content()
        return Summary(content)
    }
}
