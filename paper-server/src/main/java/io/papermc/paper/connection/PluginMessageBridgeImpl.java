package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.jspecify.annotations.NullMarked;
import java.util.Set;

@NullMarked
public interface PluginMessageBridgeImpl {

    boolean DISABLE_CHANNEL_LIMIT = System.getProperty("paper.disableChannelLimit") != null; // Paper - add a flag to disable the channel limit

    default boolean addChannel(String channel) {
        Preconditions.checkState(DISABLE_CHANNEL_LIMIT || this.channels().size() < 128, "Cannot register channel. Too many channels registered!"); // Paper - flag to disable channel limit
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels().add(channel)) {
            if (this instanceof CraftPlayer player) {
                Bukkit.getPluginManager().callEvent(new PlayerRegisterChannelEvent(player, channel));
            }
            return true;
        }

        return false;
    }

    default boolean removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels().remove(channel)) {
            if (this instanceof CraftPlayer player) {
                Bukkit.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(player, channel));
            }
            return true;
        }

        return false;
    };

    Set<String> channels();
}
