package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.util.MCUtil;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperChickenVariantRegistryEntry implements ChickenVariantRegistryEntry {

    protected ChickenVariant.@Nullable ModelType model;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture babyClientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperChickenVariantRegistryEntry(
        final Conversions ignoredConversions,
        final @Nullable ChickenVariant internal
    ) {
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientTextureAsset = internal.modelAndTexture().asset();
        this.babyClientTextureAsset = internal.babyTexture();
        this.model = internal.modelAndTexture().model();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset clientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public ClientTextureAsset babyClientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.babyClientTextureAsset, "babyClientTextureAsset"));
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
            this.clientTextureAsset = MCUtil.toResourceTexture(asArgument(clientTextureAsset, "clientTextureAsset"));
            return this;
        }

        @Override
        public Builder babyClientTextureAsset(final ClientTextureAsset babyClientTextureAsset) {
            this.babyClientTextureAsset = MCUtil.toResourceTexture(asArgument(babyClientTextureAsset, "babyClientTextureAsset"));
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
                asConfigured(this.babyClientTextureAsset, "babyClientTextureAsset"),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
