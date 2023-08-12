package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.ItemDisplayContext;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public class CraftItemDisplay extends CraftDisplay implements ItemDisplay {

    public CraftItemDisplay(CraftServer server, net.minecraft.world.entity.Display.ItemDisplay entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display.ItemDisplay getHandle() {
        return (net.minecraft.world.entity.Display.ItemDisplay) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftItemDisplay";
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asBukkitCopy(getHandle().getItemStack());
    }

    @Override
    public void setItemStack(ItemStack item) {
        getHandle().setItemStack(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemDisplayTransform getItemDisplayTransform() {
        return ItemDisplayTransform.values()[getHandle().getItemTransform().ordinal()];
    }

    @Override
    public void setItemDisplayTransform(ItemDisplayTransform display) {
        Preconditions.checkArgument(display != null, "Display cannot be null");

        getHandle().setItemTransform(ItemDisplayContext.BY_ID.apply(display.ordinal()));
    }
}
