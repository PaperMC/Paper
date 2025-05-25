package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.Optionull;
import net.minecraft.world.entity.monster.creaking.Creaking;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftCreaking extends CraftMonster implements org.bukkit.entity.Creaking {

    public CraftCreaking(final CraftServer server, final Creaking entity) {
        super(server, entity);
    }

    @Override
    public Creaking getHandle() {
        return (Creaking) this.entity;
    }

    @Nullable
    @Override
    public Location getHome() {
        return Optionull.map(this.getHandle().getHomePos(), pos -> CraftLocation.toBukkit(pos, this.getHandle().level()));
    }

    @Override
    public void activate(final Player player) {
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
}
