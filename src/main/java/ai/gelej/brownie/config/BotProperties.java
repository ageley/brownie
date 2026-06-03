package ai.gelej.brownie.config;

import ai.gelej.brownie.model.BotMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Bot settings bound from the {@code telegram.bot.*} section of {@code application.yml}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "telegram.bot")
public class BotProperties {

    private String token;

    private String username = "nathan-brownie";

    private BotMode mode = BotMode.LONG_POLLING;

    private boolean autoStart = true;

    private Webhook webhook = new Webhook();

    @Getter
    @Setter
    public static class Webhook {

        private String url;

        private String path = "/telegram/webhook";
    }
}
