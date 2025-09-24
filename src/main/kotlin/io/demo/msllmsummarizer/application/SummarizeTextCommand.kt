package io.demo.msllmsummarizer.application

import io.demo.msllmsummarizer.domain.OriginalText

data class SummarizeTextCommand(val original: OriginalText)
