package io.papermc.paper;

import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.Nullable;

public final class StaticUnsafeValues {
    private static @Nullable UnsafeValuesProvider provider;

    public static void setProvider(UnsafeValuesProvider provider) {
        if (provider != null) {
            StaticUnsafeValues.provider = provider;
        }
    }

    public static UnsafeValuesProvider getProvider() {
        if (StaticUnsafeValues.provider != null) return StaticUnsafeValues.provider;
        else throw new RuntimeException();
    }

    public interface UnsafeValuesProvider {
        DamageEffect getDamageEffect(String key);
    }
}
