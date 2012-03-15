package org.bukkit.command.defaults;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;

public class HelpCommand extends VanillaCommand {
    public HelpCommand() {
        super("help");
        this.description = "Shows the help menu";
        this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
        this.setPermission("bukkit.command.help");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        String command;
        int pageNumber;
        int pageHeight;
        int pageWidth;

        if (args.length == 0) {
            command = "";
            pageNumber = 1;
        } else if (NumberUtils.isDigits(args[args.length - 1])) {
            command = StringUtils.join(ArrayUtils.subarray(args, 0, args.length - 1), " ");
            pageNumber = NumberUtils.createInteger(args[args.length - 1]);
        } else {
            command = StringUtils.join(args, " ");
            pageNumber = 1;
        }

        if (sender instanceof ConsoleCommandSender) {
            pageHeight = ChatPaginator.UNBOUNDED_PAGE_HEIGHT;
            pageWidth = ChatPaginator.UNBOUNDED_PAGE_WIDTH;
        } else {
            pageHeight = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1;
            pageWidth = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;
        }
        
        HelpMap helpMap = Bukkit.getServer().getHelpMap();
        HelpTopic topic = helpMap.getHelpTopic(command);

        if (topic == null) {
            topic = helpMap.getHelpTopic("/" + command);
        }

        if (topic == null || !topic.canSee(sender)) {
            sender.sendMessage(ChatColor.RED + "No help for " + command);
            return true;
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);

        StringBuilder header = new StringBuilder();
        header.append(ChatColor.YELLOW);
        header.append("--------- ");
        header.append(ChatColor.WHITE);
        header.append("Help: ");
        header.append(topic.getName());
        header.append(" ");
        if (page.getTotalPages() > 1) {
            header.append("(");
            header.append(page.getPageNumber());
            header.append("/");
            header.append(page.getTotalPages());
            header.append(") ");
        }
        header.append(ChatColor.YELLOW);
        for (int i = header.length(); i < ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH; i++) {
            header.append("-");
        }
        sender.sendMessage(header.toString());

        sender.sendMessage(page.getLines());

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("help") || input.startsWith("?");
    }
}
