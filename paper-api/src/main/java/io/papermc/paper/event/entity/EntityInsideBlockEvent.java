package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity enters the hitbox of a block.
 * Only called for blocks that react when an entity is inside.
 * If cancelled, any action that would have resulted from that entity
 * being in the block will not happen (such as extinguishing an entity in a cauldron).
 * <p>
 * Blocks this is currently called for:
 * <ul>
 *     <li>Big dripleaf</li>
 *     <li>Bubble column</li>
 *     <li>Buttons</li>
 *     <li>Cactus</li>
 *     <li>Campfire</li>
 *     <li>Cauldron</li>
 *     <li>Crops</li>
 *     <li>End Gateway</li>
 *     <li>Ender Portal</li>
 *     <li>Eye blossom</li>
 *     <li>Fires</li>
 *     <li>Frogspawn</li>
 *     <li>Honey</li>
 *     <li>Hopper</li>
 *     <li>Detector rails</li>
 *     <li>Nether portals</li>
 *     <li>Pitcher crop</li>
 *     <li>Powdered snow</li>
 *     <li>Pressure plates</li>
 *     <li>Sweet berry bush</li>
 *     <li>Tripwire</li>
 *     <li>Waterlily</li>
 *     <li>Web</li>
 *     <li>Wither rose</li>
 * </ul>
 *
 * @since 1.16.5
 */
@NullMarked
public class EntityInsideBlockEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityInsideBlockEvent(final Entity entity, final Block block) {
        super(entity);
        this.block = block;
    }

    /**
     * Gets the block.
     *
     * @return the block
     */
    public Block getBlock() {
        return this.block;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
