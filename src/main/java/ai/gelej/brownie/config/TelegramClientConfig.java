package ai.gelej.brownie.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Provides the shared {@link TelegramClient} used to call the Telegram Bot API.
 */
@Configuration
@EnableConfigurationProperties(BotProperties.class)
public class TelegramClientConfig {

    /**
     * Creates the Telegram API client authenticated with the configured bot token.
     *
     * @param properties the bot settings carrying the token
     * @return a client for calling the Telegram Bot API
     */
    @Bean
    public TelegramClient telegramClient(BotProperties properties) {
        return new OkHttpTelegramClient(properties.getToken());
    }
}
