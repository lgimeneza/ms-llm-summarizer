package io.demo.msllmsummarizer.infrastructure.helper

import java.io.File
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy

class DockerComposeHelper {
    companion object {
        private const val OLLAMA = "ollama"
        private const val OLLAMA_PORT = 11434

        fun start(): ComposeContainer {
            val container =
                ComposeContainer(getDockerComposeFiles())
                    .apply { withLocalCompose(true) }
                    .apply {
                        withExposedService(
                            OLLAMA,
                            OLLAMA_PORT,
                            WaitAllStrategy(WaitAllStrategy.Mode.WITH_INDIVIDUAL_TIMEOUTS_ONLY)
                                .apply { withStrategy(Wait.forListeningPort()) }
                                .apply { withStrategy(Wait.forHttp("/api/tags").forStatusCode(200)) })
                    }

            container.start()
            return container
        }

        private fun getDockerComposeFiles(): ArrayList<File> {
            val files = arrayListOf(File("docker-compose.yml"))
            return files
        }

        fun setSystemProperties(composeContainer: ComposeContainer) {}
    }
}
