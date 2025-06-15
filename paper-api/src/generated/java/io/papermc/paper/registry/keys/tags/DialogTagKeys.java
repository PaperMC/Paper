package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#DIALOG}.
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
@ApiStatus.Experimental
public final class DialogTagKeys {
    /**
     * {@code #minecraft:pause_screen_additions}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Dialog> PAUSE_SCREEN_ADDITIONS = create(key("pause_screen_additions"));

    /**
     * {@code #minecraft:quick_actions}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Dialog> QUICK_ACTIONS = create(key("quick_actions"));

    private DialogTagKeys() {
    }

    /**
     * Creates a tag key for {@link Dialog} in the registry {@code minecraft:dialog}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Dialog> create(final Key key) {
        return TagKey.create(RegistryKey.DIALOG, key);
    }
}
