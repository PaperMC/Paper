package org.bukkit.command;

import java.net.SocketAddress;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.1.0
 */
public interface RemoteConsoleCommandSender extends CommandSender {

    /**
     * Gets the socket address of this remote sender.
     *
     * @return the remote sender's address
     * @since 1.20.1
     */
    @NotNull
    public SocketAddress getAddress();
}
