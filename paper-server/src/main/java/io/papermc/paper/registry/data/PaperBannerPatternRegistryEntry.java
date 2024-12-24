package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.block.banner.PatternType;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.*;

public class PaperBannerPatternRegistryEntry implements BannerPatternRegistryEntry {

    protected @Nullable ResourceLocation assetId;
    protected @Nullable String translationKey;

    public PaperBannerPatternRegistryEntry(
        final Conversions ignoredConversions,
        final @Nullable BannerPattern internal
    ) {
        if (internal == null) return;

        this.assetId = internal.assetId();
        this.translationKey = internal.translationKey();
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetId, "assetId"));
    }

    @Override
    public String translationKey() {
        return asConfigured(this.translationKey, "translationKey");
    }

    public static final class PaperBuilder extends PaperBannerPatternRegistryEntry implements Builder, PaperRegistryBuilder<BannerPattern, PatternType> {

        public PaperBuilder(final Conversions conversions, final @Nullable BannerPattern internal) {
            super(conversions, internal);
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public Builder translationKey(final String translationKey) {
            this.translationKey = asArgument(translationKey, "translationKey");
            return this;
        }

        @Override
        public BannerPattern build() {
            return new BannerPattern(
                asConfigured(this.assetId, "assetId"),
                this.translationKey()
            );
        }
    }
}
