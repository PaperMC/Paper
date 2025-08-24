package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import org.bukkit.inventory.view.EnchantmentView;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftEnchantmentInventoryViewBuilder extends CraftLocationInventoryViewBuilder<EnchantmentView> {

    public CraftEnchantmentInventoryViewBuilder(final MenuType<?> handle) {
        super(handle, null);
    }

    @Override
    public LocationInventoryViewBuilder<EnchantmentView> copy() {
        final CraftEnchantmentInventoryViewBuilder builder = new CraftEnchantmentInventoryViewBuilder(super.handle);
        builder.title = super.title;
        builder.checkReachable = super.checkReachable;
        builder.bukkitLocation = super.bukkitLocation;
        return builder;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        setupLocation(); // prevents state from being corrupted since the builder mutates this
        if (super.position == null) {
            super.position = player.blockPosition();
            super.world = player.level();

            super.defaultTitle = new EnchantingTableBlockEntity(super.position, Blocks.ENCHANTING_TABLE.defaultBlockState()).getDisplayName();
            return new EnchantmentMenu(player.nextContainerCounter(), player.getInventory(), ContainerLevelAccess.create(super.world, super.position));
        }

        final BlockEntity entity = super.world.getBlockEntity(position);
        if (entity instanceof final EnchantingTableBlockEntity enchantingBlockEntity) {
            super.defaultTitle = enchantingBlockEntity.getDisplayName();
        } else {
            super.defaultTitle = new EnchantingTableBlockEntity(super.position, Blocks.ENCHANTING_TABLE.defaultBlockState()).getDisplayName();
        }

        return new EnchantmentMenu(player.nextContainerCounter(), player.getInventory(), ContainerLevelAccess.create(super.world, super.position));
    }
}
