package io.papermc.paper;

import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import net.kyori.adventure.util.Services;
import org.bukkit.block.Biome;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
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

    /**
     * Creates a damage effect instance for the passed key.
     *
     * @param key the string key.
     * @return the damage effect.
     */
    DamageEffect getDamageEffect(String key);

    /**
     * Constructs the legacy custom biome instance for the biome enum.
     *
     * @return the created biome.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    Biome constructLegacyCustomBiome();
     
    /**
     * Creates a new combat entry.
     * <p>
     * The fall location and fall distance will be calculated from the entity's current state.
     *
     * @param entity entity
     * @param damageSource damage source
     * @param damage damage amount
     * @return new combat entry
     */
    CombatEntry createCombatEntry(LivingEntity entity, DamageSource damageSource, float damage);

    /**
     * Creates a new combat entry
     *
     * @param damageSource damage source
     * @param damage damage amount
     * @param fallLocationType fall location type
     * @param fallDistance fall distance
     * @return combat entry
     */
    CombatEntry createCombatEntry(DamageSource damageSource, float damage, @Nullable FallLocationType fallLocationType, float fallDistance);
}

