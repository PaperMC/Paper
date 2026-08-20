package org.bukkit.craftbukkit.event.block;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftBlockMultiPlaceEvent extends CraftBlockPlaceEvent implements BlockMultiPlaceEvent {

    private final List<BlockState> replacedStates;

    public CraftBlockMultiPlaceEvent(final List<BlockState> replacedStates, final Block clicked, final ItemStack itemInHand, final Player player, final boolean canBuild, final EquipmentSlot hand) {
        super(replacedStates.getFirst().getBlock(), replacedStates.getFirst(), clicked, itemInHand, player, canBuild, hand);
        this.replacedStates = ImmutableList.copyOf(replacedStates);
    }

    @Override
    public List<BlockState> getReplacedBlockStates() {
        return this.replacedStates;
    }
}
