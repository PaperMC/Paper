package io.papermc.paper.interact;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the type of interaction that has been performed.
 */
@NullMarked
@ApiStatus.NonExtendable
public sealed interface InteractionType {

    /**
     * Represents an interaction that involves an item. The item may be {@link ItemStack#isEmpty()}.
     */
    @ApiStatus.NonExtendable
    sealed interface ItemUseInteraction extends InteractionType {

        /**
         * Returns a copy of the item in the hand that was used to perform the interaction, from before the interaction
         * was performed. The item may be {@link ItemStack#isEmpty()}.
         *
         * @return the item
         */
        ItemStack itemInHand();

    }

    /**
     * Represents an interaction that involves a block.
     */
    @ApiStatus.NonExtendable
    sealed interface BlockUseInteraction extends InteractionType {

        /**
         * Returns a copy of the block data of the block involved in the interaction, from before the interaction was
         * performed.
         *
         * @return the block data
         */
        BlockData interactedBlockData();

        /**
         * Returns the block that was interacted with.
         *
         * @return the block
         */
        Block interactedBlock();

    }

    /**
     * Represents an interaction that involves an entity.
     */
    @ApiStatus.NonExtendable
    sealed interface EntityUseInteraction extends InteractionType {

        /**
         * Returns the entity that was interacted with.
         *
         * @return the entity
         */
        Entity interactedEntity();

    }

    /**
     * Denotes an interaction with an item, without a target block or entity.
     */
    @ApiStatus.NonExtendable
    non-sealed interface UseItem extends ItemUseInteraction {

    }

    /**
     * Denotes an interaction with an item and a target block.
     */
    @ApiStatus.NonExtendable
    non-sealed interface UseItemOnBlock extends BlockUseInteraction, ItemUseInteraction {

    }

    /**
     * Denotes an interaction with a block, without an item involved.
     */
    @ApiStatus.NonExtendable
    non-sealed interface UseBlockWithoutItem extends BlockUseInteraction {

    }

    /**
     * Denotes an interaction with an item and a target entity.
     */
    @ApiStatus.NonExtendable
    non-sealed interface UseItemOnEntity extends EntityUseInteraction, ItemUseInteraction {

    }

    /**
     * Denotes an interaction with an item and a target entity, that also contains the location where the entity was
     * clicked.
     */
    @ApiStatus.NonExtendable
    non-sealed interface UseItemOnEntityAt extends EntityUseInteraction, ItemUseInteraction {

        Vector clickedPosition();

    }

}
