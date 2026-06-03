package ai.gelej.brownie.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Wires up webhook (push) delivery. Active when {@code telegram.bot.mode=webhook}.
 *
 * <p>Incoming updates are received by {@link TelegramWebhookController}. This config is only
 * responsible for telling Telegram where to push updates (on startup) and removing that
 * registration on shutdown.
 */
@Configuration
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "webhook")
public class WebhookConfig {

    @Bean
    public SmartLifecycle webhookLifecycle(TelegramBotProperties properties, TelegramClient telegramClient) {
        return new WebhookLifecycle(properties, telegramClient);
    }

    @Slf4j
    @RequiredArgsConstructor
    static class WebhookLifecycle implements SmartLifecycle {

        private final TelegramBotProperties properties;
        private final TelegramClient telegramClient;
        private volatile boolean running;

        @Override
        public void start() {
            if (!properties.isAutoStart()) {
                log.info("Webhook auto-start disabled; webhook will not be registered with Telegram");
                return;
            }
            String url = properties.getWebhook().getUrl() + properties.getWebhook().getPath();
            try {
                telegramClient.execute(SetWebhook.builder().url(url).build());
                running = true;
                log.info("Telegram bot '{}' started in webhook mode, updates pushed to {}",
                        properties.getUsername(), url);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to register Telegram webhook at " + url, e);
            }
        }

        @Override
        public void stop() {
            if (running) {
                try {
                    telegramClient.execute(DeleteWebhook.builder().build());
                } catch (Exception e) {
                    log.warn("Failed to delete Telegram webhook", e);
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
