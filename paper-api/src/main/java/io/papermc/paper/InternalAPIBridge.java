package io.papermc.paper;

import com.destroystokyo.paper.SkinParts;
import com.destroystokyo.paper.util.VersionFetcher;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.entity.poi.PoiType;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import java.io.IOException;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.util.Services;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Static bridge to the server internals.
 * <p>
 * Any and all methods in here are *not* to be called by plugin developers, may change at any time and may generally
 * cause issues when called under unexpected circumstances.
 */
@ApiStatus.Internal
@NullMarked
public interface InternalAPIBridge {

    /**
     * Yields the instance of this API bridge by lazily requesting it from the java service loader API.
     *
     * @return the instance.
     */
    static InternalAPIBridge get() {
        class Holder {
            public static final InternalAPIBridge INSTANCE = Services.service(InternalAPIBridge.class).orElseThrow();
        }

        return Holder.INSTANCE;
    }

    @Deprecated(forRemoval = true, since = "1.21.5")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    Biome constructLegacyCustomBiome();

    CombatEntry createCombatEntry(LivingEntity entity, DamageSource damageSource, float damage);

    CombatEntry createCombatEntry(DamageSource damageSource, float damage, @Nullable FallLocationType fallLocationType, float fallDistance);

    /**
     * Causes this predicate to be considered restricted.
     * Applying this to a command node prevents this command from being executed from an
     * unattended context, such as click events.
     *
     * @param predicate wrapped predicate
     * @return wrapped predicate
     */
    Predicate<CommandSourceStack> restricted(Predicate<CommandSourceStack> predicate);

    ResolvableProfile defaultMannequinProfile();

    @Contract(value = "-> new", pure = true)
    SkinParts.Mutable allSkinParts();

    Component defaultMannequinDescription();

    <MODERN, LEGACY> GameRule<LEGACY> legacyGameRuleBridge(GameRule<MODERN> rule, Function<LEGACY, MODERN> fromLegacyToModern, Function<MODERN, LEGACY> toLegacyFromModern, Class<LEGACY> legacyClass);

    Set<Pose> validMannequinPoses();

    PoiType.Occupancy createOccupancy(String enumNameEntry);

    DamageSource.Builder createDamageSourceBuilder(DamageType damageType);

    DamageEffect getDamageEffect(String key);

    String getTranslationKey(EntityType entityType);

    SpawnCategory getSpawnCategory(EntityType entityType);

    /*
     * Called once by the version command on first use, then cached.
     */
    default VersionFetcher getVersionFetcher() {
        return new VersionFetcher.DummyVersionFetcher();
    }

    ItemStack deserializeItem(byte[] data);

    boolean hasDefaultEntityAttributes(NamespacedKey entityKey);

    Attributable getDefaultEntityAttributes(NamespacedKey entityKey);

    String getStatisticCriteriaKey(Statistic statistic);

    LifecycleEventManager<Plugin> createPluginLifecycleEventManager(JavaPlugin plugin, BooleanSupplier registrationCheck);

    ItemStack createEmptyStack();

    Component resolveWithContext(Component component, @Nullable CommandSender context, @Nullable Entity scoreboardSubject, boolean bypassPermissions) throws IOException;

    ComponentFlattener componentFlattener();
}
