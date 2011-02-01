
package org.bukkit.command;

/**
 * Represents CLI input from a console
 */
public class ConsoleCommandSender implements CommandSender {
    public void sendMessage(String message) {
        System.out.println(message.replaceAll("(?i)\u00A7[0-F]", ""));
    }

    public boolean isOp() {
        return true;
    }

    public boolean isPlayer() {
        return false;
    }
}
