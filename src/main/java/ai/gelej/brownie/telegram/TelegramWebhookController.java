package ai.gelej.brownie.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Receives updates pushed by Telegram in webhook (push) mode.
 *
 * <p>The echo reply is returned directly in the HTTP response body: Telegram executes any Bot API
 * method returned this way, so no extra outbound call is needed. Active only when
 * {@code telegram.bot.mode=webhook}.
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "webhook")
public class TelegramWebhookController {

    private final EchoHandler echoHandler;

    @PostMapping(path = "${telegram.bot.webhook.path}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BotApiMethod<?> onUpdate(@RequestBody Update update) {
        return echoHandler.handle(update).orElse(null);
    }
}
