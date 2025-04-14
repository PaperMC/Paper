package org.bukkit.craftbukkit.block;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.util.CraftLocation;

public class CraftEndGateway extends CraftBlockEntityState<TheEndGatewayBlockEntity> implements EndGateway {

    public CraftEndGateway(World world, TheEndGatewayBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftEndGateway(CraftEndGateway state, Location location) {
        super(state, location);
    }

    @Override
    public Location getExitLocation() {
        BlockPos pos = this.getSnapshot().exitPortal;
        return pos == null ? null : CraftLocation.toBukkit(pos, this.isPlaced() ? this.getWorld() : null);
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            this.getSnapshot().exitPortal = null;
        } else if (!Objects.equals(location.getWorld(), this.isPlaced() ? this.getWorld() : null)) {
            throw new IllegalArgumentException("Cannot set exit location to different world");
        } else {
            this.getSnapshot().exitPortal = CraftLocation.toBlockPosition(location);
        }
    }

    @Override
    public boolean isExactTeleport() {
        return this.getSnapshot().exactTeleport;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        this.getSnapshot().exactTeleport = exact;
    }

    @Override
    public long getAge() {
        return this.getSnapshot().age;
    }

    @Override
    public void setAge(long age) {
        this.getSnapshot().age = age;
    }

    @Override
    public void applyTo(TheEndGatewayBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        if (this.getSnapshot().exitPortal == null) {
            blockEntity.exitPortal = null;
        }
    }

    @Override
    public CraftEndGateway copy() {
        return new CraftEndGateway(this, null);
    }

    @Override
    public CraftEndGateway copy(Location location) {
        return new CraftEndGateway(this, location);
    }
}
