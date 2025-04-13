package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.util.TriState;
import org.bukkit.block.BlockType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Controls the behavior of the item as a tool.
 * @see DataComponentTypes#TOOL
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Tool {

    @Contract(value = "-> new", pure = true)
    static Tool.Builder tool() {
        return ItemComponentTypesBridge.bridge().tool();
    }

    /**
     * Creates a mining rule that specifies how an item interacts with certain block types.
     *
     * <p>This method allows you to define a rule for a set of block types, optionally setting a custom mining speed
     * and determining whether the item should correct for drops when mining these blocks.</p>
     *
     * @param blocks          The set of block types this rule applies to.
     * @param speed           The custom mining speed multiplier for these blocks. If {@code null}, the default speed is used.
     * @param correctForDrops A {@link TriState} indicating how to handle item drops:
     *                        <ul>
     *                          <li>{@link TriState#TRUE} - Items will be dropped.</li>
     *                          <li>{@link TriState#FALSE} - Items will not be dropped.</li>
     *                          <li>{@link TriState#NOT_SET} - The default drop behavior is used.</li>
     *                        </ul>
     * @return A new {@link Rule} instance representing the mining rule.
     */
    static Rule rule(final RegistryKeySet<BlockType> blocks, final @Nullable Float speed, final TriState correctForDrops) {
        return ItemComponentTypesBridge.bridge().rule(blocks, speed, correctForDrops);
    }

    /**
     * Mining speed to use if no rules match and don't override mining speed.
     *
     * @return default mining speed
     */
    @Contract(pure = true)
    float defaultMiningSpeed();

    /**
     * Amount of durability to remove each time a block is mined with this tool.
     *
     * @return durability
     */
    @Contract(pure = true)
    @NonNegative int damagePerBlock();

    /**
     * List of rule entries.
     *
     * @return rules
     */
    @Contract(pure = true)
    @Unmodifiable List<Tool.Rule> rules();

    /**
     * Whether this tool can destroy blocks in creative mode.
     *
     * @return whether this tool can destroy blocks in creative mode
     */
    @Contract(pure = true)
    boolean canDestroyBlocksInCreative();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Rule {

        /**
         * Blocks to match.
         *
         * @return blocks
         */
        RegistryKeySet<BlockType> blocks();

        /**
         * Overrides the mining speed if present and matched.
         *
         * @return speed override
         */
        @Nullable Float speed();

        /**
         * Overrides whether this tool is considered 'correct' if present and matched.
         * <p>
         * {@code true} will cause the block to mine at its most efficient speed, and drop items if the targeted block requires that.
         *
         * @return a tri-state
         */
        TriState correctForDrops();
    }

    /**
     * Builder for {@link Tool}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<Tool> {

        /**
         * Controls the amount of durability to remove each time a block is mined with this tool.
         *
         * @param damage durability to remove
         * @return the builder for chaining
         * @see #damagePerBlock()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder damagePerBlock(@NonNegative int damage);

        /**
         * Controls mining speed to use if no rules match and don't override mining speed.
         *
         * @param miningSpeed mining speed
         * @return the builder for chaining
         * @see #defaultMiningSpeed()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder defaultMiningSpeed(float miningSpeed);

        /**
         * Adds a rule to the tool that controls the breaking speed / damage per block if matched.
         *
         * @param rule rule
         * @return the builder for chaining
         * @see #rules()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addRule(Rule rule);

        /**
         * Controls whether this tool can destroy blocks in creative mode.
         *
         * @param canDestroyBlocksInCreative whether this tool can destroy blocks in creative mode
         * @return the builder for chaining
         * @see #canDestroyBlocksInCreative()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder canDestroyBlocksInCreative(boolean canDestroyBlocksInCreative);

        /**
         * Adds rules to the tool that control the breaking speed / damage per block if matched.
         *
         * @param rules rules
         * @return the builder for chaining
         * @see #rules()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addRules(Collection<Rule> rules);
    }
}
