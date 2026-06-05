package ai.gelej.brownie.telegram;

import ai.gelej.brownie.telegram.handlers.EchoHandler;
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
    void handle_messageWithText_echoesItBackToTheSameChat() {
        //given
        Update update = updateWithText(424242L, "Hello, brownie!");

        //when
        Optional<SendMessage> reply = echoHandler.handle(update);

        //then
        assertThat(reply).isPresent();
        assertThat(reply.get().getText()).isEqualTo("Hello, brownie!");
        assertThat(reply.get().getChatId()).isEqualTo("424242");
    }

    @Test
    void handle_updateWithoutMessage_returnsEmpty() {
        //when
        Optional<SendMessage> reply = echoHandler.handle(new Update());

        //then
        assertThat(reply).isEmpty();
    }

    @Test
    void handle_messageWithoutText_returnsEmpty() {
        //given
        Message message = new Message();
        message.setChat(chat(1L));
        Update update = new Update();
        update.setMessage(message);

        //when
        Optional<SendMessage> reply = echoHandler.handle(update);

        //then
        assertThat(reply).isEmpty();
    }

    @Test
    void handle_nullUpdate_returnsEmpty() {
        //when
        Optional<SendMessage> reply = echoHandler.handle(null);

        //then
        assertThat(reply).isEmpty();
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
        return new Chat(id, "private");
    }
}
