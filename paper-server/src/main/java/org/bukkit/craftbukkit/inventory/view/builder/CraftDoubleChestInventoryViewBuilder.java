package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftDoubleChestInventoryViewBuilder<V extends InventoryView> extends CraftLocationInventoryViewBuilder<V> implements InventorySupport {

    private @Nullable Inventory inventory;

    public CraftDoubleChestInventoryViewBuilder(final MenuType<?> handle) {
        super(handle, null);
        super.defaultTitle = Component.translatable("container.chestDouble");
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftDoubleChestInventoryViewBuilder<V> builder = new CraftDoubleChestInventoryViewBuilder<>(super.handle);
        builder.title = title;
        builder.checkReachable = super.checkReachable;
        builder.bukkitLocation = super.bukkitLocation;
        return builder;
    }

    @Override
    public InventoryViewBuilder<InventoryView> inventory(final Inventory inventory) {
        Preconditions.checkArgument(inventory != null, "The provided inventory must not be null");
        Preconditions.checkArgument(!(inventory instanceof CraftInventoryCustom), "Can not set CraftInventoryCustom as a inventory for a view please use Server#createMenuInventory");
        this.inventory = inventory;
        return (InventoryViewBuilder<InventoryView>) this;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        setupLocation(); // prevents state from being corrupted since the builder mutates this
        if (this.inventory != null) {
            return ChestMenu.sixRows(player.nextContainerCounter(), player.getInventory(), ((CraftInventory) inventory).getInventory());
        }

        if (super.world == null) {
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }

        final ChestBlock chest = (ChestBlock) Blocks.CHEST;
        final DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> result = chest.combine(
            super.world.getBlockState(super.position), super.world, super.position, false
        );
        if (result instanceof DoubleBlockCombiner.NeighborCombineResult.Single<? extends ChestBlockEntity>) {
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }

        final MenuProvider combined = result.apply(ChestBlock.MENU_PROVIDER_COMBINER).orElse(null);
        if (combined == null) {
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }

        return combined.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }
}
