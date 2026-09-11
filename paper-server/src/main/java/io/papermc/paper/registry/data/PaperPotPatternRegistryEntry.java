package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import io.papermc.paper.block.pot.PotPatternType;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperPotPatternRegistryEntry implements PotPatternRegistryEntry {

    protected @Nullable Identifier assetId;

    public PaperPotPatternRegistryEntry(
        final Conversions ignoredConversions,
        final @Nullable DecoratedPotPattern internal
    ) {
        if (internal == null) return;

        this.assetId = internal.assetId();
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetId, "assetId"));
    }

    public static final class PaperBuilder extends PaperPotPatternRegistryEntry implements Builder, PaperRegistryBuilder<DecoratedPotPattern, PotPatternType> {

        public PaperBuilder(final Conversions conversions, final @Nullable DecoratedPotPattern internal) {
            super(conversions, internal);
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public DecoratedPotPattern build() {
            return new DecoratedPotPattern(
                asConfigured(this.assetId, "assetId")
            );
        }
    }
}
