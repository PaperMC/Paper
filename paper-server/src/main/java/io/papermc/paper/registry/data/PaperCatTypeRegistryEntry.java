package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Cat;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCatTypeRegistryEntry implements CatTypeRegistryEntry {

    protected ClientAsset assetInfo;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperCatTypeRegistryEntry(
        final Conversions conversions,
        final @Nullable CatVariant internal
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

    public static final class PaperBuilder extends PaperCatTypeRegistryEntry implements Builder, PaperRegistryBuilder<CatVariant, Cat.Type> {

        private final CatVariant internal;

        public PaperBuilder(final Conversions conversions, final @Nullable CatVariant internal) {
            super(conversions, internal);

            this.internal = internal;
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetInfo = new ClientAsset(PaperAdventure.asVanilla(asArgument(assetId, "assetId")));
            return this;
        }

        @Override
        public CatVariant build() {
            return new CatVariant(assetInfo, internal.spawnConditions());
        }
    }
}
