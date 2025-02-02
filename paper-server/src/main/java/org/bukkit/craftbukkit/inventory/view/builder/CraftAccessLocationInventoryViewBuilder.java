package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;

public class CraftAccessLocationInventoryViewBuilder<V extends InventoryView> extends CraftAbstractLocationInventoryViewBuilder<V> {

    private final CraftAccessContainerObjectBuilder containerBuilder;

    public CraftAccessLocationInventoryViewBuilder(final MenuType<?> handle, final CraftAccessContainerObjectBuilder containerBuilder) {
        super(handle);
        this.containerBuilder = containerBuilder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        final ContainerLevelAccess access;
        if (super.position == null) {
            access = ContainerLevelAccess.create(player.level(), BlockPos.ZERO);
        } else {
            access = ContainerLevelAccess.create(super.world, super.position);
        }

        return this.containerBuilder.build(player.nextContainerCounter(), player.getInventory(), access);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftAccessLocationInventoryViewBuilder<V> copy = new CraftAccessLocationInventoryViewBuilder<>(this.handle, this.containerBuilder);
        copy.world = super.world;
        copy.position = super.position;
        copy.checkReachable = super.checkReachable;
        copy.title = title;
        return copy;
    }

    public interface CraftAccessContainerObjectBuilder {
        AbstractContainerMenu build(final int syncId, final Inventory inventory, ContainerLevelAccess access);
    }
}
