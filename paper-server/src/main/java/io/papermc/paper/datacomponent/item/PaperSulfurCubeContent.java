package io.papermc.paper.datacomponent.item;

import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.item.PaperItemStackTemplate;
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

    @Override
    public ItemStackTemplate absorbedItemTemplate() {
        return new PaperItemStackTemplate(this.impl.absorbedBlockItemStack());
    }
}
