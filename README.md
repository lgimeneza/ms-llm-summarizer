# MS LLM Summarizer

A Kotlin + Spring Boot service that generates short marketing-style taglines (summaries) from arbitrary input text using a local LLM served by [Ollama](https://github.com/ollama/ollama) through **Spring AI**. The codebase applies **Hexagonal Architecture** and **Domain-Driven Design (DDD)**: `domain` (ubiquitous language & core model), `application` (use cases / application services), and `infrastructure` (adapters: controllers, AI client wiring, configuration).

## Table of Contents
- [Features](#features)
- [Architecture (Hexagonal + DDD)](#architecture-hexagonal--ddd)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone & Build](#clone--build)
  - [Run Ollama](#run-ollama)
  - [Run the Service](#run-the-service)
- [Configuration](#configuration)
- [REST API](#rest-api)
  - [POST /v1/summarize](#post-v1summarize)
- [Testing](#testing)
- [Code Style & Formatting](#code-style--formatting)
- [Git Hooks (ktfmt)](#git-hooks-ktfmt)
- [Packaging](#packaging)
- [Troubleshooting](#troubleshooting)
- [Next Ideas](#next-ideas)
- [License](#license)

## Features
- Summarizes arbitrary textual content into a single catchy marketing sentence.
- Uses Spring AI with the Ollama provider (default model: `mistral`).
- Hexagonal + DDD layering (domain isolated from framework details).
- Unit + integration test setup (separate Gradle source set `integrationTest`).
- Automatic (optional) model pulling strategy: only downloads when missing.
- Consistent Kotlin formatting enforced via `ktfmt` + optional pre-commit hook.

## Architecture (Hexagonal + DDD)
```
+------------------+        +-----------------------+         
|  REST Controller | -----> | Application Use Case  | ----->  SummarizationService
+------------------+        +-----------------------+                |
                                                                     v
                                                            ChatClientSummarizationService (Infra Adapter)
                                                                    |
                                                             Spring AI ChatClient
                                                                    |
                                                                  Ollama
```
Layers & DDD responsibilities:
- Domain: `OriginalText`, `Summary`, `SummarizationService` (pure business language; no framework types).
- Application: `SummarizeText` orchestrates a use case; depends on domain ports only.
- Infrastructure: Adapters (HTTP controller, AI client implementation, configuration beans).

This separation enables easy replacement of the LLM provider or transport layer without touching domain or application code.

## Tech Stack
- Kotlin (JVM 17)
- Spring Boot 3.5.6
- Spring AI (Ollama starter; BOM version `1.0.0-SNAPSHOT`)
- Gradle 8.14.3 (wrapper)
- Testing: JUnit 5, Mockito-Kotlin, Rest Assured (integration), JSON Unit, Testcontainers (placeholder dependency)
- Formatting: `ktfmt`

## Getting Started
### Prerequisites
- Java 17 (JDK)
- Docker (for running Ollama locally)
- (Optional) cURL or any HTTP client (Postman, HTTPie, etc.)

### Clone & Build
```sh
git clone <your-repo-url> ms-llm-summarizer
cd ms-llm-summarizer
./gradlew build
```

### Run Ollama
Start a local Ollama instance via `docker-compose`:
```sh
docker compose up -d
```
This exposes Ollama at `http://localhost:11434`. The application is configured to pull the `mistral` model automatically **only if missing** (see `spring.ai.ollama.init.pull-model-strategy=when_missing`). If you want to pre-pull manually:
```sh
docker exec -it $(docker ps --filter ancestor=ollama/ollama:latest -q | head -1) ollama pull mistral
```

### Run the Service
Run with Gradle:
```sh
./gradlew bootRun
```
App default base URL: `http://localhost:8080`.

Or run the packaged (non-plain) Boot jar after build (file name may include a version if you set one):
```sh
./gradlew bootJar
java -jar build/libs/ms-llm-summarizer.jar
```
If only the *plain* jar exists (no Boot fat jar), use `bootRun` for now or configure the `bootJar` task.

## Configuration
Main config: `src/main/resources/application.yml`:
```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: mistral
      init:
        pull-model-strategy: when_missing
        timeout: 5m
        max-retries: 1
```
Environment variable overrides (examples):
- `SPRING_AI_OLLAMA_BASE_URL=http://host.docker.internal:11434`
- `SPRING_AI_OLLAMA_CHAT_OPTIONS_MODEL=llama3`
- `SERVER_PORT=9090`

## REST API
### POST /v1/summarize
Summarize an input text into a single marketing-style sentence.

Request:
```http
POST /v1/summarize HTTP/1.1
Content-Type: application/json

{
  "text": "Launch your productivity to the next level with our collaborative platform that integrates tasks, docs, and chat."
}
```

Response (200 OK):
```json
{
  "summary": "Supercharge teamwork with an all‑in‑one collaborative productivity platform."
}
```
Error scenarios:
- 400 Bad Request if `text` is blank (domain validation in `OriginalText`).

## Testing
Run unit tests:
```sh
./gradlew test
```
Run integration tests:
```sh
./gradlew integrationTest
```
Run everything (includes both):
```sh
./gradlew check
```
HTML test reports:
- Unit: `build/reports/tests/test/index.html`
- Integration: `build/reports/tests/integrationTest/index.html`

## Code Style & Formatting
Format all Kotlin sources with:
```sh
./gradlew ktfmtFormat
```
You can inspect formatting outputs under `build/ktfmt/`.

## Git Hooks (ktfmt)
A pre-commit hook can auto-format changed Kotlin files.
- Install/refresh formatting hook:
  ```sh
  ./gradlew installKtfmtPrecommit
  ```
- Install an empty (disabled) hook:
  ```sh
  ./gradlew installEmptyPrecommit
  ```

## Packaging
Standard build:
```sh
./gradlew build
```
Artifacts:
- Plain jar (no deps): `build/libs/ms-llm-summarizer-plain.jar`
- Executable fat jar (if enabled): `build/libs/ms-llm-summarizer.jar`

To produce the Boot jar ensure the `bootJar` task runs:
```sh
./gradlew bootJar
```

## License
Distributed under the MIT License. See `LICENSE` for more information.