package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.entity.Pig;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperPigVariantRegistryEntry implements PigVariantRegistryEntry {

    protected PigVariant.@Nullable ModelType model;
    protected ResourceLocation assetId;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperPigVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable PigVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.assetId = internal.modelAndTexture().asset().id();
        this.model = internal.modelAndTexture().model();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetId, "assetId"));
    }

    @Override
    public Pig.Variant.@Nullable Model model() {
        return this.model == null ? Pig.Variant.Model.NORMAL : Pig.Variant.Model.values()[this.model.ordinal()];
    }

    public static final class PaperBuilder extends PaperPigVariantRegistryEntry implements Builder, PaperRegistryBuilder<PigVariant, Pig.Variant> {

        private final PigVariant internal;

        public PaperBuilder(final Conversions conversions, final @Nullable PigVariant internal) {
            super(conversions, internal);

            this.internal = internal;
        }

        @Override
        public Builder model(final Pig.Variant.Model model) {
            this.model = CraftPig.CraftVariant.toNms(model);
            return this;
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public PigVariant build() {
            return new PigVariant(
                new ModelAndTexture<>(this.model == null ? PigVariant.ModelType.NORMAL : this.model, this.assetId),
                internal.spawnConditions()
            );
        }
    }
}
