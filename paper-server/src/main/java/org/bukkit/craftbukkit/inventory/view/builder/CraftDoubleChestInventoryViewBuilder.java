package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;

public class CraftDoubleChestInventoryViewBuilder<V extends InventoryView> extends CraftAbstractLocationInventoryViewBuilder<V> {

    public CraftDoubleChestInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
        super.defaultTitle = Component.translatable("container.chestDouble");
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
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

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftDoubleChestInventoryViewBuilder<V> copy = new CraftDoubleChestInventoryViewBuilder<>(super.handle);
        copy.world = this.world;
        copy.position = this.position;
        copy.checkReachable = super.checkReachable;
        copy.title = title;
        return copy;
    }
}
