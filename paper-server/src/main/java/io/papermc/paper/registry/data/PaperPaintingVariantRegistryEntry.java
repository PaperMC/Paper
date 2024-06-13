package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.Optional;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.Art;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asArgumentRange;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperPaintingVariantRegistryEntry implements PaintingVariantRegistryEntry {

    protected OptionalInt width = OptionalInt.empty();
    protected OptionalInt height = OptionalInt.empty();
    protected @Nullable Component title;
    protected @Nullable Component author;
    protected @Nullable ResourceLocation assetId;

    protected final Conversions conversions;

    public PaperPaintingVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable PaintingVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.width = OptionalInt.of(internal.width());
        this.height = OptionalInt.of(internal.height());
        this.title = internal.title().orElse(null);
        this.author = internal.author().orElse(null);
        this.assetId = internal.assetId();
    }

    @Override
    public @Range(from = 1, to = 16) int width() {
        return asConfigured(this.width, "width");
    }

    @Override
    public @Range(from = 1, to = 16) int height() {
        return asConfigured(this.height, "height");
    }

    @Override
    public net.kyori.adventure.text.@Nullable Component title() {
        return this.title == null ? null : this.conversions.asAdventure(this.title);
    }

    @Override
    public net.kyori.adventure.text.@Nullable Component author() {
        return this.author == null ? null : this.conversions.asAdventure(this.author);
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetId, "assetId"));
    }

    public static final class PaperBuilder extends PaperPaintingVariantRegistryEntry implements PaintingVariantRegistryEntry.Builder, PaperRegistryBuilder<PaintingVariant, Art> {

        public PaperBuilder(final Conversions conversions, final @Nullable PaintingVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder width(final @Range(from = 1, to = 16) int width) {
            this.width = OptionalInt.of(asArgumentRange(width, "width", 1, 16));
            return this;
        }

        @Override
        public Builder height(final @Range(from = 1, to = 16) int height) {
            this.height = OptionalInt.of(asArgumentRange(height, "height", 1, 16));
            return this;
        }

        @Override
        public Builder title(final net.kyori.adventure.text.@Nullable Component title) {
            this.title = this.conversions.asVanilla(title);
            return this;
        }

        @Override
        public Builder author(final net.kyori.adventure.text.@Nullable Component author) {
            this.author = this.conversions.asVanilla(author);
            return this;
        }

        @Override
        public Builder assetId(final Key assetId) {
            this.assetId = PaperAdventure.asVanilla(asArgument(assetId, "assetId"));
            return this;
        }

        @Override
        public PaintingVariant build() {
            return new PaintingVariant(
                this.width(),
                this.height(),
                asConfigured(this.assetId, "assetId"),
                Optional.ofNullable(this.title),
                Optional.ofNullable(this.author)
            );
        }
    }
}
