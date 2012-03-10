package org.bukkit.craftbukkit.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

public class CommandAliasHelpTopic extends HelpTopic {

    private String aliasFor;
    private Command command;
    private HelpMap helpMap;
    
    public CommandAliasHelpTopic(String alias, String aliasFor, Command command, HelpMap helpMap) {
        this.aliasFor = aliasFor.startsWith("/") ? aliasFor : "/" + aliasFor;
        this.helpMap = helpMap;
        this.command = command;
        this.name = alias.startsWith("/") ? alias : "/" + alias;
        this.shortText = ChatColor.YELLOW + "Alias for " + ChatColor.WHITE + this.aliasFor;
    }

    @Override
    public String getFullText(CommandSender forWho) {
        StringBuilder sb = new StringBuilder(shortText);
        HelpTopic aliasForTopic = helpMap.getHelpTopic(aliasFor);
        if (aliasForTopic != null) {
            sb.append("\n");
            sb.append(aliasForTopic.getFullText(forWho));
        }
        return sb.toString();
    }

    @Override
    public boolean canSee(CommandSender commandSender) {
        HelpTopic aliasForTopic = helpMap.getHelpTopic(aliasFor);
        if (aliasForTopic != null) {
            return aliasForTopic.canSee(commandSender);
        } else {
            return false;
        }
    }
}
