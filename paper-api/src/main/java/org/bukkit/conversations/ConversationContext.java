package org.bukkit.conversations;

import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ConversationContext provides continuity between nodes in the prompt graph
 * by giving the developer access to the subject of the conversation and a
 * generic map for storing values that are shared between all {@link Prompt}
 * invocations.
 */
public class ConversationContext {
    private final Conversable forWhom;
    private final Map<Object, Object> sessionData;
    private final Plugin plugin;

    /**
     * @param plugin The owning plugin.
     * @param forWhom The subject of the conversation.
     * @param initialSessionData Any initial values to put in the sessionData
     *     map.
     */
    public ConversationContext(@Nullable Plugin plugin, @NotNull Conversable forWhom, @NotNull Map<Object, Object> initialSessionData) {
        this.plugin = plugin;
        this.forWhom = forWhom;
        this.sessionData = initialSessionData;
    }

    /**
     * Gets the plugin that owns this conversation.
     *
     * @return The owning plugin.
     */
    @Nullable
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the subject of the conversation.
     *
     * @return The subject of the conversation.
     */
    @NotNull
    public Conversable getForWhom() {
        return forWhom;
    }

    /**
     * Gets the underlying sessionData map.
     *
     * May be directly modified to manipulate session data.
     *
     * @return The full sessionData map.
     */
    @NotNull
    public Map<Object, Object> getAllSessionData() {
        return sessionData;
    }

    /**
     * Gets session data shared between all {@link Prompt} invocations. Use
     * this as a way to pass data through each Prompt as the conversation
     * develops.
     *
     * @param key The session data key.
     * @return The requested session data.
     */
    @Nullable
    public Object getSessionData(@NotNull Object key) {
        return sessionData.get(key);
    }

    /**
     * Sets session data shared between all {@link Prompt} invocations. Use
     * this as a way to pass data through each prompt as the conversation
     * develops.
     *
     * @param key The session data key.
     * @param value The session data value.
     */
    public void setSessionData(@NotNull Object key, @Nullable Object value) {
        sessionData.put(key, value);
    }
}
