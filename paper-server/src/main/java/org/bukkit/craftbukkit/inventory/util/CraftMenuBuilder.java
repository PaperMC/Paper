package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface CraftMenuBuilder {

    AbstractContainerMenu build(ServerPlayer player, MenuType<?> type);

    static CraftMenuBuilder worldAccess(LocationBoundContainerBuilder builder) {
        return (ServerPlayer player, MenuType<?> type) -> {
            return builder.build(player.nextContainerCounter(), player.getInventory(), ContainerLevelAccess.create(player.level(), player.blockPosition()));
        };
    }

    static CraftMenuBuilder tileEntity(TileEntityObjectBuilder objectBuilder, Block block) {
        return (ServerPlayer player, MenuType<?> type) -> {
            return objectBuilder.build(player.blockPosition(), block.defaultBlockState()).createMenu(player.nextContainerCounter(), player.getInventory(), player);
        };
    }

    interface TileEntityObjectBuilder {

        MenuProvider build(BlockPos blockPosition, BlockState blockData);
    }

    interface LocationBoundContainerBuilder {

        AbstractContainerMenu build(int syncId, Inventory inventory, ContainerLevelAccess access);
    }
}
