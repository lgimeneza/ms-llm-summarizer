package io.demo.msllmsummarizer.acceptance

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.demo.msllmsummarizer.IntegrationTestBase
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import java.net.HttpURLConnection.HTTP_OK
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class PostSummarizeTextFeature : IntegrationTestBase() {

    @Test
    fun `should return a sensible summarized text when posting a valid request`() {
        val requestText =
            "Kotlin is a modern programming language that makes developers happier. It is concise, safe, interoperable, and tool-friendly."

        val response =
            given()
                .body(
                    """
                {
                    "text": "$requestText"
                }
                """)
                .contentType("application/json")
                .post("/v1/summarize")
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .body()
                .asString()

        val mapper = jacksonObjectMapper()
        val json: Map<String, Any?> = mapper.readValue(response)
        assertTrue(json.containsKey("summary"), "The response must contain the 'summary' field")
        val summary = json["summary"] as? String
        assertNotNull(summary, "The 'summary' field must be a String")
        assertTrue(summary.isNotBlank(), "The summary must not be empty")

        if (requestText.isNotEmpty()) {
            assertTrue(summary.length < requestText.length, "The summary must be shorter than the original text")
        }

        val keywords = listOf("Kotlin", "modern", "concise", "safe", "interoperable", "programming", "tool")
        assertTrue(
            keywords.any { summary.contains(it, ignoreCase = true) },
            "The summary should contain at least one representative keyword from the original text")
    }
}
