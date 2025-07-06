package io.papermc.paper.registry.data;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.Collections;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperTrimMaterialRegistryEntry implements TrimMaterialRegistryEntry {

    protected final Conversions conversions;
    protected MaterialAssetGroup.@Nullable AssetInfo baseAssetPath;
    protected Map<ResourceKey<EquipmentAsset>, MaterialAssetGroup.AssetInfo> assetPathOverrides = Collections.emptyMap();
    protected net.minecraft.network.chat.@Nullable Component description;

    public PaperTrimMaterialRegistryEntry(final Conversions conversions, final @Nullable TrimMaterial internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.baseAssetPath = internal.assets().base();
        this.assetPathOverrides = internal.assets().overrides();
        this.description = internal.description();
    }


    @Override
    @KeyPattern.Value
    public String baseAssetPath() {
        @Subst("suffix") final String path = asConfigured(this.baseAssetPath, "baseAssetPath").suffix();
        return path;
    }

    @Override
    public @Unmodifiable Map<Key, String> assetPathOverrides() {
        if (this.assetPathOverrides.isEmpty()) {
            return Collections.emptyMap();
        }
        final ImmutableMap.Builder<Key, String> builder = ImmutableMap.builder();
        this.assetPathOverrides.forEach((key, value) -> {
            builder.put(PaperAdventure.asAdventureKey(key), value.suffix());
        });
        return builder.buildOrThrow();
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
        public Builder baseAssetPath(final String baseAssetPath) {
            this.baseAssetPath = new MaterialAssetGroup.AssetInfo(asArgument(baseAssetPath, "baseAssetPath"));
            return this;
        }

        @Override
        public Builder assetPathOverrides(final Map<Key, String> assetPathOverrides) {
            final Map<Key, String> input = asArgument(assetPathOverrides, "assetPathOverrides");
            if (input.isEmpty()) {
                this.assetPathOverrides = Collections.emptyMap();
            } else {
                final ImmutableMap.Builder<ResourceKey<EquipmentAsset>, MaterialAssetGroup.AssetInfo> builder = ImmutableMap.builder();
                input.forEach((key, value) -> {
                    builder.put(PaperAdventure.asVanilla(EquipmentAssets.ROOT_ID, key), new MaterialAssetGroup.AssetInfo(value));
                });
                this.assetPathOverrides = builder.buildOrThrow();
            }
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
                new MaterialAssetGroup(
                    asConfigured(this.baseAssetPath, "baseAssetPath"),
                    asConfigured(this.assetPathOverrides, "assetPathOverrides")
                ),
                asConfigured(this.description, "description")
            );
        }
    }
}
