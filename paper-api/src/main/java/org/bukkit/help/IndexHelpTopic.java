package org.bukkit.help;

import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This help topic generates a list of other help topics. This class is useful
 * for adding your own index help topics. To enforce a particular order, use a
 * sorted collection.
 * <p>
 * If a preamble is provided to the constructor, that text will be displayed
 * before the first item in the index.
 */
public class IndexHelpTopic extends HelpTopic {

    protected String permission;
    protected String preamble;
    protected Collection<HelpTopic> allTopics;

    public IndexHelpTopic(@NotNull String name, @Nullable String shortText, @Nullable String permission, @NotNull Collection<HelpTopic> topics) {
        this(name, shortText, permission, topics, null);
    }

    public IndexHelpTopic(@NotNull String name, @Nullable String shortText, @Nullable String permission, @NotNull Collection<HelpTopic> topics, @Nullable String preamble) {
        this.name = name;
        this.shortText = (shortText == null) ? "" : shortText;
        this.permission = permission;
        this.preamble = (preamble == null) ? "" : preamble;
        setTopicsCollection(topics);
    }

    /**
     * Sets the contents of the internal allTopics collection.
     *
     * @param topics The topics to set.
     */
    protected void setTopicsCollection(@NotNull Collection<HelpTopic> topics) {
        this.allTopics = topics;
    }

    @Override
    public boolean canSee(@NotNull CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (permission == null) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    @Override
    public void amendCanSee(@Nullable String amendedPermission) {
        permission = amendedPermission;
    }

    @Override
    @NotNull
    public String getFullText(@NotNull CommandSender sender) {
        StringBuilder sb = new StringBuilder();

        if (preamble != null) {
            sb.append(buildPreamble(sender));
            sb.append("\n");
        }

        for (HelpTopic topic : allTopics) {
            if (topic.canSee(sender)) {
                String lineStr = buildIndexLine(sender, topic).replace("\n", ". ");
                if (sender instanceof Player && lineStr.length() > ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                    sb.append(lineStr, 0, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - 3);
                    sb.append("...");
                } else {
                    sb.append(lineStr);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Builds the topic preamble. Override this method to change how the index
     * preamble looks.
     *
     * @param sender The command sender requesting the preamble.
     * @return The topic preamble.
     */
    @NotNull
    protected String buildPreamble(@NotNull CommandSender sender) {
        return ChatColor.GRAY + preamble;
    }

    /**
     * Builds individual lines in the index topic. Override this method to
     * change how index lines are rendered.
     *
     * @param sender The command sender requesting the index line.
     * @param topic  The topic to render into an index line.
     * @return The rendered index line.
     */
    @NotNull
    protected String buildIndexLine(@NotNull CommandSender sender, @NotNull HelpTopic topic) {
        StringBuilder line = new StringBuilder();
        line.append(ChatColor.GOLD);
        line.append(topic.getName());
        line.append(": ");
        line.append(ChatColor.WHITE);
        line.append(topic.getShortText());
        return line.toString();
    }
}
