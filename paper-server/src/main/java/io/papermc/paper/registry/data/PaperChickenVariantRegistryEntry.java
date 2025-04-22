package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperChickenVariantRegistryEntry implements ChickenVariantRegistryEntry {

    protected ChickenVariant.@Nullable ModelType model;
    protected ResourceLocation assetId;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperChickenVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable ChickenVariant internal
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
    public Chicken.Variant.@Nullable Model model() {
        return this.model == null ? Chicken.Variant.Model.NORMAL : Chicken.Variant.Model.values()[this.model.ordinal()];
    }

    public static final class PaperBuilder extends PaperChickenVariantRegistryEntry implements Builder, PaperRegistryBuilder<ChickenVariant, Chicken.Variant> {

        private final ChickenVariant internal;

        public PaperBuilder(final Conversions conversions, final @Nullable ChickenVariant internal) {
            super(conversions, internal);

            this.internal = internal;
        }

        @Override
        public Builder model(final Chicken.Variant.Model model) {
            this.model = CraftChicken.CraftVariant.toNms(model);
            return this;
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public ChickenVariant build() {
            return new ChickenVariant(
                new ModelAndTexture<>(this.model == null ? ChickenVariant.ModelType.NORMAL : this.model, this.assetId),
                internal.spawnConditions()
            );
        }
    }
}
