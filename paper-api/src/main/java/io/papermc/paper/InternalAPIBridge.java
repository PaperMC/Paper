package io.papermc.paper;

import net.kyori.adventure.util.Services;
import org.bukkit.damage.DamageEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

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
}

