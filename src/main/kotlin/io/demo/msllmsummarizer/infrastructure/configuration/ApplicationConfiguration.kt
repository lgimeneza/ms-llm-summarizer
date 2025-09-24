package io.demo.msllmsummarizer.infrastructure.configuration

import io.demo.msllmsummarizer.application.SummarizeText
import io.demo.msllmsummarizer.domain.SummarizationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {
    @Bean
    fun summarizeText(summarizationService: SummarizationService): SummarizeText = SummarizeText(summarizationService)
}
