package ai.gelej.brownie.telegram;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class EchoHandlerTest {

    private final EchoHandler echoHandler = new EchoHandler();

    @Test
    void echoesTextBackToTheSameChat() {
        Update update = updateWithText(424242L, "Hello, brownie!");

        Optional<SendMessage> reply = echoHandler.handle(update);

        assertThat(reply).isPresent();
        assertThat(reply.get().getText()).isEqualTo("Hello, brownie!");
        assertThat(reply.get().getChatId()).isEqualTo("424242");
    }

    @Test
    void ignoresUpdatesWithoutAMessage() {
        assertThat(echoHandler.handle(new Update())).isEmpty();
    }

    @Test
    void ignoresMessagesWithoutText() {
        Message message = new Message();
        message.setChat(chat(1L));
        Update update = new Update();
        update.setMessage(message);

        assertThat(echoHandler.handle(update)).isEmpty();
    }

    @Test
    void ignoresNullUpdate() {
        assertThat(echoHandler.handle(null)).isEmpty();
    }

    private static Update updateWithText(long chatId, String text) {
        Message message = new Message();
        message.setText(text);
        message.setChat(chat(chatId));
        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    private static Chat chat(long id) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setType("private");
        return chat;
    }
}
