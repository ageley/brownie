package ai.gelej.brownie.telegram;

/**
 * The mode the bot uses to receive updates from Telegram.
 *
 * <ul>
 *     <li>{@link #LONG_POLLING} &mdash; the bot pulls updates from Telegram (no public URL required).</li>
 *     <li>{@link #WEBHOOK} &mdash; Telegram pushes updates to a public HTTPS endpoint exposed by the bot.</li>
 * </ul>
 */
public enum BotMode {
    LONG_POLLING,
    WEBHOOK
}
