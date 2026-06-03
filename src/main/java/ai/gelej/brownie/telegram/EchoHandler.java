package ai.gelej.brownie.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

/**
 * Core bot logic: turns an incoming {@link Update} into the reply to send back.
 *
 * <p>This is deliberately free of any transport concerns (polling vs. webhook) and of any
 * network I/O, so it can be unit tested in isolation.
 */
@Component
public class EchoHandler {

    /**
     * Builds the echo reply for an update, repeating the received text back to the same chat.
     *
     * @param update the incoming Telegram update
     * @return the message to send back, or {@link Optional#empty()} if the update carries no text
     */
    public Optional<SendMessage> handle(Update update) {
        if (update == null || !update.hasMessage()) {
            return Optional.empty();
        }
        Message message = update.getMessage();
        if (!message.hasText()) {
            return Optional.empty();
        }
        return Optional.of(SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text(message.getText())
                .build());
    }
}
