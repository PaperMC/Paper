package io.papermc.paper.event.world;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an explosion happens on the server.
 * This includes explosions that are visual only or will not affect blocks due to set gamerules.
 */
@NullMarked
public class ExplodeEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DamageSource damageSource;
    private final ExplodeEventSource eventSource;
    private final Location location;
    private final List<Block> blocks;
    private final boolean spreadFire;
    private final ExplosionResult result;

    private float yield;
    private boolean cancelled;

    @ApiStatus.Internal
    public ExplodeEvent(
        final Location location,
        final DamageSource damageSource,
        final ExplodeEventSource eventSource,
        final List<Block> blocks,
        final float yield,
        final boolean spreadFire,
        final ExplosionResult result
    ) {
        super(location.getWorld());
        this.damageSource = damageSource;
        this.eventSource = eventSource;
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.spreadFire = spreadFire;
        this.result = result;
    }

    /**
     * Gets the source of damage.
     *
     * @return a damage source detailing the source of the damage from the explosion
     * @apiNote by default, it's the explosion but data-packs using custom explode effect can set other type of damages
     */
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    /**
     * Returns the location where the explosion happened.
     *
     * @return the location of the explosion
     */
    public Location getLocation() {
        return this.location.clone();
    }

    /**
     * Yields the source of this explode event.
     * <p>
     * The returned interface is sealed and is expected to either be pattern matched or checked via instance of for
     * further consumption.
     * Retrieving and processing only entities that caused the explosion could hence be implemented as follows:
     * <pre>{@code
     * if (!(event.getSource() instanceof final ExplodeEventSource.EntitySource entitySource)) return;
     * final Entity entity = entitySource.entity();
     * ....
     * }</pre>
     * <p>
     * Alternatively, pattern matching may be used to process different types of sources easily:
     * <pre>
     * {@code
     * switch (event.getSource()) {
     *     case final ExplodeEventSource.BlockSource blockSource -> {
     *         final BlockState blockState = blockSource.blockState();
     *     }
     *     case final ExplodeEventSource.EnchantmentSource enchantmentSource -> {
     *         final ItemStack itemStack = enchantmentSource.itemStack();
     *     }
     *     case final ExplodeEventSource.EntitySource entitySource -> {
     *         final Entity entity = entitySource.entity();
     *     }
     *     case final ExplodeEventSource.UnknownSource ignored -> {
     *     }
     * }
     * }
     * </pre>
     *
     * @return the source of this explosion.
     */
    public ExplodeEventSource getSource() {
        return this.eventSource;
    }

    /**
     * Returns a mutable list of blocks that would have been caught in the explosion.
     *
     * @return all blocks caught in the explosion
     * @apiNote the behaviors with these blocks depends on {@link #getExplosionResult()}
     */
    @ApiStatus.Experimental
    public List<Block> getAffectedBlocks() {
        return this.blocks;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion.
     *
     * @return the yield percentage
     * @apiNote this behavior depends on {@link #getExplosionResult()}
     */
    public float getYield() {
        return this.yield;
    }

    /**
     * Sets the percentage of blocks to drop from this explosion.
     *
     * @param yield the new yield percentage
     */
    public void setYield(float yield) {
        this.yield = yield;
    }

    /**
     * Checks if this explosion should spread fire
     * on the ground.
     *
     * @return if this explosion should spread fire
     */
    public boolean shouldSpreadFire() {
        return this.spreadFire;
    }

    /**
     * Returns the result of the explosion if it is not cancelled.
     *
     * @return the result of the explosion
     */
    public ExplosionResult getExplosionResult() {
        return this.result;
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
