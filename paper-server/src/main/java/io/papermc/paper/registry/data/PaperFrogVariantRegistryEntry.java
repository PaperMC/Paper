package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Frog;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperFrogVariantRegistryEntry implements FrogVariantRegistryEntry {

    protected ClientAsset assetInfo;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperFrogVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable FrogVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.assetInfo = internal.assetInfo();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetInfo.id(), "assetId"));
    }

    public static final class PaperBuilder extends PaperFrogVariantRegistryEntry implements Builder, PaperRegistryBuilder<FrogVariant, Frog.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable FrogVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetInfo = new ClientAsset(PaperAdventure.asVanilla(asArgument(assetId, "assetId")));
            return this;
        }

        @Override
        public FrogVariant build() {
            return new FrogVariant(assetInfo, SpawnPrioritySelectors.EMPTY);
        }
    }
}
