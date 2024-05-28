package org.bukkit.inventory.meta.components;

import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a component which can turn any item into a tool.
 */
@ApiStatus.Experimental
public interface ToolComponent extends ConfigurationSerializable {

    /**
     * Get the default mining speed of this tool. This value is used by the tool
     * if no rule explicitly overrides it. 1.0 is standard mining speed.
     *
     * @return the default mining speed
     * @see ToolRule#getSpeed()
     */
    float getDefaultMiningSpeed();

    /**
     * Set the default mining speed of this tool. This value is used by the tool
     * if no rule explicitly overrides it. 1.0 is standard mining speed.
     *
     * @param speed the speed to set
     */
    void setDefaultMiningSpeed(float speed);

    /**
     * Get the amount of durability to be removed from the tool each time a
     * block is broken.
     *
     * @return the damage per block
     */
    int getDamagePerBlock();

    /**
     * Set the amount of durability to be removed from the tool each time a
     * block is broken.
     *
     * @param damage the damage to set. Must be 0 or a positive integer
     */
    void setDamagePerBlock(int damage);

    /**
     * Get the list of {@link ToolRule ToolRules} that apply to this tool.
     *
     * @return all tool rules. The mutability of the returned list cannot be
     * guaranteed, but its contents are mutable and can have their values
     * changed
     */
    @NotNull
    List<ToolRule> getRules();

    /**
     * Set the list of {@link ToolRule ToolRules} to apply to this tool. This
     * will remove any existing tool rules.
     *
     * @param rules the rules to set
     */
    void setRules(@NotNull List<ToolRule> rules);

    /**
     * Add a new rule to this tool component, which provides further information
     * about a specific block type.
     *
     * @param block the block type to which the rule applies
     * @param speed the mining speed to use when mining the block, or null to
     * use the default mining speed
     * @param correctForDrops whether or not this tool, when mining the block,
     * is considered the optimal tool for the block and will drop its items when
     * broken, or null to use the default tool checking behavior defined by
     * Minecraft
     * @return the {@link ToolRule} instance that was added to this tool
     */
    @NotNull
    ToolRule addRule(@NotNull Material block, @Nullable Float speed, @Nullable Boolean correctForDrops);

    /**
     * Add a new rule to this tool component, which provides further information
     * about a collection of block types.
     *
     * @param blocks the block types to which the rule applies
     * @param speed the mining speed to use when mining one of the blocks, or
     * null to use the default mining speed
     * @param correctForDrops whether or not this tool, when mining one of the
     * blocks, is considered the optimal tool for the block and will drop its
     * items when broken, or null to use the default tool checking behavior
     * defined by Minecraft
     * @return the {@link ToolRule} instance that was added to this tool
     */
    @NotNull
    ToolRule addRule(@NotNull Collection<Material> blocks, @Nullable Float speed, @Nullable Boolean correctForDrops);

    /**
     * Add a new rule to this tool component, which provides further information
     * about a collection of block types represented by a block {@link Tag}.
     *
     * @param tag the block tag containing block types to which the rule
     * applies.
     * @param speed the mining speed to use when mining one of the blocks, or
     * null to use the default mining speed
     * @param correctForDrops whether or not this tool, when mining one of the
     * blocks, is considered the optimal tool for the block and will drop its
     * items when broken, or null to use the default tool checking behavior
     * defined by Minecraft
     * @return the {@link ToolRule} instance that was added to this tool
     * @throws IllegalArgumentException if the passed {@code tag} is not a block
     * tag
     */
    @NotNull
    ToolRule addRule(@NotNull Tag<Material> tag, @Nullable Float speed, @Nullable Boolean correctForDrops);

    /**
     * Remove the given {@link ToolRule} from this tool.
     *
     * @param rule the rule to remove
     * @return true if the rule was removed, false if this component did not
     * contain a matching rule
     */
    boolean removeRule(@NotNull ToolRule rule);

    /**
     * A rule governing use of this tool and overriding attributes per-block.
     */
    public interface ToolRule extends ConfigurationSerializable {

        /**
         * Get a collection of the block types to which this tool rule applies.
         *
         * @return the blocks
         */
        @NotNull
        Collection<Material> getBlocks();

        /**
         * Set the block type to which this rule applies.
         *
         * @param block the block type
         */
        void setBlocks(@NotNull Material block);

        /**
         * Set the block types to which this rule applies.
         *
         * @param blocks the block types
         */
        void setBlocks(@NotNull Collection<Material> blocks);

        /**
         * Set the block types (represented as a block {@link Tag}) to which
         * this rule applies.
         *
         * @param tag the block tag
         * @throws IllegalArgumentException if the passed {@code tag} is not a
         * block tag
         */
        void setBlocks(@NotNull Tag<Material> tag);

        /**
         * Get the mining speed of this rule. If non-null, this speed value is
         * used in lieu of the default speed value of the tool. 1.0 is standard
         * mining speed.
         *
         * @return the mining speed, or null if the default speed is used
         */
        @Nullable
        Float getSpeed();

        /**
         * Set the mining speed of this rule. 1.0 is standard mining speed.
         *
         * @param speed the mining speed, or null to use the default speed
         */
        void setSpeed(@Nullable Float speed);

        /**
         * Get whether or not this rule is considered the optimal tool for the
         * blocks listed by this rule and will drop items. If non-null, this
         * value is used in lieu of the default tool checking behavior defined
         * by Minecraft.
         *
         * @return true if correct for drops, false otherwise, or null to
         * fallback to vanilla tool checking behavior
         */
        @Nullable
        Boolean isCorrectForDrops();

        /**
         * Set whether or not this rule is considered the optimal tool for the
         * blocks listed by this rule and will drop items.
         *
         * @param correct whether or not this rule is correct for drops, or null
         * to fallback to vanilla tool checking behavior
         */
        void setCorrectForDrops(@Nullable Boolean correct);
    }
}
