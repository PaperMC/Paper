package io.papermc.paper.block.fluid;

import io.papermc.paper.annotation.MinecraftVersionDependent;
import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.Fluid;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@MinecraftVersionDependent
@ApiStatus.NonExtendable
public interface FluidPredicate extends BlockPredicate {

    static MatchesBlocks matchesFluids(final RegistryKeySet<Fluid> fluids) {
        record MatchesBlocksPredicateImpl(RegistryKeySet<Fluid> fluids) implements MatchesBlocks {
        }
        return new MatchesBlocksPredicateImpl(fluids);
    }

    static MatchesDirection matchesDirection(final BlockFace direction, final RegistryKeySet<Fluid> fluids) {
        record MatchesDirectionPredicateImpl(RegistryKeySet<Fluid> fluids, BlockFace direction)
            implements MatchesDirection {
        }
        return new MatchesDirectionPredicateImpl(fluids, direction);
    }

    @ApiStatus.NonExtendable
    interface MatchesBlocks extends FluidPredicate {

        RegistryKeySet<Fluid> fluids();
    }

    @ApiStatus.NonExtendable
    interface MatchesDirection extends MatchesBlocks {

        BlockFace direction();
    }
}
