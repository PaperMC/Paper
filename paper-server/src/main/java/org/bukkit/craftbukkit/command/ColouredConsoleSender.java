package org.bukkit.craftbukkit.command;

import java.util.EnumMap;
import java.util.Map;
import jline.ANSIBuffer.ANSICodes;
import jline.ConsoleReader;
import jline.Terminal;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;

public class ColouredConsoleSender extends ConsoleCommandSender {
    private final ConsoleReader reader;
    private final Terminal terminal;
    private final Map<ChatColor, String> replacements = new EnumMap<ChatColor, String>(ChatColor.class);
    private final ChatColor[] colors = ChatColor.values();

    public ColouredConsoleSender(CraftServer server) {
        super(server);
        this.reader = server.getReader();
        this.terminal = reader.getTerminal();

        replacements.put(ChatColor.BLACK, ANSICodes.attrib(0));
        replacements.put(ChatColor.DARK_BLUE, ANSICodes.attrib(34));
        replacements.put(ChatColor.DARK_GREEN, ANSICodes.attrib(32));
        replacements.put(ChatColor.DARK_AQUA, ANSICodes.attrib(36));
        replacements.put(ChatColor.DARK_RED, ANSICodes.attrib(31));
        replacements.put(ChatColor.DARK_PURPLE, ANSICodes.attrib(35));
        replacements.put(ChatColor.GOLD, ANSICodes.attrib(33));
        replacements.put(ChatColor.GRAY, ANSICodes.attrib(37));
        replacements.put(ChatColor.DARK_GRAY, ANSICodes.attrib(0));
        replacements.put(ChatColor.BLUE, ANSICodes.attrib(34));
        replacements.put(ChatColor.GREEN, ANSICodes.attrib(32));
        replacements.put(ChatColor.AQUA, ANSICodes.attrib(36));
        replacements.put(ChatColor.RED, ANSICodes.attrib(31));
        replacements.put(ChatColor.LIGHT_PURPLE, ANSICodes.attrib(35));
        replacements.put(ChatColor.YELLOW, ANSICodes.attrib(33));
        replacements.put(ChatColor.WHITE, ANSICodes.attrib(37));
    }

    @Override
    public void sendMessage(String message) {
        if (terminal.isANSISupported()) {
            String result = message;

            for (ChatColor color : colors) {
                if (replacements.containsKey(color)) {
                    result = result.replaceAll(color.toString(), replacements.get(color));
                } else {
                    result = result.replaceAll(color.toString(), "");
                }
            }
            System.out.println(result + ANSICodes.attrib(0));
        } else {
            super.sendMessage(message);
        }
    }
}
