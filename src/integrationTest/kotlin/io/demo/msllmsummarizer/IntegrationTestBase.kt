package io.demo.msllmsummarizer

import io.restassured.module.mockmvc.RestAssuredMockMvc.mockMvc
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [ContainersInitializerTest::class])
class IntegrationTestBase {
    @Autowired private lateinit var mvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc(mvc)
    }
}
