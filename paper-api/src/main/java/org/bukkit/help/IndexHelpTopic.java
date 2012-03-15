package org.bukkit.help;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.util.ChatPaginator;

import java.util.Collection;

/**
 * This help topic generates a list of other help topics. This class is useful for adding your own
 * index help topics. To enforce a particular order, use a sorted collection.
 */
public class IndexHelpTopic extends HelpTopic {

    private String permission;
    private String preamble;
    private Collection<HelpTopic> allTopics;

    public IndexHelpTopic(String name, String shortText, String permission, Collection<HelpTopic> topics) {
        this(name, shortText, permission, topics, null);
    }
    
    public IndexHelpTopic(String name, String shortText, String permission, Collection<HelpTopic> topics, String preamble) {
        this.name = name;
        this.shortText = shortText;
        this.permission = permission;
        this.allTopics = topics;
        this.preamble = preamble;
    }

    public boolean canSee(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (permission == null) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    @Override
    public void amendCanSee(String amendedPermission) {
        permission = amendedPermission;
    }

    public String getFullText(CommandSender sender) {
        StringBuilder sb = new StringBuilder();

        if (preamble != null) {
            sb.append(preamble);
            sb.append("\n");
        }

        for (HelpTopic topic : allTopics) {
            if (topic.canSee(sender)) {
                StringBuilder line = new StringBuilder();
                line.append(ChatColor.GOLD);
                line.append(topic.getName());
                line.append(": ");
                line.append(ChatColor.WHITE);
                line.append(topic.getShortText());

                String lineStr = line.toString().replace("\n", ". ");
                if (sender instanceof Player && lineStr.length() > ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                    sb.append(lineStr.substring(0, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - 3));
                    sb.append("...");
                } else {
                    sb.append(lineStr);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
