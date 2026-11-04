package io.papermc.paper.block.stateprovider;

import io.papermc.paper.block.BlockPredicate;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface RuleBasedBlockStateProvider extends BlockStateProvider {

    /**
     * Fallback provider used when no rule matches, or null if absent.
     *
     * @return fallback provider or null
     */
    @Contract(pure = true)
    @Nullable BlockStateProvider fallback();

    /**
     * Ordered rules evaluated from first to last.
     *
     * @return immutable list of rules
     */
    @Contract(pure = true)
    @Unmodifiable List<Rule> rules();

    record Rule(BlockPredicate ifTrue, BlockStateProvider thenProvide) {
    }
}
