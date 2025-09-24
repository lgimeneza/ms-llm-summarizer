package io.demo.msllmsummarizer.domain

interface SummarizationService {
    fun summarize(original: OriginalText): Summary
}
