package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * PluginNameConversationPrefix is a {@link ConversationPrefix} implementation
 * that displays the plugin name in front of conversation output.
 */
public class PluginNameConversationPrefix implements ConversationPrefix {

    protected String separator;
    protected ChatColor prefixColor;
    protected Plugin plugin;

    private String cachedPrefix;

    public PluginNameConversationPrefix(@NotNull Plugin plugin) {
        this(plugin, " > ", ChatColor.LIGHT_PURPLE);
    }

    public PluginNameConversationPrefix(@NotNull Plugin plugin, @NotNull String separator, @NotNull ChatColor prefixColor) {
        this.separator = separator;
        this.prefixColor = prefixColor;
        this.plugin = plugin;

        cachedPrefix = prefixColor + plugin.getDescription().getName() + separator + ChatColor.WHITE;
    }

    /**
     * Prepends each conversation message with the plugin name.
     *
     * @param context Context information about the conversation.
     * @return An empty string.
     */
    @Override
    @NotNull
    public String getPrefix(@NotNull ConversationContext context) {
        return cachedPrefix;
    }
}
