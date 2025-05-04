package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Pig;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperPigVariantRegistryEntry implements PigVariantRegistryEntry {

    protected PigVariant.@Nullable ModelType model;
    protected net.minecraft.core.@Nullable ClientAsset clientAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperPigVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable PigVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientAsset = internal.modelAndTexture().asset();
        this.model = internal.modelAndTexture().model();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientAsset clientAsset() {
        return this.conversions.asBukkit(asConfigured(this.clientAsset, "clientAsset"));
    }

    @Override
    public Model model() {
        return switch (asConfigured(this.model, "model")) {
            case NORMAL -> Model.NORMAL;
            case COLD -> Model.COLD;
        };
    }

    public static final class PaperBuilder extends PaperPigVariantRegistryEntry implements Builder, PaperRegistryBuilder<PigVariant, Pig.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable PigVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientAsset(final ClientAsset clientAsset) {
            this.clientAsset = this.conversions.asVanilla(asArgument(clientAsset, "clientAsset"));
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
        public PigVariant build() {
            return new PigVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientAsset, "clientAsset")),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
