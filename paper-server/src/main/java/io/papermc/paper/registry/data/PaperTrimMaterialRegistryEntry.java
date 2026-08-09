package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperTrimMaterialRegistryEntry implements TrimMaterialRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Identifier paletteId;
    protected net.minecraft.network.chat.@Nullable Component description;

    public PaperTrimMaterialRegistryEntry(final Conversions conversions, final @Nullable TrimMaterial internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.paletteId = internal.paletteId();
        this.description = internal.description();
    }

    @Override
    public Key paletteId() {
        return PaperAdventure.asAdventure(asConfigured(this.paletteId, "paletteId"));
    }

    @Override
    public Component description() {
        return this.conversions.asAdventure(asConfigured(this.description, "description"));
    }

    public static final class PaperBuilder extends PaperTrimMaterialRegistryEntry implements Builder, PaperRegistryBuilder<TrimMaterial, org.bukkit.inventory.meta.trim.TrimMaterial> {

        public PaperBuilder(final Conversions conversions, final @Nullable TrimMaterial internal) {
            super(conversions, internal);
        }

        @Override
        public Builder paletteId(final Key paletteId) {
            this.paletteId = PaperAdventure.asVanilla(asConfigured(paletteId, "paletteId"));
            return this;
        }

        @Override
        public Builder description(final Component description) {
            this.description = this.conversions.asVanilla(asArgument(description, "description"));
            return this;
        }

        @Override
        public TrimMaterial build() {
            return new TrimMaterial(
                asConfigured(this.paletteId, "paletteId"),
                asConfigured(this.description, "description")
            );
        }
    }
}
