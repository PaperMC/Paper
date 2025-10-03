package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperChickenVariantRegistryEntry implements ChickenVariantRegistryEntry {

    protected ChickenVariant.@Nullable ModelType model;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperChickenVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable ChickenVariant internal
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
        return this.conversions.asBukkit(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public Model model() {
        return switch (asConfigured(this.model, "model")) {
            case NORMAL -> Model.NORMAL;
            case COLD -> Model.COLD;
        };
    }

    public static final class PaperBuilder extends PaperChickenVariantRegistryEntry implements Builder, PaperRegistryBuilder<ChickenVariant, Chicken.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable ChickenVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientTextureAsset(final ClientTextureAsset clientTextureAsset) {
            this.clientTextureAsset = this.conversions.asVanilla(asArgument(clientTextureAsset, "clientTextureAsset"));
            return this;
        }

        @Override
        public Builder model(final Model model) {
            this.model = switch (asArgument(model, "model")) {
                case NORMAL -> ChickenVariant.ModelType.NORMAL;
                case COLD -> ChickenVariant.ModelType.COLD;
            };
            return this;
        }

        @Override
        public ChickenVariant build() {
            return new ChickenVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientTextureAsset, "clientTextureAsset")),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
