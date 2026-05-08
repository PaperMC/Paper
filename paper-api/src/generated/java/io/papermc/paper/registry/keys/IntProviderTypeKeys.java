package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.math.provider.IntProviderType;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#INT_PROVIDER_TYPE}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedFrom("1.21.6")
public final class IntProviderTypeKeys {
    /**
     * {@code minecraft:biased_to_bottom}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> BIASED_TO_BOTTOM = create(key("biased_to_bottom"));

    /**
     * {@code minecraft:clamped}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> CLAMPED = create(key("clamped"));

    /**
     * {@code minecraft:clamped_normal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> CLAMPED_NORMAL = create(key("clamped_normal"));

    /**
     * {@code minecraft:constant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> CONSTANT = create(key("constant"));

    /**
     * {@code minecraft:uniform}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> UNIFORM = create(key("uniform"));

    /**
     * {@code minecraft:weighted_list}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<IntProviderType<?>> WEIGHTED_LIST = create(key("weighted_list"));

    private IntProviderTypeKeys() {
    }

    private static TypedKey<IntProviderType<?>> create(final Key key) {
        return TypedKey.create(RegistryKey.INT_PROVIDER_TYPE, key);
    }
}
