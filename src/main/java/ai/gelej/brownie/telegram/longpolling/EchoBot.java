package ai.gelej.brownie.telegram.longpolling;

import ai.gelej.brownie.telegram.BotProperties;
import ai.gelej.brownie.telegram.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Long polling (pull) bot. Registered automatically by the Telegram Spring Boot starter, which
 * polls Telegram and feeds updates to this consumer; replies are produced by the dispatcher.
 * Active when {@code telegram.bot.mode=long-polling} (the default).
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "long-polling",
        matchIfMissing = true)
public class EchoBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotProperties properties;
    private final TelegramClient telegramClient;
    private final MessageDispatcher dispatcher;

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    /**
     * Dispatches an incoming update and sends the resulting reply, if any.
     *
     * @param update the update received from Telegram
     */
    @Override
    public void consume(Update update) {
        dispatcher.dispatch(update).ifPresent(message -> {
            try {
                telegramClient.execute(message);
            } catch (Exception e) {
                log.error("Failed to send reply", e);
            }
        });
    }
}
