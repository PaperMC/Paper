package org.bukkit.craftbukkit.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EnderCrystal;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {

    public CraftEnderCrystal(CraftServer server, EndCrystal entity) {
        super(server, entity);
    }

    @Override
    public EndCrystal getHandle() {
        return (EndCrystal) this.entity;
    }

    @Override
    public boolean isShowingBottom() {
        return this.getHandle().showsBottom();
    }

    @Override
    public void setShowingBottom(boolean showing) {
        this.getHandle().setShowBottom(showing);
    }

    @Override
    public Location getBeamTarget() {
        BlockPos pos = this.getHandle().getBeamTarget();
        return pos == null ? null : CraftLocation.toBukkit(pos, this.getWorld());
    }

    @Override
    public void setBeamTarget(Location location) {
        if (location == null) {
            this.getHandle().setBeamTarget(null);
        } else if (location.getWorld() != this.getWorld()) {
            throw new IllegalArgumentException("Cannot set beam target location to different world");
        } else {
            this.getHandle().setBeamTarget(CraftLocation.toBlockPosition(location));
        }
    }
}
