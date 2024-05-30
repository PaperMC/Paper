package io.papermc.paper.world.flag;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.FeatureFlag;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftEntityTypes;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

public class PaperFeatureFlagProviderImpl implements FeatureFlagProvider {

    public static final BiMap<FeatureFlag, net.minecraft.world.flag.FeatureFlag> FLAGS = ImmutableBiMap.of(
        FeatureFlag.VANILLA, FeatureFlags.VANILLA,
        FeatureFlag.TRADE_REBALANCE, FeatureFlags.TRADE_REBALANCE,
        FeatureFlag.MINECART_IMPROVEMENTS, FeatureFlags.MINECART_IMPROVEMENTS,
        FeatureFlag.REDSTONE_EXPERIMENTS, FeatureFlags.REDSTONE_EXPERIMENTS
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
        } else {
            throw new IllegalArgumentException(dependant + " is not a valid feature dependant");
        }
    }
}
