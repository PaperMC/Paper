package io.papermc.paper.item;

import com.google.common.base.Preconditions;
import net.minecraft.core.component.DataComponentPatch;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.ItemType;

public final class ItemStackTemplateBridgeImpl implements ItemStackTemplateBridge {

    @Override
    public ItemStackTemplate.Builder builder(final ItemType type) {
        Preconditions.checkArgument(type != ItemType.AIR, "type cannot be air");
        return new PaperItemStackTemplate.BuilderImpl(CraftItemType.bukkitToMinecraftHolderNew(type), 1, DataComponentPatch.EMPTY);
    }
}
