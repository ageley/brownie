# General coding preferences

Reusable, project-agnostic conventions accumulated from code-review feedback. Project-specific
decisions live in [docs/project-decisions.md](docs/project-decisions.md). Update both after every
review round.

## Comments

- No inline comments and no commented-out code in any file (sources, config, YAML).
- Allowed: Javadoc on classes and public methods only. Keep it brief — state the purpose plus
  input/output parameters where applicable.

## Formatting

- Fluent / builder APIs: keep the first invocation on the same line as the receiver, then put each
  subsequent call on its own line.

  ```java
  telegramClient.execute(SetWebhook.builder()
          .url(url)
          .build());
  ```

## Lombok

- Prefer Lombok over hand-written boilerplate. Use `@RequiredArgsConstructor` for constructor
  injection of `final` fields instead of declaring the constructor by hand; `@Getter`/`@Setter`
  for accessors; `@Slf4j` for loggers.

## Package layout

Organize by layer (relative to the base package):

- `config` — `@Configuration` classes and `@ConfigurationProperties`.
- `model` — enums, records, POJOs.
- `controller` — controllers.
- `service` — services, handlers, business logic.

## Naming

- Do not prefix class names with the framework/vendor (e.g. `Telegram`). The only exception is a
  class that produces a vendor bean named that way (e.g. `TelegramClientConfig` → `TelegramClient`).

## Build & dependencies

- Keep **all versions** (Java, Gradle plugins, dependencies, BOMs) in `gradle.properties`; no
  version literals in `build.gradle.kts`.
- Reference them with Kotlin delegated properties and `$` interpolation:
  `val telegramBotsVersion: String by project` then `"org.telegram:telegrambots-client:$telegramBotsVersion"`.
- The `plugins { }` block is the one exception: it must be the first block in the file, so no
  `val`/`by project` can precede it. Read plugin versions inline there with
  `providers.gradleProperty("...").get()` (or move them to `settings.gradle.kts` `pluginManagement`).
- Keep dependency versions current.

## Spring wiring

- Prefer plain beans over boilerplate lifecycle wrappers. Declare lifecycle beans directly as
  components, not wrapped inside a `@Configuration` factory method.
- Select beans by configuration with `@ConditionalOnProperty` rather than `@ConditionalOnExpression`
  when a simple property match suffices.
- A `SmartLifecycle` bean that depends on the web server being up should override `getPhase()` to
  start last (and therefore stop first).
