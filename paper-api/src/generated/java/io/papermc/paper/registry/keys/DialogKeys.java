package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#DIALOG}.
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
@GeneratedFrom("1.21.6-pre3")
public final class DialogKeys {
    /**
     * {@code minecraft:custom_options}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Dialog> CUSTOM_OPTIONS = create(key("custom_options"));

    /**
     * {@code minecraft:quick_actions}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Dialog> QUICK_ACTIONS = create(key("quick_actions"));

    /**
     * {@code minecraft:server_links}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Dialog> SERVER_LINKS = create(key("server_links"));

    private DialogKeys() {
    }

    /**
     * Creates a typed key for {@link Dialog} in the registry {@code minecraft:dialog}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Dialog> create(final Key key) {
        return TypedKey.create(RegistryKey.DIALOG, key);
    }
}
