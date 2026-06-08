# Nathan Brownie

AI calory tracker

## Components & integrations

```
 ┌──────────┐        messages        ┌──────────────┐     getUpdates / webhook    ┌────────────────────┐
 │          │ ─────────────────────► │              │ ──────────────────────────► │                    │
 │   User   │                        │   Telegram   │                             │   nathan-brownie   │
 │          │ ◄───────────────────── │              │ ◄────────────────────────── │                    │
 └──────────┘     echoed replies     └──────────────┘     sendMessage (echo)      └────────────────────┘
```

- **User** — chats with the bot in Telegram and receives the same text echoed back.
- **Telegram** — the Bot API platform that relays messages between the user and the bot.
- **nathan-brownie bot** — this Spring Boot application; it receives updates and echoes text back.

## Running locally

Create an .env file in the project root (see [.env.example](.env.example))

Build a .jar from the project root:

```shell
./gradlew build
```

Run an app:

```shell
docker compose up -d --build
```

Cleanup:

```shell
docker compose down -v --rmi
```

### Switching delivery mode

The mode is controlled by `telegram.bot.mode` in [`application.yml`](src/main/resources/application.yml)

| Mode           | Value          | Notes                                                           |
|----------------|----------------|-----------------------------------------------------------------|
| Long polling   | `long-polling` | Default. The bot pulls updates; no public URL needed.           |
| Webhook (push) | `webhook`      | Telegram pushes updates to `telegram.bot.webhook.url` + `path`. |
