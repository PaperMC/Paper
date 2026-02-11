package io.papermc.paper.util.capture;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public class BoneMealContext {

    public ServerLevel ogServerLevel;
    public @Nullable ServerPlayer player;
    public boolean precancelStructureEvent = false;
    public TreeType treeHook; // just make this a field

    public @Nullable Player getBukkitPlayer() {
        return this.player == null ? null : this.player.getBukkitEntity();
    }
}
