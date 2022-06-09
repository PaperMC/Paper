package io.papermc.paper.configuration.transformation.world.versioned;

import io.papermc.paper.configuration.type.number.IntOr;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

/**
 * Several configurations that set a y-level used '0' as the "disabled" value.
 * Since 0 is now a valid value, they need to be updated.
 */
public final class V29_ZeroWorldHeight implements TransformAction {

    private static final int VERSION = 29;

    private static final String FIXES_KEY = "fixes";
    private static final String FALLING_BLOCK_HEIGHT_NERF_KEY = "falling-block-height-nerf";
    private static final String TNT_ENTITY_HEIGHT_NERF_KEY = "tnt-entity-height-nerf";

    private static final String ENVIRONMENT_KEY = "environment";
    private static final String NETHER_CEILING_VOID_DAMAGE_HEIGHT_KEY = "nether-ceiling-void-damage-height";

    private static final V29_ZeroWorldHeight INSTANCE = new V29_ZeroWorldHeight();

    private V29_ZeroWorldHeight() {
    }

    public static void apply(ConfigurationTransformation.VersionedBuilder builder) {
        final ConfigurationTransformation transformation = ConfigurationTransformation.builder()
            .addAction(path(FIXES_KEY, FALLING_BLOCK_HEIGHT_NERF_KEY), INSTANCE)
            .addAction(path(FIXES_KEY, TNT_ENTITY_HEIGHT_NERF_KEY), INSTANCE)
            .addAction(path(ENVIRONMENT_KEY, NETHER_CEILING_VOID_DAMAGE_HEIGHT_KEY), INSTANCE)
            .build();
        builder.addVersion(VERSION, transformation);
    }

    @Override
    public Object @Nullable [] visitPath(NodePath path, ConfigurationNode value) throws ConfigurateException {
        if (value.getInt() == 0) {
            value.set(IntOr.Disabled.DISABLED);
        }
        return null;
    }
}
