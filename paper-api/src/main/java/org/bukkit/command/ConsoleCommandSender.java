
package org.bukkit.command;

import org.bukkit.Server;

/**
 * Represents CLI input from a console
 */
public class ConsoleCommandSender implements CommandSender {
    private final Server server;

    public ConsoleCommandSender(Server server) {
        this.server = server;
    }

    public void sendMessage(String message) {
        System.out.println(message.replaceAll("(?i)\u00A7[0-F]", ""));
    }

    public boolean isOp() {
        return true;
    }

    public boolean isPlayer() {
        return false;
    }

    public Server getServer() {
        return server;
    }
}
