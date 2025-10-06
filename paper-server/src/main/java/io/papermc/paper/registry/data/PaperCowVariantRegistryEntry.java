package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Cow;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCowVariantRegistryEntry implements CowVariantRegistryEntry {

    protected CowVariant.@Nullable ModelType model = null;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset = null;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperCowVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable CowVariant internal
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
            case WARM -> Model.WARM;
        };
    }

    public static final class PaperBuilder extends PaperCowVariantRegistryEntry implements Builder, PaperRegistryBuilder<CowVariant, Cow.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable CowVariant internal) {
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
                case NORMAL -> CowVariant.ModelType.NORMAL;
                case COLD -> CowVariant.ModelType.COLD;
                case WARM -> CowVariant.ModelType.WARM;
            };
            return this;
        }

        @Override
        public CowVariant build() {
            return new CowVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientTextureAsset, "clientTextureAsset")),
                this.spawnConditions
            );
        }
    }
}
