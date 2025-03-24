package org.bukkit.event.block;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a single block placement action of a player triggers the
 * creation of multiple blocks(e.g. placing a bed block). The block returned
 * by {@link #getBlockPlaced()} and its related methods is the block where
 * the placed block would exist if the placement only affected a single
 * block.
 */
public class BlockMultiPlaceEvent extends BlockPlaceEvent {
    private final List<BlockState> states;

    @Deprecated(forRemoval = true)
    public BlockMultiPlaceEvent(@NotNull List<BlockState> states, @NotNull Block clicked, @NotNull ItemStack itemInHand, @NotNull Player thePlayer, boolean canBuild) {
        // Paper start - add hand to BlockMultiPlaceEvent
        this(states, clicked, itemInHand, thePlayer, canBuild, org.bukkit.inventory.EquipmentSlot.HAND);
    }

    @ApiStatus.Internal
    public BlockMultiPlaceEvent(@NotNull List<BlockState> states, @NotNull Block clicked, @NotNull ItemStack itemInHand, @NotNull Player thePlayer, boolean canBuild, @NotNull org.bukkit.inventory.EquipmentSlot hand) {
        super(states.get(0).getBlock(), states.get(0), clicked, itemInHand, thePlayer, canBuild, hand);
        this.states = ImmutableList.copyOf(states);
        // Paper end
    }

    /**
     * Gets a list of blockstates for all blocks which were replaced by the
     * placement of the new blocks. Most of these blocks will just have a
     * Material type of AIR.
     *
     * @return immutable list of replaced BlockStates
     */
    @NotNull
    public List<BlockState> getReplacedBlockStates() {
        return states;
    }
}
