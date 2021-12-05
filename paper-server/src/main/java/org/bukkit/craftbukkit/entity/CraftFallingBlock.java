package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.FallingBlock;

public class CraftFallingBlock extends CraftEntity implements FallingBlock {

    public CraftFallingBlock(CraftServer server, FallingBlockEntity entity) {
        super(server, entity);
    }

    @Override
    public FallingBlockEntity getHandle() {
        return (FallingBlockEntity) this.entity;
    }

    @Override
    public String toString() {
        return "CraftFallingBlock";
    }

    @Override
    public Material getMaterial() {
        return this.getBlockData().getMaterial();
    }

    @Override
    public BlockData getBlockData() {
        return CraftBlockData.fromData(this.getHandle().getBlockState());
    }
    // Paper start - Expand FallingBlock API
    @Override
    public void setBlockData(final BlockData blockData) {
        Preconditions.checkArgument(blockData != null, "blockData");
        final net.minecraft.world.level.block.state.BlockState oldState = this.getHandle().blockState, newState = ((CraftBlockData) blockData).getState();
        this.getHandle().blockState = newState;
        this.getHandle().blockData = null;

        if (oldState != newState) this.update();
    }

    @Override
    public org.bukkit.block.BlockState getBlockState() {
        return org.bukkit.craftbukkit.block.CraftBlockStates.getBlockState(this.getHandle().blockState, this.getHandle().blockData);
    }

    @Override
    public void setBlockState(final org.bukkit.block.BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState");
        // Calls #update if needed, the block data compound tag is not synced with the client and hence can be mutated after the sync with clients.
        // The call also clears any potential old block data.
        this.setBlockData(blockState.getBlockData());
        if (blockState instanceof final org.bukkit.craftbukkit.block.CraftBlockEntityState<?> tileEntity) this.getHandle().blockData = tileEntity.getSnapshotNBT();
    }
    // Paper end - Expand FallingBlock API

    @Override
    public boolean getDropItem() {
        return this.getHandle().dropItem;
    }

    @Override
    public void setDropItem(boolean drop) {
        this.getHandle().dropItem = drop;
    }

    @Override
    public boolean getCancelDrop() {
        return this.getHandle().cancelDrop;
    }

    @Override
    public void setCancelDrop(boolean cancelDrop) {
        this.getHandle().cancelDrop = cancelDrop;
    }

    @Override
    public boolean canHurtEntities() {
        return this.getHandle().hurtEntities;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        this.getHandle().hurtEntities = hurtEntities;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityFallingBlock
        this.getHandle().time = value;
    }

    @Override
    public float getDamagePerBlock() {
        return this.getHandle().fallDamagePerDistance;
    }

    @Override
    public void setDamagePerBlock(float damage) {
        Preconditions.checkArgument(damage >= 0.0, "damage must be >= 0.0, given %s", damage);

        this.getHandle().fallDamagePerDistance = damage;
        if (damage > 0.0) {
            this.setHurtEntities(true);
        }
    }

    @Override
    public int getMaxDamage() {
        return this.getHandle().fallDamageMax;
    }

    @Override
    public void setMaxDamage(int damage) {
        Preconditions.checkArgument(damage >= 0, "damage must be >= 0, given %s", damage);

        this.getHandle().fallDamageMax = damage;
        if (damage > 0) {
            this.setHurtEntities(true);
        }
    }
    // Paper start - Expand FallingBlock API
    @Override
    public boolean doesAutoExpire() {
        return this.getHandle().autoExpire;
    }

    @Override
    public void shouldAutoExpire(boolean autoExpires) {
        this.getHandle().autoExpire = autoExpires;
    }
    // Paper end - Expand FallingBlock API
}
