package io.papermc.generator.utils.experimental;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.MinecraftExperimental;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FlagHolders {

    public static final SingleFlagHolder TRADE_REBALANCE = SingleFlagHolder.fromValue(FeatureFlags.TRADE_REBALANCE);
    public static final SingleFlagHolder REDSTONE_EXPERIMENTS = SingleFlagHolder.fromValue(FeatureFlags.REDSTONE_EXPERIMENTS);
    public static final SingleFlagHolder MINECART_IMPROVEMENTS = SingleFlagHolder.fromValue(FeatureFlags.MINECART_IMPROVEMENTS);

    static final Map<FeatureFlag, MinecraftExperimental.Requires> ANNOTATION_EQUIVALENT = Util.make(new HashMap<SingleFlagHolder, MinecraftExperimental.Requires>(), map -> {
        map.put(TRADE_REBALANCE, MinecraftExperimental.Requires.TRADE_REBALANCE);
        map.put(REDSTONE_EXPERIMENTS, MinecraftExperimental.Requires.REDSTONE_EXPERIMENTS);
        map.put(MINECART_IMPROVEMENTS, MinecraftExperimental.Requires.MINECART_IMPROVEMENTS);
    }).entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().flag(), Map.Entry::getValue));
}
