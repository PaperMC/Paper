package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityLootable;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public abstract class CraftLootable<T extends TileEntityLootable> extends CraftContainer<T> implements Nameable {

    public CraftLootable(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftLootable(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public String getCustomName() {
        T lootable = this.getSnapshot();
        return lootable.hasCustomName() ? CraftChatMessage.fromComponent(lootable.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);

        if (!this.getSnapshot().hasCustomName()) {
            lootable.setCustomName(null);
        }
    }
}
