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
}
