package io.papermc.paper.datacomponent.item;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.map.MapCursor;
import org.jspecify.annotations.Nullable;

public record PaperMapDecorations(
    net.minecraft.world.item.component.MapDecorations impl
) implements MapDecorations, Handleable<net.minecraft.world.item.component.MapDecorations> {

    @Override
    public net.minecraft.world.item.component.MapDecorations getHandle() {
        return this.impl;
    }

    @Override
    public @Nullable DecorationEntry decoration(final String id) {
        final net.minecraft.world.item.component.MapDecorations.Entry decoration = this.impl.decorations().get(id);
        if (decoration == null) {
            return null;
        }

        return new PaperDecorationEntry(decoration);
    }

    @Override
    public Map<String, DecorationEntry> decorations() {
        if (this.impl.decorations().isEmpty()) {
            return Collections.emptyMap();
        }

        final Set<Map.Entry<String, net.minecraft.world.item.component.MapDecorations.Entry>> entries = this.impl.decorations().entrySet();
        final Map<String, DecorationEntry> decorations = new Object2ObjectOpenHashMap<>(entries.size());
        for (final Map.Entry<String, net.minecraft.world.item.component.MapDecorations.Entry> entry : entries) {
            decorations.put(entry.getKey(), new PaperDecorationEntry(entry.getValue()));
        }

        return Collections.unmodifiableMap(decorations);
    }

    public record PaperDecorationEntry(net.minecraft.world.item.component.MapDecorations.Entry entry) implements DecorationEntry {

        public static DecorationEntry toApi(final MapCursor.Type type, final double x, final double z, final float rotation) {
            return new PaperDecorationEntry(new net.minecraft.world.item.component.MapDecorations.Entry(CraftMapCursor.CraftType.bukkitToMinecraftHolder(type), x, z, rotation));
        }

        @Override
        public MapCursor.Type type() {
            return CraftMapCursor.CraftType.minecraftHolderToBukkit(this.entry.type());
        }

        @Override
        public double x() {
            return this.entry.x();
        }

        @Override
        public double z() {
            return this.entry.z();
        }

        @Override
        public float rotation() {
            return this.entry.rotation();
        }
    }

    static final class BuilderImpl implements Builder {

        private final Map<String, net.minecraft.world.item.component.MapDecorations.Entry> entries = new Object2ObjectOpenHashMap<>();

        @Override
        public MapDecorations.Builder put(final String id, final DecorationEntry entry) {
            this.entries.put(id, new net.minecraft.world.item.component.MapDecorations.Entry(CraftMapCursor.CraftType.bukkitToMinecraftHolder(entry.type()), entry.x(), entry.z(), entry.rotation()));
            return this;
        }

        @Override
        public Builder putAll(final Map<String, DecorationEntry> entries) {
            entries.forEach(this::put);
            return this;
        }

        @Override
        public MapDecorations build() {
            if (this.entries.isEmpty()) {
                return new PaperMapDecorations(net.minecraft.world.item.component.MapDecorations.EMPTY);
            }
            return new PaperMapDecorations(new net.minecraft.world.item.component.MapDecorations(new Object2ObjectOpenHashMap<>(this.entries)));
        }
    }
}
