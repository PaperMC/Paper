package io.papermc.paper.configuration.transformation.global.versioned;

import io.papermc.paper.configuration.Configurations.ContextMap;
import io.papermc.paper.configuration.transformation.ContextualTransformation;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.MoveStrategy;

import static io.papermc.paper.configuration.transformation.Transformations.move;
import static org.spongepowered.configurate.NodePath.path;

public final class V32_SpigotConfigCleanup implements ContextualTransformation {

    public static final V32_SpigotConfigCleanup INSTANCE = new V32_SpigotConfigCleanup();
    private static final int VERSION = 32;

    private V32_SpigotConfigCleanup() {
    }

    @Override
    public int version() {
        return VERSION;
    }

    @Override
    public ConfigurationTransformation create(final ContextMap context) {
        return ConfigurationTransformation.builder()
            .moveStrategy(MoveStrategy.MERGE)
            .addAction(path("player-auto-save"), move(path("players", "auto-save")))
            .addAction(path("misc", "strict-advancement-dimension-check"), move(path("advancements", "strict-dimension-check")))
            .build();
    }
}
