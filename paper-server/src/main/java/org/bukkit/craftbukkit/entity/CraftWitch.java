package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Witch;
// Paper start
import com.destroystokyo.paper.entity.CraftRangedEntity;
import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
// Paper end

public class CraftWitch extends CraftRaider implements Witch, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.Witch> { // Paper
    public CraftWitch(CraftServer server, net.minecraft.world.entity.monster.Witch entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Witch getHandle() {
        return (net.minecraft.world.entity.monster.Witch) this.entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public boolean isDrinkingPotion() {
        return this.getHandle().isDrinkingPotion();
    }
    // Paper start
    public int getPotionUseTimeLeft() {
        return getHandle().usingTime;
    }

    @Override
    public void setPotionUseTimeLeft(int ticks) {
        getHandle().usingTime = ticks;
    }

    public ItemStack getDrinkingPotion() {
        return CraftItemStack.asCraftMirror(getHandle().getMainHandItem());
    }

    public void setDrinkingPotion(ItemStack potion) {
        Preconditions.checkArgument(potion == null || potion.getType().isEmpty() || potion.getType() == Material.POTION, "must be potion, air, or null");
        getHandle().setDrinkingPotion(CraftItemStack.asNMSCopy(potion));
    }
    // Paper end
}
