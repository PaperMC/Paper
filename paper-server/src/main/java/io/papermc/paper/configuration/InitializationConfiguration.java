package io.papermc.paper.configuration;

import java.util.Set;
import org.spongepowered.configurate.objectmapping.meta.Comment;

/**
 * The {@code initialization} section of {@code paper-global.yml}.
 * <p>
 * This is deserialized <b>before</b> the rest of {@link GlobalConfiguration}, during {@link PaperConfigurationsInitializer}
 * because the server needs these values before a {@link net.minecraft.core.RegistryAccess} exists.
 * It is read from the same file, with the same removal and versioned transformations applied, but early.
 * <p>
 * Only primitive values are allowed.
 */
public class InitializationConfiguration extends ConfigurationPart {

    @Comment("""
        The directory worlds are stored in, relative to the server root.
        Read before the world folder is opened; overridden by the --universe launch argument.""")
    public String worldContainer = ".";

    @Comment("""
        The directory inside the plugins folder that plugin updates are loaded from.
        Read before plugins are discovered. Set to an empty string to disable auto updating.""")
    public String updateFolder = "update";

    @Comment("""
        Advancements that will not be loaded at all. Accepts '*' to disable every advancement,
        a namespace to disable all advancements in it, or individual advancement keys.
        Read before datapacks are loaded, so it cannot live under the 'advancements' section.""")
    public Set<String> disabledAdvancements = Set.of("minecraft:story/disabled");
}
