package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.ITileInventory;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;

public interface CraftMenuBuilder {

    Container build(EntityPlayer player, Containers<?> type);

    static CraftMenuBuilder worldAccess(LocationBoundContainerBuilder builder) {
        return (EntityPlayer player, Containers<?> type) -> {
            return builder.build(player.nextContainerCounter(), player.getInventory(), ContainerAccess.create(player.level(), player.blockPosition()));
        };
    }

    static CraftMenuBuilder tileEntity(TileEntityObjectBuilder objectBuilder, Block block) {
        return (EntityPlayer player, Containers<?> type) -> {
            return objectBuilder.build(player.blockPosition(), block.defaultBlockState()).createMenu(player.nextContainerCounter(), player.getInventory(), player);
        };
    }

    interface TileEntityObjectBuilder {

        ITileInventory build(BlockPosition blockPosition, IBlockData blockData);
    }

    interface LocationBoundContainerBuilder {

        Container build(int syncId, PlayerInventory inventory, ContainerAccess access);
    }
}
