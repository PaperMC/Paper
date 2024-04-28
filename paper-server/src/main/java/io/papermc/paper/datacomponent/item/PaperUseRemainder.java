package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;

public record PaperUseRemainder(
    net.minecraft.world.item.component.UseRemainder impl
) implements UseRemainder, Handleable<net.minecraft.world.item.component.UseRemainder> {

    @Override
    public net.minecraft.world.item.component.UseRemainder getHandle() {
        return this.impl;
    }

    @Override
    public ItemStack transformInto() {
        return CraftItemStack.asBukkitCopy(this.impl.convertInto());
    }
}
