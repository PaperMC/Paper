package io.papermc.paper.event.world;

import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents the source of an explosion that triggered a {@link ExplodeEvent}.
 */
@NullMarked
public sealed interface ExplodeEventSource permits ExplodeEventSource.BlockSource, ExplodeEventSource.EnchantmentSource, ExplodeEventSource.EntitySource, ExplodeEventSource.UnknownSource {

    /**
     * A block explode event source.
     * <p>
     * Yielded by {@link ExplodeEvent#getSource()} if a block exploded, like {@link BlockType#RESPAWN_ANCHOR} does
     * outside the nether.
     */
    non-sealed interface BlockSource extends ExplodeEventSource {

        /**
         * Yields the block state of the block prior to exploding.
         * Useful as {@link ExplodeEvent#getLocation()}'s block is already exploded to air.
         *
         * @return the block state of the block prior to exploding.
         */
        BlockState blockState();
    }

    /**
     * An entity explode event source.
     * <p>
     * Yielded by {@link ExplodeEvent#getSource()} if an entity was the cause of an explosion, such as {@link TNTPrimed}.
     */
    non-sealed interface EntitySource extends ExplodeEventSource {

        /**
         * Yields the entity that exploded.
         *
         * @return the entity instance.
         */
        Entity entity();
    }

    /**
     * An enchantment explode event source.
     * <p>
     * Yielded by {@link ExplodeEvent#getSource()} if the explosion was caused by an enchantment effect, like
     * {@link Enchantment#WIND_BURST}.
     */
    non-sealed interface EnchantmentSource extends ExplodeEventSource {

        /**
         * The itemstack that was enchanted to cause this explosion.
         *
         * @return a copy of the itemstack.
         */
        @Contract("-> new")
        ItemStack itemStack();

        /**
         * The owner of the enchanted item.
         * The owner is understood as the further entity related to the item causing this explosion, such as the wielder
         * of a weapon or the shooter of an arrow.
         *
         * @return the item owner.
         */
        @Nullable
        LivingEntity itemOwner();

        /**
         * The entity affected by this explosion.
         * Other than the {@link #itemOwner()}, this may be the shot arrow, the victim, or the
         * attacker (depending on the enchantment definition).
         *
         * @return the affected entity.
         */
        Entity affected();
    }

    /**
     * The unknown explode event source.
     * Yielded if the api cannot or does not expose any further information about the source.
     */
    non-sealed interface UnknownSource extends ExplodeEventSource {
        /**
         * The static instance of the unknown source.
         */
        UnknownSource INSTANCE = new UnknownSource() {
        };
    }
}
