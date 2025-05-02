package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected Set<Player> makeReference() { // Paper - protected
        Preconditions.checkState(this.reference == null, "Reference already created!");
        return makePlayerSet(this.server);
    }

    public static Set<Player> makePlayerSet(final MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().players;
        Set<Player> reference = new HashSet<>(players.size());
        for (ServerPlayer player : players) {
            reference.add(player.getBukkitEntity());
        }
        return reference;
    }
}
