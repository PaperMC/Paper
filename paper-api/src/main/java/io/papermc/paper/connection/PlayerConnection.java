package io.papermc.paper.connection;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface PlayerConnection {

    void disconnect(Component component);

}
