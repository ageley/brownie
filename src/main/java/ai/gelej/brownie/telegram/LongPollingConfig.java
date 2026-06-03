package ai.gelej.brownie.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Wires up long polling (pull) delivery. Active when {@code telegram.bot.mode=long-polling},
 * which is also the default when the property is absent.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "long-polling", matchIfMissing = true)
public class LongPollingConfig {

    /**
     * Update consumer that echoes every text message back to its chat. It delegates the
     * decision of what to reply to {@link EchoHandler} and only performs the network send.
     */
    @Bean
    public LongPollingSingleThreadUpdateConsumer echoUpdateConsumer(EchoHandler echoHandler,
                                                                    TelegramClient telegramClient) {
        return update -> echoHandler.handle(update).ifPresent(message -> {
            try {
                telegramClient.execute(message);
            } catch (Exception e) {
                log.error("Failed to send echo reply", e);
            }
        });
    }

    /**
     * Manages the lifecycle of the long polling session: it registers the bot when the
     * application starts and unregisters it on shutdown. Registration (the only network-touching
     * step) is skipped when {@code telegram.bot.auto-start=false}.
     */
    @Bean
    public SmartLifecycle longPollingLifecycle(TelegramBotProperties properties,
                                               LongPollingSingleThreadUpdateConsumer consumer) {
        return new LongPollingLifecycle(properties, consumer);
    }

    @Slf4j
    static class LongPollingLifecycle implements SmartLifecycle {

        private final TelegramBotProperties properties;
        private final LongPollingSingleThreadUpdateConsumer consumer;
        private TelegramBotsLongPollingApplication application;
        private volatile boolean running;

        LongPollingLifecycle(TelegramBotProperties properties,
                             LongPollingSingleThreadUpdateConsumer consumer) {
            this.properties = properties;
            this.consumer = consumer;
        }

        @Override
        public void start() {
            if (!properties.isAutoStart()) {
                log.info("Long polling auto-start disabled; bot will not connect to Telegram");
                return;
            }
            try {
                application = new TelegramBotsLongPollingApplication();
                application.registerBot(properties.getToken(), consumer);
                running = true;
                log.info("Telegram bot '{}' started in long polling mode", properties.getUsername());
            } catch (Exception e) {
                throw new IllegalStateException("Failed to start Telegram long polling bot", e);
            }
        }

        @Override
        public void stop() {
            if (application != null) {
                try {
                    application.close();
                } catch (Exception e) {
                    log.warn("Failed to stop Telegram long polling application", e);
                }
            }
            running = false;
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }
}
