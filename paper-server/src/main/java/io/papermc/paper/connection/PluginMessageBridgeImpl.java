package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PluginMessageBridgeImpl {

    boolean DISABLE_CHANNEL_LIMIT = System.getProperty("paper.disableChannelLimit") != null;
    int CHANNEL_LIMIT = Integer.getInteger("paper.pluginMessageChannelLimit", 128);

    default boolean addChannel(String channel) {
        Preconditions.checkState(DISABLE_CHANNEL_LIMIT || this.channels().size() < CHANNEL_LIMIT, "Cannot register channel. Too many channels registered!");
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
    }

    Set<String> channels();
}
