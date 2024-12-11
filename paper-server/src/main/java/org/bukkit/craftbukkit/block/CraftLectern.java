package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.inventory.CraftInventoryLectern;
import org.bukkit.inventory.Inventory;

public class CraftLectern extends CraftBlockEntityState<LecternBlockEntity> implements Lectern {

    public CraftLectern(World world, LecternBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftLectern(CraftLectern state, Location location) {
        super(state, location);
    }

    @Override
    public int getPage() {
        return this.getSnapshot().getPage();
    }

    @Override
    public void setPage(int page) {
        this.getSnapshot().setPage(page);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventoryLectern(this.getSnapshot().bookAccess);
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryLectern(this.getTileEntity().bookAccess);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.getType() == Material.LECTERN && this.getWorldHandle() instanceof net.minecraft.world.level.Level) {
            LecternBlock.signalPageChange(this.world.getHandle(), this.getPosition(), this.getHandle());
        }

        return result;
    }

    @Override
    public CraftLectern copy() {
        return new CraftLectern(this, null);
    }

    @Override
    public CraftLectern copy(Location location) {
        return new CraftLectern(this, location);
    }
}
