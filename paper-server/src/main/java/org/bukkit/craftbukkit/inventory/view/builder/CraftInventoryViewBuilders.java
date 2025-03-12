package org.bukkit.craftbukkit.inventory.view.builder;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CraftInventoryViewBuilders {

    public interface CraftBlockEntityMenuBuilder {
        MenuProvider build(BlockPos pos, BlockState state);
    }

    public interface CraftInventoryMenuBuilder {
        CraftInventoryMenuBuilder GENERIC_9X3 = (syncId, playerInventory, container) -> new ChestMenu(MenuType.GENERIC_9x3, syncId, playerInventory, container, 3);

        AbstractContainerMenu build(int syncId, Inventory playerInventory, Container container);
    }

    public interface CraftAccessMenuBuilder {
        AbstractContainerMenu build(final int syncId, final Inventory inventory, ContainerLevelAccess access);
    }

    public static Pair<AbstractContainerMenu, Component> buildBlockEntityMenu(final MenuType<?> type, final ServerPlayer player, Level level, BlockPos pos, final Block block, final CraftBlockEntityMenuBuilder builder, boolean useFakeBlockEntity) {
        if (level == null) {
            level = player.level();
        }

        if (pos == null) {
            pos = player.blockPosition();
            return buildFakeBlockEntityMenu(type, player, level, pos, block, builder, useFakeBlockEntity);
        }

        final BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof final MenuConstructor container)) {
            return buildFakeBlockEntityMenu(type, player, level, pos, block, builder, useFakeBlockEntity);
        }

        final AbstractContainerMenu atBlock = container.createMenu(player.nextContainerCounter(), player.getInventory(), player);
        if (atBlock.getType() != type) {
            return buildFakeBlockEntityMenu(type, player, level, pos, block, builder, useFakeBlockEntity);
        }

        if (!(entity instanceof final MenuProvider provider)) {
            throw new IllegalStateException("Provided blockEntity during MenuType creation can not find a default title! This is a bug!");
        }

        return Pair.of(atBlock, provider.getDisplayName());
    }

    public static Pair<AbstractContainerMenu, Component> buildFakeBlockEntityMenu(final MenuType<?> type, final ServerPlayer player, final Level level, final BlockPos pos, final Block block, final CraftBlockEntityMenuBuilder builder, boolean useFakeBlockEntity) {
        final MenuProvider inventory = builder.build(pos, block.defaultBlockState());
        Component defaultTitle = null;
        if (inventory instanceof final BlockEntity blockEntity) {
            blockEntity.setLevel(level);
            defaultTitle = inventory.getDisplayName();
        }

        if (!useFakeBlockEntity) { // gets around open noise for chest
            return Pair.of(type.create(player.nextContainerCounter(), player.getInventory()), defaultTitle);
        }

        return Pair.of(inventory.createMenu(player.nextContainerCounter(), player.getInventory(), player), defaultTitle);
    }
}
