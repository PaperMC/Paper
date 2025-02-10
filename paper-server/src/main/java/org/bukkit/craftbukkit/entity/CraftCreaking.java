package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.creaking.Creaking;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Optional;

@NullMarked
public class CraftCreaking extends CraftMonster implements org.bukkit.entity.Creaking {

    public CraftCreaking(CraftServer server, Creaking entity) {
        super(server, entity);
    }

    @Override
    public Creaking getHandle() {
        return (Creaking) this.entity;
    }

    @Nullable
    @Override
    public Location getHome() {
        BlockPos homePos = this.getHandle().getHomePos();
        return homePos != null ? CraftLocation.toBukkit(homePos, this.getHandle().level()) : null;
    }

    @Override
    public void detachHome() {
        this.getHandle().getEntityData().set(Creaking.HOME_POS, Optional.empty());
    }

    @Override
    public void activate(Player player) {
        Preconditions.checkArgument(player != null, "player cannot be null");
        this.getHandle().activate(((CraftPlayer) player).getHandle());
    }

    @Override
    public void deactivate() {
        this.getHandle().deactivate();
    }

    @Override
    public boolean isActive() {
        return this.getHandle().isActive();
    }

    @Override
    public String toString() {
        return "CraftCreaking";
    }
}
