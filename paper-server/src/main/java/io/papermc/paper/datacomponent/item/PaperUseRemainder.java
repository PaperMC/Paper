package io.papermc.paper.datacomponent.item;

import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.item.PaperItemStackTemplate;
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

    @Override
    public ItemStackTemplate transformIntoTemplate() {
        return new PaperItemStackTemplate(this.impl.convertInto());
    }
}
