package ai.gelej.brownie.telegram;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Provides the shared {@link TelegramClient} used to call the Telegram Bot API
 * (sending messages, registering/removing the webhook, etc.).
 */
@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramClientConfig {

    @Bean
    public TelegramClient telegramClient(TelegramBotProperties properties) {
        return new OkHttpTelegramClient(properties.getToken());
    }
}
