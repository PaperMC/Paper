package io.papermc.generator.utils.experimental;

import net.minecraft.world.flag.FeatureFlags;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FlagHolders {

    public static final SingleFlagHolder TRADE_REBALANCE = SingleFlagHolder.fromValue(FeatureFlags.TRADE_REBALANCE);
    public static final SingleFlagHolder REDSTONE_EXPERIMENTS = SingleFlagHolder.fromValue(FeatureFlags.REDSTONE_EXPERIMENTS);
    public static final SingleFlagHolder MINECART_IMPROVEMENTS = SingleFlagHolder.fromValue(FeatureFlags.MINECART_IMPROVEMENTS);
}
