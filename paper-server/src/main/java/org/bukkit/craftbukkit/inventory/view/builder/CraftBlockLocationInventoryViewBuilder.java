package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftBlockLocationInventoryViewBuilder<V extends InventoryView> extends CraftLocationInventoryViewBuilder<V> {

    public CraftBlockLocationInventoryViewBuilder(final MenuType<?> handle, final Block block) {
        super(handle, block);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftBlockLocationInventoryViewBuilder<V> builder = new CraftBlockLocationInventoryViewBuilder<>(super.handle, super.block);
        builder.title = super.title;
        builder.checkReachable = super.checkReachable;
        builder.bukkitLocation = super.bukkitLocation;
        return builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        setupLocation(); // prevents state from being corrupted since the builder mutates this
        final BlockState blockState;
        if (super.position == null) {
            super.position = player.blockPosition();
            super.world = player.level();
            blockState = super.block.defaultBlockState();
        } else {
            blockState = super.world.getBlockState(super.position);
        }

        final MenuProvider provider = block.getMenuProvider(blockState, super.world, super.position);
        super.defaultTitle = provider.getDisplayName();
        return provider.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }
}
