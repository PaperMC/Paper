package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CauldronLevelChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;
    private final ChangeReason reason;
    private final BlockState newState;

    private boolean cancelled;

    @ApiStatus.Internal
    public CauldronLevelChangeEvent(@NotNull Block block, @Nullable Entity entity, @NotNull ChangeReason reason, @NotNull BlockState newBlock) {
        super(block);
        this.entity = entity;
        this.reason = reason;
        this.newState = newBlock;
    }

    /**
     * Get entity which did this. May be {@code null}.
     *
     * @return acting entity
     */
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    @NotNull
    public ChangeReason getReason() {
        return this.reason;
    }

    /**
     * Gets the new state of the cauldron.
     *
     * @return The block state of the block that will be changed
     */
    @NotNull
    public BlockState getNewState() {
        return this.newState;
    }

    /**
     * Gets the old level of the cauldron.
     *
     * @return old level
     * @see #getBlock()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    public int getOldLevel() {
        BlockData oldBlock = this.getBlock().getBlockData();
        return (oldBlock instanceof Levelled) ? ((Levelled) oldBlock).getLevel() : ((oldBlock.getMaterial() == Material.CAULDRON) ? 0 : 3);
    }

    /**
     * Gets the new level of the cauldron.
     *
     * @return new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    public int getNewLevel() {
        BlockData newBlock = this.newState.getBlockData();
        return (newBlock instanceof Levelled) ? ((Levelled) newBlock).getLevel() : ((newBlock.getMaterial() == Material.CAULDRON) ? 0 : 3);
    }

    /**
     * Sets the new level of the cauldron.
     *
     * @param newLevel new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    public void setNewLevel(int newLevel) {
        Preconditions.checkArgument(0 <= newLevel && newLevel <= 3, "Cauldron level out of bounds 0 <= %s <= 3", newLevel);
        if (newLevel == 0) {
            this.newState.setType(Material.CAULDRON);
        } else if (this.newState.getBlockData() instanceof Levelled) {
            ((Levelled) this.newState.getBlockData()).setLevel(newLevel);
        } else {
            // Error, non-levellable block
        }
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum ChangeReason {
        /**
         * Player emptying the cauldron by filling their bucket.
         */
        BUCKET_FILL,
        /**
         * Player filling the cauldron by emptying their bucket.
         */
        BUCKET_EMPTY,
        /**
         * Player emptying the cauldron by filling their bottle.
         */
        BOTTLE_FILL,
        /**
         * Player filling the cauldron by emptying their bottle.
         */
        BOTTLE_EMPTY,
        /**
         * Player cleaning their banner.
         */
        BANNER_WASH,
        /**
         * Player cleaning their armor.
         */
        ARMOR_WASH,
        /**
         * Player cleaning a shulker box.
         */
        SHULKER_WASH,
        /**
         * Entity being extinguished.
         */
        EXTINGUISH,
        /**
         * Evaporating due to biome dryness.
         */
        EVAPORATE,
        /**
         * Filling due to natural fluid sources, e.g. rain or dripstone.
         */
        NATURAL_FILL,
        /**
         * Unknown.
         */
        UNKNOWN
    }
}
