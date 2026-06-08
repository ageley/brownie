package ai.gelej.brownie.telegram.webhook;

import ai.gelej.brownie.telegram.dispatcher.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Receives updates pushed by Telegram in webhook (push) mode and replies via the Telegram client.
 * Active only when {@code telegram.bot.mode=webhook}.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "webhook")
public class WebhookController {

    private final MessageDispatcher dispatcher;
    private final TelegramClient telegramClient;

    /**
     * Handles a single pushed update, sending any reply produced by the dispatcher.
     *
     * @param update the update pushed by Telegram
     * @return an empty {@code 200 OK} acknowledgement
     */
    @PostMapping(path = "${telegram.bot.webhook.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> onUpdate(@RequestBody Update update) {
        dispatcher.dispatch(update).ifPresent(message -> {
            try {
                telegramClient.execute(message);
            } catch (Exception e) {
                log.error("Failed to send reply", e);
            }
        });
        return ResponseEntity.ok().build();
    }
}
