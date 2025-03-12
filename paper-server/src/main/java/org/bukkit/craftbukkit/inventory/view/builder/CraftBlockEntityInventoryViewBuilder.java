package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.bukkit.craftbukkit.inventory.view.builder.CraftInventoryViewBuilders.CraftBlockEntityMenuBuilder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;

public class CraftBlockEntityInventoryViewBuilder<V extends InventoryView> extends CraftAbstractLocationInventoryViewBuilder<V> {

    protected final Block block;
    protected final boolean useFakeBlockEntity;
    protected final CraftBlockEntityMenuBuilder builder;

    public CraftBlockEntityInventoryViewBuilder(
            final MenuType<?> handle,
            final Block block,
            final CraftBlockEntityMenuBuilder builder
    ) {
        this(handle, block, builder, true);
    }

    public CraftBlockEntityInventoryViewBuilder(
            final MenuType<?> handle,
            final Block block,
            final CraftBlockEntityMenuBuilder builder,
            final boolean useFakeBlockEntity
    ) {
        super(handle);
        this.useFakeBlockEntity = useFakeBlockEntity;
        this.block = block;
        this.builder = builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        final var built = CraftInventoryViewBuilders.buildBlockEntityMenu(this.handle, player, super.world, super.position, this.block, this.builder, this.useFakeBlockEntity);
        super.defaultTitle = built.second();
        return built.first();
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
}
