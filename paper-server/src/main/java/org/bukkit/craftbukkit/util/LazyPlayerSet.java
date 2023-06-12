package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    HashSet<Player> makeReference() {
        Preconditions.checkState(reference == null, "Reference already created!");
        List<EntityPlayer> players = server.getPlayerList().players;
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (EntityPlayer player : players) {
            reference.add(player.getBukkitEntity());
        }
        return reference;
    }
}
