package org.bukkit.craftbukkit.event.block;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftBlockPlaceEvent extends CraftBlockEvent implements BlockPlaceEvent {

    protected final Player player;
    protected final ItemStack itemInHand;
    protected final EquipmentSlot hand;
    protected final Block placedAgainst;
    protected final BlockState replacedState;
    protected boolean canBuild = true;

    protected boolean cancelled;

    public CraftBlockPlaceEvent(
        final Player player, final ItemStack itemInHand, final EquipmentSlot hand, final Block placedBlock, final Block placedAgainst, final BlockState replacedState
    ) {
        super(placedBlock);
        this.player = player;
        this.itemInHand = itemInHand;
        this.hand = hand;
        this.placedAgainst = placedAgainst;
        this.replacedState = replacedState;
    }

    public CraftBlockPlaceEvent(
        final Level level,
        final net.minecraft.world.entity.player.Player player,
        final InteractionHand hand,
        final BlockState replacedState,
        final BlockPos clickedPos
    ) {
        this(
            (Player) player.getBukkitEntity(),
            CraftItemStack.asBukkitCopy(player.getItemInHand(hand)),
            CraftEquipmentSlot.getHand(hand),
            replacedState.getBlock(),
            CraftBlock.at(level, clickedPos),
            replacedState
        );
        this.forEachPos(pos -> this.canBuild &= level.mayInteract(player, pos));
    }

    protected void forEachPos(final Consumer<BlockPos> output) {
        output.accept(((CraftBlockState) this.replacedState).getPosition());
    }

    @Override
    public Block getBlockAgainst() {
        return this.placedAgainst;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Block getBlockPlaced() {
        return this.block;
    }

    @Override
    public BlockState getBlockReplacedState() {
        return this.replacedState;
    }

    @Override
    public boolean canBuild() {
        return this.canBuild;
    }

    @Override
    public void setBuild(final boolean canBuild) {
        this.canBuild = canBuild;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockPlaceEvent.getHandlerList();
    }
}
