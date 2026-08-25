package com.destroystokyo.paper.event.block;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.jspecify.annotations.Nullable;

/**
 * Called when an anvil is damaged from being used
 */
public interface AnvilDamagedEvent extends InventoryEvent, Cancellable {

    @Override
    AnvilInventory getInventory();

    /**
     * Gets the new state of damage on the anvil
     *
     * @return Damage state
     */
    DamageState getDamageState();

    /**
     * Sets the new state of damage on the anvil
     *
     * @param damageState Damage state
     */
    void setDamageState(DamageState damageState);

    /**
     * Gets if anvil is breaking on this use
     *
     * @return {@code true} if breaking
     */
    boolean isBreaking();

    /**
     * Sets if anvil is breaking on this use
     *
     * @param breaking {@code true} if breaking
     */
    void setBreaking(boolean breaking);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Represents the amount of damage on an anvil block
     */
    enum DamageState {
        FULL(Material.ANVIL),
        CHIPPED(Material.CHIPPED_ANVIL),
        DAMAGED(Material.DAMAGED_ANVIL),
        BROKEN(Material.AIR);

        private final Material material;

        DamageState(final Material material) {
            this.material = material;
        }

        /**
         * Get block material of this state
         *
         * @return Material
         */
        public Material getMaterial() {
            return this.material;
        }

        /**
         * Get damaged state by block data
         *
         * @param blockData Block data
         * @return DamageState
         * @throws IllegalArgumentException If non anvil block data is given
         */
        public static DamageState getState(final @Nullable BlockData blockData) {
            return blockData == null ? BROKEN : getState(blockData.getMaterial());
        }

        /**
         * Get damaged state by block material
         *
         * @param material Block material
         * @return DamageState
         * @throws IllegalArgumentException If non anvil material is given
         */
        public static DamageState getState(final @Nullable Material material) {
            if (material == null) {
                return BROKEN;
            }
            for (final DamageState state : values()) {
                if (state.getMaterial() == material) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Material is not an anvil state");
        }
    }
}
