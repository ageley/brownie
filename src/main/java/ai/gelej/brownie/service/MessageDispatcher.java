package ai.gelej.brownie.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

/**
 * Routes an incoming update through the ordered chain of {@link MessageHandler}s, stopping at
 * the first handler that produces a reply.
 */
@Service
public class MessageDispatcher {

    private final List<MessageHandler> handlers;

    /**
     * @param handlers the handler chain, injected in {@code @Order} sequence
     */
    public MessageDispatcher(List<MessageHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * Dispatches an update to the first handler that accepts it.
     *
     * @param update the incoming Telegram update
     * @return the reply from the first matching handler, or {@link Optional#empty()} if none match
     */
    public Optional<SendMessage> dispatch(Update update) {
        return handlers.stream()
                .map(handler -> handler.handle(update))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
