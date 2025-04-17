package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Wolf;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperWolfVariantRegistryEntry implements WolfVariantRegistryEntry {

    protected ClientAsset assetInfoAngry;
    protected ClientAsset assetInfoWild;
    protected ClientAsset assetInfoTame;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperWolfVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable WolfVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.assetInfoAngry = internal.assetInfo().angry();
        this.assetInfoWild = internal.assetInfo().wild();
        this.assetInfoTame = internal.assetInfo().tame();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public Key assetIdAngry() {
        return PaperAdventure.asAdventure(asConfigured(this.assetInfoAngry.id(), "assetIdAngry"));
    }

    @Override
    public Key assetIdWild() {
        return PaperAdventure.asAdventure(asConfigured(this.assetInfoWild.id(), "assetIdWild"));
    }

    @Override
    public Key assetIdTame() {
        return PaperAdventure.asAdventure(asConfigured(this.assetInfoTame.id(), "assetIdTame"));
    }

    public static final class PaperBuilder extends PaperWolfVariantRegistryEntry implements Builder, PaperRegistryBuilder<WolfVariant, Wolf.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable WolfVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder assetIdAngry(final Key assetId) {
            this.assetInfoAngry = new ClientAsset(PaperAdventure.asVanilla(asArgument(assetId, "assetIdAngry")));
            return this;
        }

        @Override
        public Builder assetIdWild(final Key assetId) {
            this.assetInfoWild = new ClientAsset(PaperAdventure.asVanilla(asArgument(assetId, "assetIdWild")));
            return this;
        }

        @Override
        public Builder assetIdTame(final Key assetId) {
            this.assetInfoTame = new ClientAsset(PaperAdventure.asVanilla(asArgument(assetId, "assetIdTame")));
            return this;
        }

        @Override
        public WolfVariant build() {
            return new WolfVariant(new WolfVariant.AssetInfo(assetInfoWild, assetInfoTame, assetInfoAngry), SpawnPrioritySelectors.EMPTY);
        }
    }
}
