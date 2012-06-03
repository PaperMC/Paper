package org.bukkit.conversations;

import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * A ConversationContext provides continuity between nodes in the prompt graph by giving the developer access to the
 * subject of the conversation and a generic map for storing values that are shared between all {@link Prompt}
 * invocations.
 */
public class ConversationContext {
    private Conversable forWhom;
    private Map<Object, Object> sessionData;
    private Plugin plugin;

    /**
     * @param plugin The owning plugin.
     * @param forWhom The subject of the conversation.
     * @param initialSessionData Any initial values to put in the sessionData map.
     */
    public ConversationContext(Plugin plugin, Conversable forWhom, Map<Object, Object> initialSessionData) {
        this.plugin = plugin;
        this.forWhom = forWhom;
        this.sessionData = initialSessionData;
    }

    /**
     * Gets the plugin that owns this conversation.
     * @return The owning plugin.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the subject of the conversation.
     * @return The subject of the conversation.
     */
    public Conversable getForWhom() {
        return forWhom;
    }

    /**
     * Gets session data shared between all {@link Prompt} invocations. Use this as a way
     * to pass data through each Prompt as the conversation develops.
     * @param key The session data key.
     * @return The requested session data.
     */
    public Object getSessionData(Object key) {
        return sessionData.get(key);
    }

    /**
     * Sets session data shared between all {@link Prompt} invocations. Use this as a way to pass
     * data through each prompt as the conversation develops.
     * @param key The session data key.
     * @param value The session data value.
     */
    public void setSessionData(Object key, Object value) {
        sessionData.put(key, value);
    }
}
