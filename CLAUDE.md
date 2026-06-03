# Project conventions

Accumulated from code-review feedback. Update this file after every review round.

## Build & dependencies

- Keep **all versions** (Java, Gradle plugins, dependencies, BOMs) in `gradle.properties`.
  `build.gradle.kts` must not hard-code version literals; read them via
  `providers.gradleProperty("...")`.

## Docker

- The `Dockerfile` contains only three instructions: `FROM`, `COPY` the jar, and `ENTRYPOINT`.
  The jar is built by the CI/Gradle step beforehand; the image does not build it.

## Comments

- No inline comments and no commented-out code in any project file (sources, config, YAML).
- Allowed: Javadoc on classes and public methods only. Keep it brief — state the purpose plus
  input/output parameters where applicable.

## Package layout

Organize by layer (relative to `ai.gelej.brownie`):

- `config` — `@Configuration` classes and `@ConfigurationProperties`.
- `model` — enums, records, POJOs.
- `controller` — controllers.
- `service` — services, handlers, business logic.

## Naming

- Do not prefix class names with `Telegram`. The only exception is `TelegramClientConfig`,
  which produces the `TelegramClient` bean.

## Spring wiring

- Prefer plain beans over boilerplate lifecycle wrappers.
- Long polling: declare a single bean implementing `SpringLongPollingBot` and
  `LongPollingSingleThreadUpdateConsumer` (via `telegrambots-springboot-longpolling-starter`).
  The starter registers it and manages its lifecycle — no `SmartLifecycle` needed.
- Webhook: there is no equivalent starter hook, so a small `SmartLifecycle` bean
  (`WebhookRegistrar`) registers/removes the webhook URL. Declare such lifecycle beans
  directly as components, not wrapped inside a `@Configuration` factory method.

## Message handling

- Incoming updates go to a `MessageDispatcher` that runs an ordered chain of `MessageHandler`
  strategies. The first handler that returns a reply interrupts the chain.
- Each handler encapsulates its own applicability checks. Adding a new handler must not require
  changing existing ones; order is controlled with `@Order`.
