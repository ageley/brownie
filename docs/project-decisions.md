# Project-specific decisions

Decisions specific to the `brownie` Telegram bot. General, reusable conventions live in the root
[CLAUDE.md](../CLAUDE.md). Update both after every review round.

## Delivery mode

- The bot supports two delivery modes, selected by `telegram.bot.mode` in `application.yml`:
  `long-polling` (default, pull) and `webhook` (push).
- Mode is a wiring concern, resolved entirely via `@ConditionalOnProperty(prefix = "telegram.bot",
  name = "mode", ...)`. There is no `BotMode` enum and no `mode` field on `BotProperties` — nothing
  reads the value at runtime, so it is not bound.
- There is no `auto-start` flag: in production the active mode always starts.

## Long polling

- `EchoBot` is a single bean implementing `SpringLongPollingBot` and
  `LongPollingSingleThreadUpdateConsumer` (via `telegrambots-springboot-longpolling-starter`). The
  starter registers it and manages its lifecycle — no `SmartLifecycle`.

## Webhook

- There is no starter hook to register the webhook URL, so `WebhookRegistrar` (a `SmartLifecycle`
  `@Component`) calls `SetWebhook` on start and `DeleteWebhook` on stop.
- It overrides `getPhase()` to `Integer.MAX_VALUE` so the webhook is registered only after the web
  server and `WebhookController` are ready to receive updates, and removed first on shutdown.
- `WebhookController` acknowledges each pushed update with `200 OK` and sends any reply through the
  `TelegramClient`.

## Message handling

- Incoming updates go to `MessageDispatcher`, which runs an ordered chain of `MessageHandler`
  strategies; the first handler that returns a reply interrupts the chain.
- Each handler encapsulates its own applicability checks. Adding a new handler must not require
  changing existing ones; order is controlled with `@Order` (`EchoHandler` is `@Order(0)`).

## Docker

- The `Dockerfile` contains only `FROM`, `COPY` the jar, and `ENTRYPOINT`. The jar is built by the
  CI/Gradle step beforehand; the image does not build it.
