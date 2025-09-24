package io.demo.msllmsummarizer.application

import io.demo.msllmsummarizer.domain.SummarizationService

class SummarizeText(private val summarizationService: SummarizationService) {
    fun execute(command: SummarizeTextCommand): SummarizeTextResult {
        val summary = summarizationService.summarize(command.original)
        return SummarizeTextResult(summary)
    }
}
