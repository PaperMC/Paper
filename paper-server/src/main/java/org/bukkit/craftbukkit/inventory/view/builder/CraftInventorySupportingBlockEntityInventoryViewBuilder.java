package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.view.builder.CraftInventoryViewBuilders.CraftBlockEntityMenuBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupportingInventoryViewBuilder;
import org.bukkit.inventory.view.builder.LocationInventorySupportingInventoryViewBuilder;
import org.jspecify.annotations.Nullable;

import static org.bukkit.craftbukkit.inventory.view.builder.CraftInventoryViewBuilders.*;

public class CraftInventorySupportingBlockEntityInventoryViewBuilder<V extends InventoryView> extends CraftBlockEntityInventoryViewBuilder<V> implements LocationInventorySupportingInventoryViewBuilder<V> {

    private final CraftInventoryMenuBuilder invmBuilder;
    @Nullable
    private Container container;

    public CraftInventorySupportingBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final CraftBlockEntityMenuBuilder builder, final CraftInventoryMenuBuilder invmBuilder) {
        this(handle, block, builder, invmBuilder, true);
    }

    public CraftInventorySupportingBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final CraftBlockEntityMenuBuilder builder, final CraftInventoryMenuBuilder invmBuilder, boolean useFakeBlockEntity) {
        super(handle, block, builder, useFakeBlockEntity);
        this.invmBuilder = invmBuilder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (this.container != null) {
            return this.invmBuilder.build(player.nextContainerCounter(), player.getInventory(), this.container);
        }
        return super.buildContainer(player);
    }

    @Override
    public InventorySupportingInventoryViewBuilder<V> inventory(final Inventory inventory) {
        Preconditions.checkArgument(inventory != null, "The provided inventory msut not be null");
        this.container = ((CraftInventory) inventory).getInventory();
        return this;
    }

    @Override
    public CraftInventorySupportingBlockEntityInventoryViewBuilder<V> copy() {
        final var copy = new CraftInventorySupportingBlockEntityInventoryViewBuilder<V>(super.handle, super.block, super.builder, this.invmBuilder, super.useFakeBlockEntity);
        copy.world = this.world;
        copy.position = this.position;
        copy.checkReachable = super.checkReachable;
        copy.title = super.title;
        return copy;
    }

    @Override
    public CraftInventorySupportingBlockEntityInventoryViewBuilder<V> title(final @Nullable Component title) {
        return (CraftInventorySupportingBlockEntityInventoryViewBuilder<V>) super.title(title);
    }
}
