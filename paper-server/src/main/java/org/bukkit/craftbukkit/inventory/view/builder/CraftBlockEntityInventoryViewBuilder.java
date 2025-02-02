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
import org.jspecify.annotations.Nullable;

public class CraftBlockEntityInventoryViewBuilder<V extends InventoryView> extends CraftAbstractLocationInventoryViewBuilder<V> {

    private final Block block;
    private final @Nullable CraftTileInventoryBuilder builder;

    public CraftBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final @Nullable CraftTileInventoryBuilder builder) {
        super(handle);
        this.block = block;
        this.builder = builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (this.world == null) {
            this.world = player.level();
        }

        if (this.position == null) {
            this.position = BlockPos.ZERO;
        }

        final BlockEntity entity = this.world.getBlockEntity(position);
        if (!(entity instanceof final MenuConstructor container)) {
            return buildFakeTile(player);
        }

        final AbstractContainerMenu atBlock = container.createMenu(player.nextContainerCounter(), player.getInventory(), player);
        if (atBlock.getType() != super.handle) {
            return buildFakeTile(player);
        }

        return atBlock;
    }

    private AbstractContainerMenu buildFakeTile(final ServerPlayer player) {
        if (this.builder == null) {
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }
        final MenuProvider inventory = this.builder.build(this.position, this.block.defaultBlockState());
        if (inventory instanceof final BlockEntity tile) {
            tile.setLevel(this.world);
        }
        return inventory.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftBlockEntityInventoryViewBuilder<V> copy = new CraftBlockEntityInventoryViewBuilder<>(super.handle, this.block, this.builder);
        copy.world = this.world;
        copy.position = this.position;
        copy.checkReachable = super.checkReachable;
        copy.title = title;
        return copy;
    }

    public interface CraftTileInventoryBuilder {
        MenuProvider build(BlockPos blockPosition, BlockState blockData);
    }
}
