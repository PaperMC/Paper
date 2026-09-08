package org.bukkit.craftbukkit.event.block;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public class CraftBlockMultiPlaceEvent extends CraftBlockPlaceEvent implements BlockMultiPlaceEvent {

    private final List<BlockState> replacedStates;

    public CraftBlockMultiPlaceEvent(
        final Player player, final ItemStack itemInHand, final EquipmentSlot hand, final Block clicked, final List<BlockState> replacedStates
    ) {
        this.replacedStates = Collections.unmodifiableList(replacedStates);
        super(player, itemInHand, hand, clicked, replacedStates.getFirst().getBlock(), replacedStates.getFirst());
    }

    public CraftBlockMultiPlaceEvent(
        final Level level,
        final net.minecraft.world.entity.player.Player player,
        final InteractionHand hand,
        final List<BlockState> replacedStates,
        final BlockPos clickedPos
    ) {
        this.replacedStates = Collections.unmodifiableList(replacedStates);
        super(level, player, hand, replacedStates.getFirst(), clickedPos);
    }

    @Override
    public @Unmodifiable List<BlockState> getReplacedBlockStates() {
        return this.replacedStates;
    }

    @Override
    protected void forEachPos(final Consumer<BlockPos> output) {
        this.replacedStates.forEach(state -> {
            output.accept(((CraftBlockState) state).getPosition());
        });
    }
}
