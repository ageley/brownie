# brownie

AI calory tracker — currently exposing a simple **echo** Telegram bot (`nathan-brownie`).

## Components & integrations

```
 ┌──────────┐        messages        ┌────────────────┐     getUpdates / webhook    ┌─────────────────────┐
 │          │ ─────────────────────► │                │ ──────────────────────────► │                     │
 │   User   │                        │    Telegram    │                             │  nathan-brownie bot │
 │          │ ◄───────────────────── │  (Bot API/MTP) │ ◄────────────────────────── │   (Spring Boot app) │
 └──────────┘     echoed replies     └────────────────┘     sendMessage (echo)       └─────────────────────┘

 Receiving updates from Telegram — selectable via application.yml (telegram.bot.mode):

   long-polling (pull):   bot  ──getUpdates──►  Telegram        (no public URL required)
   webhook      (push):   Telegram  ──POST /telegram/webhook──►  bot   (public HTTPS URL required)
```

- **User** — chats with the bot in Telegram and receives the same text echoed back.
- **Telegram** — the Bot API platform that relays messages between the user and the bot.
- **nathan-brownie bot** — this Spring Boot application; it receives updates and echoes text back.

## Running locally

Set the bot token (from [@BotFather](https://t.me/BotFather)) and run:

```bash
TELEGRAM_BOT_TOKEN=<your-token> ./gradlew bootRun
```

### Switching delivery mode

The mode is controlled by `telegram.bot.mode` in [`application.yml`](src/main/resources/application.yml)
(or the `TELEGRAM_BOT_MODE` environment variable):

| Mode           | Value          | Notes                                                           |
|----------------|----------------|-----------------------------------------------------------------|
| Long polling   | `long-polling` | Default. The bot pulls updates; no public URL needed.           |
| Webhook (push) | `webhook`      | Telegram pushes updates to `telegram.bot.webhook.url` + `path`. |

For webhook mode also set the public base URL:

```bash
TELEGRAM_BOT_MODE=webhook \
TELEGRAM_BOT_TOKEN=<your-token> \
TELEGRAM_WEBHOOK_URL=https://your-public-host \
./gradlew bootRun
```

## Tests

```bash
./gradlew test
```

## Docker

The app packages into a slim [`bellsoft/liberica-openjre-alpine`](https://hub.docker.com/r/bellsoft/liberica-openjre-alpine) image:

```bash
docker build -t nathan-brownie .
docker run -e TELEGRAM_BOT_TOKEN=<your-token> nathan-brownie
```
