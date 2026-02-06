package io.papermc.paper.util.capture;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class BoneMealContext {

    public ServerLevel ogServerLevel;
    public ServerPlayer player;
    public boolean precancelStructureEvent = false;
    public java.util.concurrent.atomic.AtomicReference<org.bukkit.TreeType> treeHook = new AtomicReference<>(); // just make this a field


    @Nullable
    public Player getBukkitPlayer() {
        return this.player == null ? null : this.player.getBukkitEntity();
    }

}
