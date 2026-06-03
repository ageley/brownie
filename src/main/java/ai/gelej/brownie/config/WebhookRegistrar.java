package ai.gelej.brownie.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Registers the webhook URL with Telegram on startup and removes it on shutdown. Unlike long
 * polling (handled by the Telegram Spring Boot starter), webhook mode has nothing to manage the
 * registration, so this {@link SmartLifecycle} bean does it. Active when
 * {@code telegram.bot.mode=webhook}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "webhook")
public class WebhookRegistrar implements SmartLifecycle {

    private final BotProperties properties;
    private final TelegramClient telegramClient;
    private volatile boolean running;

    @Override
    public void start() {
        String url = properties.getWebhook().getUrl() + properties.getWebhook().getPath();
        try {
            telegramClient.execute(SetWebhook.builder()
                    .url(url)
                    .build());
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
                telegramClient.execute(DeleteWebhook.builder()
                        .build());
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

    /**
     * Starts last (and stops first) so the webhook is registered with Telegram only once the web
     * server and {@link ai.gelej.brownie.controller.WebhookController} are ready to receive updates.
     *
     * @return the highest lifecycle phase
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
