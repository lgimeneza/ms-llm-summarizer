package io.demo.msllmsummarizer.domain

data class OriginalText(val value: String) {
    init {
        require(value.isNotBlank()) { "Original text must not be blank" }
    }
}
