package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.data.variant.PaperSpawnConditions;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperPigVariantRegistryEntry implements PigVariantRegistryEntry {

    protected final Conversions conversions;
    protected PigVariant.@Nullable ModelType model;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperPigVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable PigVariant internal
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
            case COLD -> Model.COLD;
        };
    }

    @Override
    public @Unmodifiable List<SpawnConditionPriority> spawnConditions() {
        return PaperSpawnConditions.fromNms(this.spawnConditions);
    }

    public static final class PaperBuilder extends PaperPigVariantRegistryEntry implements Builder, PaperRegistryBuilder<PigVariant, Pig.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable PigVariant internal) {
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
                case NORMAL -> PigVariant.ModelType.NORMAL;
                case COLD -> PigVariant.ModelType.COLD;
            };
            return this;
        }

        @Override
        public Builder spawnConditions(final List<SpawnConditionPriority> spawnConditions) {
            this.spawnConditions = PaperSpawnConditions.fromApi(asArgument(spawnConditions, "spawnConditions"), this.conversions);
            return this;
        }

        @Override
        public PigVariant build() {
            return new PigVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientTextureAsset, "clientTextureAsset")),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
