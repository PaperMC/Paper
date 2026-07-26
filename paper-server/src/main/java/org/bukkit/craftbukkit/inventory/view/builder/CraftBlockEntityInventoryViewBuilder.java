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
    private final boolean useFakeBlockEntity;
    private final @Nullable CraftBlockInventoryBuilder builder;

    public CraftBlockEntityInventoryViewBuilder(
        final MenuType<?> handle,
        final Block block,
        final @Nullable CraftBlockInventoryBuilder builder
    ) {
        this(handle, block, builder, true);
    }

    public CraftBlockEntityInventoryViewBuilder(
        final MenuType<?> handle,
        final Block block,
        final @Nullable CraftBlockInventoryBuilder builder,
        final boolean useFakeBlockEntity
    ) {
        super(handle);
        this.useFakeBlockEntity = useFakeBlockEntity;
        this.block = block;
        this.builder = builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (this.world == null) {
            this.world = player.level();
        }

        if (this.position == null) {
            this.position = player.blockPosition();
            return buildFakeBlockEntity(player);
        }

        final BlockEntity entity = this.world.getBlockEntity(position);
        if (!(entity instanceof final MenuConstructor container)) {
            return buildFakeBlockEntity(player);
        }

        final AbstractContainerMenu atBlock = container.createMenu(player.nextContainerCounter(), player.getInventory(), player);
        if (atBlock.getType() != super.handle) {
            return buildFakeBlockEntity(player);
        }

        if (!(entity instanceof final MenuProvider provider)) {
            throw new IllegalStateException("Provided blockEntity during MenuType creation can not find a default title! This is a bug!");
        }

        super.defaultTitle = provider.getDisplayName();
        return atBlock;
    }

    private AbstractContainerMenu buildFakeBlockEntity(final ServerPlayer player) {
        final MenuProvider inventory = this.builder.build(this.position, this.block.defaultBlockState());
        if (inventory instanceof final BlockEntity blockEntity) {
            blockEntity.setLevel(this.world);
            super.defaultTitle = inventory.getDisplayName();
        }

        if (!this.useFakeBlockEntity) { // gets around open noise for chest
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }

        return inventory.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftBlockEntityInventoryViewBuilder<V> copy = new CraftBlockEntityInventoryViewBuilder<>(super.handle, this.block, this.builder, this.useFakeBlockEntity);
        copy.world = this.world;
        copy.position = this.position;
        copy.checkReachable = super.checkReachable;
        copy.title = title;
        return copy;
    }

    public interface CraftBlockInventoryBuilder {
        MenuProvider build(BlockPos pos, BlockState state);
    }
}
