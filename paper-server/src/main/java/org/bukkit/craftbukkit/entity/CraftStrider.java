package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Strider;

public class CraftStrider extends CraftAnimals implements Strider {

    public CraftStrider(CraftServer server, net.minecraft.world.entity.monster.Strider entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Strider getHandle() {
        return (net.minecraft.world.entity.monster.Strider) this.entity;
    }

    @Override
    public boolean isShivering() {
        return this.getHandle().isSuffocating();
    }

    @Override
    public void setShivering(boolean shivering) {
        this.getHandle().setSuffocating(shivering);
    }

    @Override
    public boolean hasSaddle() {
        return this.getHandle().isSaddled();
    }

    @Override
    public void setSaddle(boolean saddled) {
        this.getHandle().setItemSlot(EquipmentSlot.SADDLE, saddled ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY);
    }

    @Override
    public int getBoostTicks() {
        return this.getHandle().steering.boosting ? this.getHandle().steering.boostTimeTotal() : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        this.getHandle().steering.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return this.getHandle().steering.boosting ? this.getHandle().steering.boostTime : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!this.getHandle().steering.boosting) {
            return;
        }

        int max = this.getHandle().steering.boostTimeTotal();
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %s (inclusive)", max);

        this.getHandle().steering.boostTime = ticks;
    }

    @Override
    public Material getSteerMaterial() {
        return Material.WARPED_FUNGUS_ON_A_STICK;
    }
}
