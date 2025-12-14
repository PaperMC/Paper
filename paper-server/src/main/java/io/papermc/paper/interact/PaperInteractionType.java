package io.papermc.paper.interact;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public final class PaperInteractionType {

    public record UseItem(ItemStack itemInHand) implements InteractionType.UseItem {

        public UseItem(net.minecraft.world.item.ItemStack itemInHand) {
            this(itemInHand == null ? ItemStack.empty() : CraftItemStack.asBukkitCopy(itemInHand));
        }

    }

    public record UseItemOnBlock(BlockData interactedBlockData, Block interactedBlock, ItemStack itemInHand) implements InteractionType.UseItemOnBlock {

        public UseItemOnBlock(Level level, BlockPos blockPos, BlockState state, net.minecraft.world.item.ItemStack itemInHand) {
            this(CraftBlockData.fromData(state), CraftBlock.at(level, blockPos), itemInHand == null ? ItemStack.empty() : CraftItemStack.asBukkitCopy(itemInHand));
        }

    }

    public record UseBlockWithoutItem(BlockData interactedBlockData, Block interactedBlock) implements InteractionType.UseBlockWithoutItem {

        public UseBlockWithoutItem(Level level, BlockPos blockPos, BlockState state) {
            this(CraftBlockData.fromData(state), CraftBlock.at(level, blockPos));
        }

    }

    public record UseItemOnEntity(Entity interactedEntity, ItemStack itemInHand) implements InteractionType.UseItemOnEntity {

        public UseItemOnEntity(net.minecraft.world.entity.Entity interactedEntity, net.minecraft.world.item.ItemStack itemInHand) {
            this(interactedEntity.getBukkitEntity(), itemInHand == null ? ItemStack.empty() : CraftItemStack.asBukkitCopy(itemInHand));
        }

    }

}
