package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftBlockEntityInventorySupportViewBuilder<V extends InventoryView> extends CraftBlockEntityInventoryViewBuilder<V> implements InventorySupport {

    private final BlockEntityContainerBuilder containerBuilder;

    private @Nullable Inventory inventory;

    public CraftBlockEntityInventorySupportViewBuilder(final MenuType<?> handle, final Block block, final BlockEntityProvider blockEntityProvider, BlockEntityContainerBuilder containerBuilder, final boolean useFakeBlockEntity) {
        super(handle, block, blockEntityProvider, useFakeBlockEntity);
        this.containerBuilder = containerBuilder;
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftBlockEntityInventorySupportViewBuilder<V> builder = new CraftBlockEntityInventorySupportViewBuilder<>(super.handle, super.block, super.blockEntityProvider, this.containerBuilder, super.useFakeBlockEntity);
        builder.title = super.title;
        builder.checkReachable = super.checkReachable;
        builder.bukkitLocation = super.bukkitLocation;
        return super.copy();
    }

    @Override
    public InventoryViewBuilder<InventoryView> inventory(final Inventory inventory) {
        this.inventory = inventory;
        return (InventoryViewBuilder<InventoryView>) this;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        setupLocation(); // prevents state from being corrupted since the builder mutates this
        if (this.inventory != null) {
            super.position = player.blockPosition();
            super.world = player.level();
            buildFakeBlockEntity(player); // just fetch the default title
            return this.containerBuilder.build(player.nextContainerCounter(), player.getInventory(), ((CraftInventory) inventory).getInventory());
        }

        return super.buildContainer(player);
    }

    @FunctionalInterface
    public interface BlockEntityContainerBuilder {
        AbstractContainerMenu build(int id, net.minecraft.world.entity.player.Inventory inventory, Container container);
    }
}
