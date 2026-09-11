package io.papermc.paper.block.stateprovider;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface BlockStateProvider {

    /**
     * Creates a simple provider that always returns the default state of the given block type.
     *
     * @param blockType block type to provide
     * @return a simple block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static SimpleBlockStateProvider simple(final BlockType blockType) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record SimpleProviderImpl(BlockType blockType) implements SimpleBlockStateProvider {
        }
        //</editor-fold>
        return new SimpleProviderImpl(blockType);
    }

    /**
     * Creates a provider that copies properties from the current block into the given target block type.
     *
     * @param blockType target block type
     * @return a copy-properties block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static CopyPropertiesBlockStateProvider copyPropertiesFrom(final BlockType blockType) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record CopyPropertiesProviderImpl(BlockType blockType) implements CopyPropertiesBlockStateProvider {
        }
        //</editor-fold>
        return new CopyPropertiesProviderImpl(blockType);
    }

    /**
     * Creates a provider that chooses a random block from the provided set.
     *
     * @param blocks candidate blocks
     * @return a random block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static RandomBlockStateProvider randomBlock(final RegistryKeySet<BlockType> blocks) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record RandomBlockProviderImpl(RegistryKeySet<BlockType> blocks) implements RandomBlockStateProvider {
        }
        //</editor-fold>
        return new RandomBlockProviderImpl(blocks);
    }

    /**
     * Creates a provider that applies a random rotation to the output of another provider.
     *
     * @param stateProvider source provider
     * @return a rotated block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static RotatedBlockStateProvider rotated(final BlockStateProvider stateProvider) {
        return rotated(stateProvider, null);
    }

    /**
     * Creates a provider that rotates the output of another provider with an optional forced direction.
     *
     * @param stateProvider source provider
     * @param direction     direction to force, or null for random direction
     * @return a rotated block state provider
     */
    @Contract(value = "_, _ -> new", pure = true)
    static RotatedBlockStateProvider rotated(final BlockStateProvider stateProvider, final @Nullable BlockFace direction) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record RotatedBlockProviderImpl(BlockStateProvider stateProvider, @Nullable BlockFace direction) implements RotatedBlockStateProvider {
        }
        //</editor-fold>
        return new RotatedBlockProviderImpl(stateProvider, direction);
    }

    /**
     * Creates a rule-based provider using the given rules and no fallback provider.
     *
     * @param rules rule list
     * @return a rule-based block state provider
     */
    @Contract(value = "_ -> new", pure = true)
    static RuleBasedBlockStateProvider ruleBased(final List<RuleBasedBlockStateProvider.Rule> rules) {
        return ruleBased(null, rules);
    }

    /**
     * Creates a rule-based provider using the given fallback and rules.
     *
     * @param fallback fallback provider when no rule matches
     * @param rules    rule list
     * @return a rule-based block state provider
     */
    @Contract(value = "_, _ -> new", pure = true)
    static RuleBasedBlockStateProvider ruleBased(final @Nullable BlockStateProvider fallback, final List<RuleBasedBlockStateProvider.Rule> rules) {
        //<editor-fold desc="implementations" defaultstate="collapsed">
        record RuleBasedProviderImpl(@Nullable BlockStateProvider fallback, @Unmodifiable List<RuleBasedBlockStateProvider.Rule> rules)
            implements RuleBasedBlockStateProvider {
            RuleBasedProviderImpl {
                rules = List.copyOf(rules);
            }
        }
        //</editor-fold>
        return new RuleBasedProviderImpl(fallback, rules);
    }

    /**
     * Creates a rule used by {@link #ruleBased(List)} and {@link #ruleBased(BlockStateProvider, List)}.
     *
     * @param ifTrue      predicate that must match
     * @param thenProvide provider to use when the predicate matches
     * @return an immutable rule
     */
    @Contract(value = "_, _ -> new", pure = true)
    static RuleBasedBlockStateProvider.Rule rule(final BlockPredicate ifTrue, final BlockStateProvider thenProvide) {
        return new RuleBasedBlockStateProvider.Rule(ifTrue, thenProvide);
    }
}
