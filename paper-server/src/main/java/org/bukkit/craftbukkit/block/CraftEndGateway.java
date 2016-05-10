package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.TileEntityEndGateway;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftEndGateway extends CraftBlockState implements EndGateway {

    private final CraftWorld world;
    private final TileEntityEndGateway gateway;

    public CraftEndGateway(Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        gateway = (TileEntityEndGateway) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftEndGateway(final Material material, TileEntityEndGateway te) {
        super(material);
        world = null;
        this.gateway = te;
    }
    
    @Override
    public Location getExitLocation() {
        BlockPosition pos = gateway.exitPortal;
        return pos == null ? null : new Location(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            gateway.exitPortal = null;
        } else if (location.getWorld() != world) {
            throw new IllegalArgumentException("Cannot set exit location to different world");
        } else {
            gateway.exitPortal = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }

    @Override
    public boolean isExactTeleport() {
        return gateway.exactTeleport;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        gateway.exactTeleport = exact;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            gateway.update();
        }

        return result;
    }

    @Override
    public TileEntityEndGateway getTileEntity() {
        return gateway;
    }
}
