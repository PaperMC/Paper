package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a fluid type.
 */
public interface Fluid extends OldEnum<Fluid>, Keyed {

    // Start generate - Fluid
    // @GeneratedFrom 1.21.6-pre1
    Fluid EMPTY = getFluid("empty");

    Fluid FLOWING_LAVA = getFluid("flowing_lava");

    Fluid FLOWING_WATER = getFluid("flowing_water");

    Fluid LAVA = getFluid("lava");

    Fluid WATER = getFluid("water");
    // End generate - Fluid

    @NotNull
    private static Fluid getFluid(@NotNull String key) {
        return Registry.FLUID.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * @param name of the fluid.
     * @return the fluid with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Fluid valueOf(@NotNull String name) {
        Fluid fluid = Bukkit.getUnsafe().get(RegistryKey.FLUID, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(fluid != null, "No fluid found with the name %s", name);
        return fluid;
    }

    /**
     * @return an array of all known fluids.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Fluid[] values() {
        return Lists.newArrayList(Registry.FLUID).toArray(new Fluid[0]);
    }
}
