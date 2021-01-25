package io.papermc.paper.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the {@link EnderDragon} is defeated (killed) in a {@link DragonBattle},
 * causing a {@link Material#DRAGON_EGG} (more formally: {@link #getNewState()})
 * to possibly appear depending on {@link #isCancelled()}.
 * <p>
 * <b>This event might be cancelled by default depending on
 * e.g. {@link DragonBattle#hasBeenPreviouslyKilled()} and server configuration.</b>
 */
@NullMarked
public class DragonEggFormEvent extends BlockFormEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DragonBattle dragonBattle;

    @ApiStatus.Internal
    public DragonEggFormEvent(final Block block, final BlockState newState, final DragonBattle dragonBattle) {
        super(block, newState);
        this.dragonBattle = dragonBattle;
    }

    /**
     * Gets the {@link DragonBattle} associated with this event.
     * Keep in mind that the {@link EnderDragon} is already dead
     * when this event is called.
     *
     * @return the dragon battle
     */
    public DragonBattle getDragonBattle() {
        return this.dragonBattle;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
