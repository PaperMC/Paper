package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;

public record PaperBannerPatternLayers(
    net.minecraft.world.level.block.entity.BannerPatternLayers impl
) implements BannerPatternLayers, Handleable<net.minecraft.world.level.block.entity.BannerPatternLayers> {

    private static List<Pattern> convert(final net.minecraft.world.level.block.entity.BannerPatternLayers nmsPatterns) {
        return MCUtil.transformUnmodifiable(nmsPatterns.layers(), input -> {
            final Optional<PatternType> type = CraftRegistry.unwrapAndConvertHolder(RegistryKey.BANNER_PATTERN, input.pattern());
            return new Pattern(Objects.requireNonNull(DyeColor.getByWoolData((byte) input.color().getId())), type.orElseThrow(() -> new IllegalStateException("Inlined banner patterns are not supported yet in the API!")));
        });
    }

    @Override
    public net.minecraft.world.level.block.entity.BannerPatternLayers getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<Pattern> patterns() {
        return convert(impl);
    }

    static final class BuilderImpl implements BannerPatternLayers.Builder {

        private final net.minecraft.world.level.block.entity.BannerPatternLayers.Builder builder = new net.minecraft.world.level.block.entity.BannerPatternLayers.Builder();

        @Override
        public BannerPatternLayers.Builder add(final Pattern pattern) {
            this.builder.add(
                CraftPatternType.bukkitToMinecraftHolder(pattern.getPattern()),
                net.minecraft.world.item.DyeColor.byId(pattern.getColor().getWoolData())
            );
            return this;
        }

        @Override
        public BannerPatternLayers.Builder addAll(final List<Pattern> patterns) {
            patterns.forEach(this::add);
            return this;
        }

        @Override
        public BannerPatternLayers build() {
            return new PaperBannerPatternLayers(this.builder.build());
        }
    }
}
