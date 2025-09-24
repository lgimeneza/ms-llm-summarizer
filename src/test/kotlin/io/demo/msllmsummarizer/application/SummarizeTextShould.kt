package io.demo.msllmsummarizer.application

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.demo.msllmsummarizer.domain.OriginalText
import io.demo.msllmsummarizer.domain.SummarizationService
import io.demo.msllmsummarizer.domain.Summary
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.Test

class SummarizeTextShould {

    @Test
    fun `return summary when service produces one`() {
        val service: SummarizationService = mock()
        val useCase = SummarizeText(service)
        val original = OriginalText("Kotlin improves productivity")
        val command = SummarizeTextCommand(original)
        val expected = Summary("Kotlin boosts productivity")
        doReturn(expected).`when`(service).summarize(original)

        val result = useCase.execute(command)

        assertEquals(expected, result.summary)
        verify(service, times(1)).summarize(original)
    }

    @Test
    fun `propagate exception when service fails`() {
        val service: SummarizationService = mock()
        val useCase = SummarizeText(service)
        val original = OriginalText("Failure path")
        val command = SummarizeTextCommand(original)
        doThrow(IllegalStateException("failure")).`when`(service).summarize(original)

        assertFailsWith<IllegalStateException> { useCase.execute(command) }
        verify(service, times(1)).summarize(original)
    }

    @Test
    fun `fail fast when original text is blank`() {
        assertFailsWith<IllegalArgumentException> { OriginalText("") }
    }

    @Test
    fun `allow null summary value from service`() {
        val service: SummarizationService = mock()
        val useCase = SummarizeText(service)
        val original = OriginalText("Some text")
        val command = SummarizeTextCommand(original)
        val expected = Summary(null)
        doReturn(expected).`when`(service).summarize(original)

        val result = useCase.execute(command)

        assertEquals(null, result.summary.value)
        verify(service, times(1)).summarize(original)
    }
}
