package ai.gelej.brownie.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the Telegram bot, bound from the {@code telegram.bot.*} section of
 * {@code application.yml}. The {@link #mode} property selects between long polling and webhook
 * (push) delivery and can be changed without touching any code.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {

    /** Bot token issued by @BotFather. */
    private String token;

    /** Bot username (without the leading {@code @}). */
    private String username = "nathan-brownie";

    /** How updates are received from Telegram. Defaults to long polling. */
    private BotMode mode = BotMode.LONG_POLLING;

    /**
     * When {@code false}, beans are created but the bot does not connect to Telegram
     * (no polling thread is started and no webhook is registered). Used by tests.
     */
    private boolean autoStart = true;

    /** Webhook (push mode) settings. Only used when {@link #mode} is {@link BotMode#WEBHOOK}. */
    private Webhook webhook = new Webhook();

    @Getter
    @Setter
    public static class Webhook {

        /** Public base URL of this service, e.g. {@code https://bot.example.com}. */
        private String url;

        /** Path on which the webhook endpoint is exposed. */
        private String path = "/telegram/webhook";
    }
}
