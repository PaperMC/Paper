package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;

public class CraftStandardInventoryViewBuilder<V extends InventoryView> extends CraftAbstractInventoryViewBuilder<V> {

    public CraftStandardInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
        super.defaultTitle = ChestBlockEntity.DEFAULT_NAME;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        return super.handle.create(player.nextContainerCounter(), player.getInventory());
    }

    @Override
    public InventoryViewBuilder<V> copy() {
        final CraftStandardInventoryViewBuilder<V> copy = new CraftStandardInventoryViewBuilder<>(handle);
        copy.title = this.title;
        return copy;
    }
}
