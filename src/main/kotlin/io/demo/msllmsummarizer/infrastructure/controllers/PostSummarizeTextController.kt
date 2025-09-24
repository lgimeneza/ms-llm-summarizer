package io.demo.msllmsummarizer.infrastructure.controllers

import io.demo.msllmsummarizer.application.SummarizeText
import io.demo.msllmsummarizer.application.SummarizeTextCommand
import io.demo.msllmsummarizer.domain.OriginalText
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostSummarizeTextController(private val summarizeText: SummarizeText) {
    @PostMapping("v1/summarize")
    fun postSummarizeText(@RequestBody request: PostSummarizeTextRequest): ResponseEntity<Any> {
        val original = OriginalText(request.text)
        val command = SummarizeTextCommand(original)
        val result = summarizeText.execute(command)
        val response = mapOf("summary" to result.summary.value)
        return ResponseEntity.ok(response)
    }
}

data class PostSummarizeTextRequest(val text: String)
