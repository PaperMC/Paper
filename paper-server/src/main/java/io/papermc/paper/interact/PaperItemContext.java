package io.papermc.paper.interact;

import net.minecraft.world.InteractionResult;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public record PaperItemContext(InteractionResult.ItemContext handle) implements ItemContext {

    @Override
    public boolean wasItemInteraction() {
        return handle().wasItemInteraction();
    }

    @Override
    public ItemStack heldItemTransformedTo() {
        net.minecraft.world.item.ItemStack item = handle.heldItemTransformedTo();
        return item == null ? null : CraftItemStack.asBukkitCopy(item);
    }

}
