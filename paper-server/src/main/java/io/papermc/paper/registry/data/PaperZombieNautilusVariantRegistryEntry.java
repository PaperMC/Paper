package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.data.variant.PaperSpawnConditions;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.ZombieNautilus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperZombieNautilusVariantRegistryEntry implements ZombieNautilusVariantRegistryEntry {

    protected final Conversions conversions;
    protected ZombieNautilusVariant.@Nullable ModelType model;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperZombieNautilusVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable ZombieNautilusVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientTextureAsset = internal.modelAndTexture().asset();
        this.model = internal.modelAndTexture().model();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset clientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public Model model() {
        return switch (asConfigured(this.model, "model")) {
            case NORMAL -> Model.NORMAL;
            case WARM -> Model.WARM;
        };
    }

    @Override
    public @Unmodifiable List<SpawnConditionPriority> spawnConditions() {
        return PaperSpawnConditions.fromNms(this.spawnConditions);
    }

    public static final class PaperBuilder extends PaperZombieNautilusVariantRegistryEntry implements Builder, PaperRegistryBuilder<ZombieNautilusVariant, ZombieNautilus.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable ZombieNautilusVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientTextureAsset(final ClientTextureAsset clientTextureAsset) {
            this.clientTextureAsset = MCUtil.toResourceTexture(asArgument(clientTextureAsset, "clientTextureAsset"));
            return this;
        }

        @Override
        public Builder model(final Model model) {
            this.model = switch (asArgument(model, "model")) {
                case NORMAL -> ZombieNautilusVariant.ModelType.NORMAL;
                case WARM -> ZombieNautilusVariant.ModelType.WARM;
            };
            return this;
        }

        @Override
        public Builder spawnConditions(final List<SpawnConditionPriority> spawnConditions) {
            this.spawnConditions = PaperSpawnConditions.fromApi(asArgument(spawnConditions, "spawnConditions"), this.conversions);
            return this;
        }

        @Override
        public ZombieNautilusVariant build() {
            return new ZombieNautilusVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientTextureAsset, "clientTextureAsset")),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
