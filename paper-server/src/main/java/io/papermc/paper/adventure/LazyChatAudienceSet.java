package io.papermc.paper.adventure;

import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.audience.Audience;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.LazyHashSet;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.entity.Player;

final class LazyChatAudienceSet extends LazyHashSet<Audience> {
    private final MinecraftServer server;

    public LazyChatAudienceSet(final MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected Set<Audience> makeReference() {
        final Set<Player> playerSet = LazyPlayerSet.makePlayerSet(this.server);
        final HashSet<Audience> audiences = new HashSet<>(playerSet);
        audiences.add(Bukkit.getConsoleSender());
        return audiences;
    }
}
