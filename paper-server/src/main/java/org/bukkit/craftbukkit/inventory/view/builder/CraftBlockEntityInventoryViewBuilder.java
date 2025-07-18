package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftBlockEntityInventoryViewBuilder<V extends InventoryView> extends CraftLocationInventoryViewBuilder<V> {

    protected final BlockEntityProvider blockEntityProvider;
    protected final boolean useFakeBlockEntity;

    public CraftBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final BlockEntityProvider blockEntityProvider, boolean useFakeBlockEntity) {
        super(handle, block);
        this.blockEntityProvider = blockEntityProvider;
        this.useFakeBlockEntity = useFakeBlockEntity;
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftBlockEntityInventoryViewBuilder<V> builder = new CraftBlockEntityInventoryViewBuilder<>(super.handle, super.block, this.blockEntityProvider, this.useFakeBlockEntity);
        builder.title = super.title;
        builder.checkReachable = super.checkReachable;
        builder.bukkitLocation = super.bukkitLocation;
        return builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        setupLocation(); // prevents state from being corrupted since the builder mutates this
        if (super.position == null) {
            this.position = player.blockPosition();
            this.world = player.level();
            return buildFakeBlockEntity(player);
        }

        final BlockEntity entity = super.world.getBlockEntity(super.position);
        if (!(entity instanceof final MenuConstructor menuConstructor)) {
            return buildFakeBlockEntity(player);
        }

        final AbstractContainerMenu atBlock = menuConstructor.createMenu(player.nextContainerCounter(), player.getInventory(), player);
        if (atBlock.getType() != super.handle) {
            return buildFakeBlockEntity(player);
        }

        if (!(entity instanceof final MenuProvider provider)) {
            throw new IllegalStateException("Provided blockEntity during MenuType creation can not find a default title! This is a bug!");
        }

        super.defaultTitle = provider.getDisplayName();
        return atBlock;
    }

    protected AbstractContainerMenu buildFakeBlockEntity(final ServerPlayer player) {
        final MenuProvider inventory = this.blockEntityProvider.build(super.position, super.block.defaultBlockState());
        if (inventory instanceof final BlockEntity blockEntity) {
            blockEntity.setLevel(super.world);
            super.defaultTitle = inventory.getDisplayName();
        }

        if (!this.useFakeBlockEntity) { // gets around open noise for chest
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }

        return inventory.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }

    @FunctionalInterface
    public interface BlockEntityProvider {
        MenuProvider build(BlockPos position, BlockState state);
    }
}
