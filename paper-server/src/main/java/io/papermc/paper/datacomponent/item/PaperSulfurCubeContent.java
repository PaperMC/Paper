package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;

public record PaperSulfurCubeContent(
    net.minecraft.world.item.component.SulfurCubeContent impl
) implements SulfurCubeContent, Handleable<net.minecraft.world.item.component.SulfurCubeContent> {

    @Override
    public net.minecraft.world.item.component.SulfurCubeContent getHandle() {
        return this.impl;
    }

    @Override
    public ItemStack absorbedItem() {
        return CraftItemStack.asBukkitCopy(this.impl.absorbedBlockItemStack());
    }
}
