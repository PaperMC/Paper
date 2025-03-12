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

public class CraftEnchantmentInventoryViewBuilder extends CraftAbstractLocationInventoryViewBuilder<EnchantmentView> {

    public CraftEnchantmentInventoryViewBuilder(final MenuType<?> handle) {
        super(handle);
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (this.world == null) {
            this.world = player.level();
        }

        if (this.position == null) {
            this.position = player.blockPosition();
            super.defaultTitle = new EnchantingTableBlockEntity(this.position, Blocks.ENCHANTING_TABLE.defaultBlockState()).getDisplayName();
            return new EnchantmentMenu(player.nextContainerCounter(), player.getInventory(), ContainerLevelAccess.create(this.world, this.position));
        }

        final BlockEntity entity = this.world.getBlockEntity(position);
        if (entity instanceof final EnchantingTableBlockEntity enchantingBlockEntity) {
            super.defaultTitle = enchantingBlockEntity.getDisplayName();
        } else {
            super.defaultTitle = new EnchantingTableBlockEntity(this.position, Blocks.ENCHANTING_TABLE.defaultBlockState()).getDisplayName();
        }

        return new EnchantmentMenu(player.nextContainerCounter(), player.getInventory(), ContainerLevelAccess.create(this.world, this.position));
    }
}
