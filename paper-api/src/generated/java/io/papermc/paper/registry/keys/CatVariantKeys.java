package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#CAT_VARIANT}.
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
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class CatVariantKeys {
    /**
     * {@code minecraft:all_black}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> ALL_BLACK = create(key("all_black"));

    /**
     * {@code minecraft:black}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> BLACK = create(key("black"));

    /**
     * {@code minecraft:british_shorthair}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> BRITISH_SHORTHAIR = create(key("british_shorthair"));

    /**
     * {@code minecraft:calico}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> CALICO = create(key("calico"));

    /**
     * {@code minecraft:jellie}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> JELLIE = create(key("jellie"));

    /**
     * {@code minecraft:persian}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> PERSIAN = create(key("persian"));

    /**
     * {@code minecraft:ragdoll}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> RAGDOLL = create(key("ragdoll"));

    /**
     * {@code minecraft:red}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> RED = create(key("red"));

    /**
     * {@code minecraft:siamese}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> SIAMESE = create(key("siamese"));

    /**
     * {@code minecraft:tabby}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> TABBY = create(key("tabby"));

    /**
     * {@code minecraft:white}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Cat.Type> WHITE = create(key("white"));

    private CatVariantKeys() {
    }

    private static TypedKey<Cat.Type> create(final Key key) {
        return TypedKey.create(RegistryKey.CAT_VARIANT, key);
    }
}
