package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperTrimPatternRegistryEntry implements TrimPatternRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Identifier assetId;
    protected net.minecraft.network.chat.@Nullable Component description;
    protected boolean decal = false;

    public PaperTrimPatternRegistryEntry(final Conversions conversions, final @Nullable TrimPattern internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.assetId = internal.assetId();
        this.description = internal.description();
        this.decal = internal.decal();
    }

    @Override
    public Key assetId() {
        return PaperAdventure.asAdventure(asConfigured(this.assetId, "assetId"));
    }

    @Override
    public Component description() {
        return this.conversions.asAdventure(asConfigured(this.description, "description"));
    }

    @Override
    public boolean decal() {
        return this.decal;
    }

    public static final class PaperBuilder extends PaperTrimPatternRegistryEntry implements Builder, PaperRegistryBuilder<TrimPattern, org.bukkit.inventory.meta.trim.TrimPattern> {

        public PaperBuilder(final Conversions conversions, final @Nullable TrimPattern internal) {
            super(conversions, internal);
        }

        @Override
        public Builder assetId(final Key key) {
            this.assetId = PaperAdventure.asVanilla(asArgument(key, "assetId"));
            return this;
        }

        @Override
        public Builder description(final Component description) {
            this.description = this.conversions.asVanilla(asArgument(description, "description"));
            return this;
        }

        @Override
        public Builder decal(final boolean decal) {
            this.decal = decal;
            return this;
        }

        @Override
        public TrimPattern build() {
            return new TrimPattern(
                asConfigured(this.assetId, "assetId"),
                asConfigured(this.description, "description"),
                this.decal
            );
        }
    }
}
