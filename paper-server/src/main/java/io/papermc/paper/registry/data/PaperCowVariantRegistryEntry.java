package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.entity.Cow;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCowVariantRegistryEntry implements CowVariantRegistryEntry {

    protected CowVariant.@Nullable ModelType model;
    protected ResourceLocation assetId;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperCowVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable CowVariant internal
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
    public Cow.Variant.@Nullable Model model() {
        return this.model == null ? Cow.Variant.Model.NORMAL : Cow.Variant.Model.values()[this.model.ordinal()];
    }

    public static final class PaperBuilder extends PaperCowVariantRegistryEntry implements Builder, PaperRegistryBuilder<CowVariant, Cow.Variant> {

        private final CowVariant internal;

        public PaperBuilder(final Conversions conversions, final @Nullable CowVariant internal) {
            super(conversions, internal);

            this.internal = internal;
        }

        @Override
        public Builder model(final Cow.Variant.Model model) {
            this.model = CraftCow.CraftVariant.toNms(model);
            return this;
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public CowVariant build() {
            return new CowVariant(
                new ModelAndTexture<>(this.model == null ? CowVariant.ModelType.NORMAL : this.model, this.assetId),
                internal.spawnConditions()
            );
        }
    }
}
