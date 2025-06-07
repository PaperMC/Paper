package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.view.builder.CraftInventorySupportBlockEntityInventoryViewBuilder.SimpleMenuBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jspecify.annotations.Nullable;

public class CraftStandardInventoryViewBuilder<V extends InventoryView> extends CraftAbstractInventoryViewBuilder<V> implements InventorySupport<V> {

    private final SimpleMenuBuilder smb;

    private @Nullable Inventory inventory;
    public CraftStandardInventoryViewBuilder(final MenuType<?> handle, SimpleMenuBuilder smb) {
        super(handle);
        this.smb = smb;
        super.defaultTitle = Component.translatable("container.chest");
    }

    @Override
    public InventoryViewBuilder<V> inventory(final Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (inventory != null) {
            return smb.build(player.nextContainerCounter(), player.getInventory(), ((CraftInventory) inventory).getInventory());
        }
        return super.handle.create(player.nextContainerCounter(), player.getInventory());
    }

    @Override
    public InventoryViewBuilder<V> copy() {
        final CraftStandardInventoryViewBuilder<V> copy = new CraftStandardInventoryViewBuilder<>(handle, smb);
        copy.title = this.title;
        return copy;
    }
}
