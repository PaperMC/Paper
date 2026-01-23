package io.papermc.paper.connection;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.bukkit.craftbukkit.event.player.CraftPlayerRegisterChannelEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerUnregisterChannelEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PluginMessageBridgeImpl {

    boolean DISABLE_CHANNEL_LIMIT = System.getProperty("paper.disableChannelLimit") != null; // Paper - add a flag to disable the channel limit

    default boolean addChannel(String channel) {
        Preconditions.checkState(DISABLE_CHANNEL_LIMIT || this.channels().size() < 128, "Cannot register channel. Too many channels registered!"); // Paper - flag to disable channel limit
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (this.channels().add(channel)) {
            if (this instanceof Player player) {
                new CraftPlayerRegisterChannelEvent(player, channel).callEvent();
            }
            return true;
        }

        return false;
    }

    default boolean removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (this.channels().remove(channel)) {
            if (this instanceof Player player) {
                new CraftPlayerUnregisterChannelEvent(player, channel).callEvent();
            }
            return true;
        }

        return false;
    }

    Set<String> channels();
}
