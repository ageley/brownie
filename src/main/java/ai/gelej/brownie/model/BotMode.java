package ai.gelej.brownie.model;

/**
 * How the bot receives updates from Telegram: {@code LONG_POLLING} pulls updates,
 * {@code WEBHOOK} has Telegram push them to a public HTTPS endpoint.
 */
public enum BotMode {
    LONG_POLLING,
    WEBHOOK
}
