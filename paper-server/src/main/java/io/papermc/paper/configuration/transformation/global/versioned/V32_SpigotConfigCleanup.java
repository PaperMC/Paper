package io.papermc.paper.configuration.transformation.global.versioned;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.MoveStrategy;

import static io.papermc.paper.configuration.transformation.Transformations.move;
import static org.spongepowered.configurate.NodePath.path;

public final class V32_SpigotConfigCleanup {

    private static final int VERSION = 32;

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(
            VERSION,
            ConfigurationTransformation.builder()
                .moveStrategy(MoveStrategy.MERGE)
                .addAction(path("player-auto-save"), move(path("players", "auto-save")))
                .addAction(path("misc", "strict-advancement-dimension-check"), move(path("advancements", "strict-dimension-check")))
                .build()
        );

    }

    private V32_SpigotConfigCleanup() {
    }
}
