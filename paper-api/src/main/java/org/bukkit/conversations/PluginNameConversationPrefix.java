package org.bukkit.conversations;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * PluginNameConversationPrefix is a {@link ConversationPrefix} implementation
 * that displays the plugin name in front of conversation output.
 *
 * @deprecated The conversation API has been deprecated for removal. This system does not support component based messages
 * and has been slowly losing functionality over the years as Minecraft has changed that this API can not adapt to.
 * It is recommended you instead manually listen to the {@link io.papermc.paper.event.player.AsyncChatEvent}
 * or alternatively using {@link io.papermc.paper.dialog.Dialog} to get user input.
 */
@Deprecated(forRemoval = true)
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
