package ai.gelej.brownie.telegram;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

/**
 * Handler that echoes any text message back to the chat it came from.
 */
@Component
@Order(0)
public class EchoHandler implements MessageHandler {

    /**
     * Builds the echo reply, repeating the received text back to the same chat.
     *
     * @param update the incoming Telegram update
     * @return the echo message, or {@link Optional#empty()} if the update carries no text
     */
    @Override
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
