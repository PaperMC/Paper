package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;

public class CraftStandardInventoryViewBuilder<V extends InventoryView> extends CraftAbstractInventoryViewBuilder<V> {

    public CraftStandardInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
        super.defaultTitle = Component.translatable("container.chest");
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
