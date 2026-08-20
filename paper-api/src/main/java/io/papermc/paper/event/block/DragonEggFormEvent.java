package io.papermc.paper.event.block;

import org.bukkit.Material;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;

/**
 * Called when the {@link EnderDragon} is defeated (killed) in a {@link DragonBattle},
 * causing a {@link Material#DRAGON_EGG} (more formally: {@link #getNewState()})
 * to possibly appear depending on {@link #isCancelled()}.
 * <p>
 * <b>This event might be cancelled by default depending on
 * e.g. {@link DragonBattle#hasBeenPreviouslyKilled()} and server configuration.</b>
 */
public interface DragonEggFormEvent extends BlockFormEvent {

    /**
     * Gets the {@link DragonBattle} associated with this event.
     * Keep in mind that the {@link EnderDragon} is already dead
     * when this event is called.
     *
     * @return the dragon battle
     */
    DragonBattle getDragonBattle();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
