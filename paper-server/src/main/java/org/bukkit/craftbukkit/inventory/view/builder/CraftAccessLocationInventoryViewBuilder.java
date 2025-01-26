package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;

public class CraftAccessLocationInventoryViewBuilder<V extends InventoryView> extends CraftAbstractLocationInventoryViewBuilder<V> {

    private final Block block;

    public CraftAccessLocationInventoryViewBuilder(final MenuType<?> handle, final Block block) {
        super(handle);
        this.block = block;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
//        final ContainerLevelAccess access;
//        if (super.position == null) {
//            access = ContainerLevelAccess.create(player.level(), player.blockPosition());
//        } else {
//            access = ContainerLevelAccess.create(super.world, super.position);
//        }
        final BlockState state;
        final BlockPos defPos;
        final Level defLevel;
        if (super.position != null) {
            defPos = super.position;
            defLevel = super.world;
            state = super.world.getBlockState(position);
        } else {
            defPos = player.blockPosition();
            defLevel = player.level();
            state = block.defaultBlockState();
        }

        final MenuProvider provider = block.getMenuProvider(state, defLevel, defPos);
        super.defaultTitle = provider.getDisplayName();
        return provider.createMenu(player.nextContainerCounter(), player.getInventory(), player);
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftAccessLocationInventoryViewBuilder<V> copy = new CraftAccessLocationInventoryViewBuilder<>(this.handle, this.block);
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
