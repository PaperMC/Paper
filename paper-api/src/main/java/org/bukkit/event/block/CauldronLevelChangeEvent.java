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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CauldronLevelChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private final Entity entity;
    private final ChangeReason reason;
    private final BlockState newState;

    public CauldronLevelChangeEvent(@NotNull Block block, @Nullable Entity entity, @NotNull ChangeReason reason, @NotNull BlockState newBlock) {
        super(block);
        this.entity = entity;
        this.reason = reason;
        this.newState = newBlock;
    }

    /**
     * Get entity which did this. May be null.
     *
     * @return acting entity
     */
    @Nullable
    public Entity getEntity() {
        return entity;
    }

    @NotNull
    public ChangeReason getReason() {
        return reason;
    }

    /**
     * Gets the new state of the cauldron.
     *
     * @return The block state of the block that will be changed
     */
    @NotNull
    public BlockState getNewState() {
        return newState;
    }

    /**
     * Gets the old level of the cauldron.
     *
     * @return old level
     * @see #getBlock()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated
    public int getOldLevel() {
        BlockData oldBlock = getBlock().getBlockData();
        return (oldBlock instanceof Levelled) ? ((Levelled) oldBlock).getLevel() : ((oldBlock.getMaterial() == Material.CAULDRON) ? 0 : 3);
    }

    /**
     * Gets the new level of the cauldron.
     *
     * @return new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated
    public int getNewLevel() {
        BlockData newBlock = newState.getBlockData();
        return (newBlock instanceof Levelled) ? ((Levelled) newBlock).getLevel() : ((newBlock.getMaterial() == Material.CAULDRON) ? 0 : 3);
    }

    /**
     * Sets the new level of the cauldron.
     *
     * @param newLevel new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated
    public void setNewLevel(int newLevel) {
        Preconditions.checkArgument(0 <= newLevel && newLevel <= 3, "Cauldron level out of bounds 0 <= %s <= 3", newLevel);
        if (newLevel == 0) {
            newState.setType(Material.CAULDRON);
        } else if (newState.getBlockData() instanceof Levelled) {
            ((Levelled) newState.getBlockData()).setLevel(newLevel);
        } else {
            // Error, non-levellable block
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
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
         * Filling due to natural fluid sources, eg rain or dripstone.
         */
        NATURAL_FILL,
        /**
         * Unknown.
         */
        UNKNOWN
    }
}
