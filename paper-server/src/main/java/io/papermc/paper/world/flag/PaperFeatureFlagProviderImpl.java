package io.papermc.paper.world.flag;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import org.bukkit.FeatureFlag;
import org.bukkit.GameRule;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

public class PaperFeatureFlagProviderImpl implements FeatureFlagProvider {

    public static final BiMap<FeatureFlag, net.minecraft.world.flag.FeatureFlag> FLAGS = ImmutableBiMap.of(
        // Start generate - PaperFeatureFlagProviderImpl#FLAGS
        // @GeneratedFrom 1.21.6-pre1
        FeatureFlag.MINECART_IMPROVEMENTS, FeatureFlags.MINECART_IMPROVEMENTS,
        FeatureFlag.REDSTONE_EXPERIMENTS, FeatureFlags.REDSTONE_EXPERIMENTS,
        FeatureFlag.TRADE_REBALANCE, FeatureFlags.TRADE_REBALANCE,
        FeatureFlag.VANILLA, FeatureFlags.VANILLA
        // End generate - PaperFeatureFlagProviderImpl#FLAGS
    );

    @Override
    public Set<FeatureFlag> requiredFeatures(final FeatureDependant dependant) {
        final FeatureFlagSet requiredFeatures = getFeatureElement(dependant).requiredFeatures();
        return fromNms(requiredFeatures);
    }

    public static Set<FeatureFlag> fromNms(final FeatureFlagSet flagSet) {
        final Set<FeatureFlag> flags = new HashSet<>();
        for (final net.minecraft.world.flag.FeatureFlag nmsFlag : FeatureFlags.REGISTRY.names.values()) {
            if (flagSet.contains(nmsFlag)) {
                flags.add(FLAGS.inverse().get(nmsFlag));
            }
        }
        return Collections.unmodifiableSet(flags);
    }

    static FeatureElement getFeatureElement(final FeatureDependant dependant) {
        if (dependant instanceof final EntityType entityType) {
            // TODO remove when EntityType is server-backed
            return CraftEntityType.bukkitToMinecraft(entityType);
        } else if (dependant instanceof final PotionType potionType) {
            return CraftPotionType.bukkitToMinecraft(potionType);
        } else if (dependant instanceof final GameRule<?> gameRule) {
            return getGameRuleType(gameRule.getName()).asFeatureElement();
        } else {
            throw new IllegalArgumentException(dependant + " is not a valid feature dependant");
        }
    }

    private static GameRules.Type<?> getGameRuleType(final String name) {
        for (final Map.Entry<GameRules.Key<?>, GameRules.Type<?>> gameRules : GameRules.GAME_RULE_TYPES.entrySet()) {
            if (gameRules.getKey().getId().equals(name)) {
                return gameRules.getValue();
            }
        }
        return null;
    }
}
