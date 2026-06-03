package ai.gelej.brownie.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * A single strategy for turning an incoming update into a reply. Each handler encapsulates
 * its own applicability checks; a new handler can be added to the chain without changing others.
 */
public interface MessageHandler {

    /**
     * Attempts to handle the given update.
     *
     * @param update the incoming Telegram update
     * @return the reply to send, or {@link Optional#empty()} if this handler does not apply
     */
    Optional<SendMessage> handle(Update update);
}
