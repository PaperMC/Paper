package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.Holder;
import org.bukkit.JukeboxSong;
import org.bukkit.craftbukkit.CraftJukeboxSong;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperJukeboxPlayable(
    net.minecraft.world.item.JukeboxPlayable impl
) implements JukeboxPlayable, Handleable<net.minecraft.world.item.JukeboxPlayable> {

    @Override
    public net.minecraft.world.item.JukeboxPlayable getHandle() {
        return this.impl;
    }

    @Override
    public JukeboxSong jukeboxSong() {
        return CraftJukeboxSong.minecraftHolderToBukkit(this.impl.song());
    }

    @Override
    public TypedKey<JukeboxSong> jukeboxSongKey() {
        // jukebox songs on items can only be references to registered songs
        return PaperRegistries.fromNms(this.impl.song().unwrapKey().orElseThrow());
    }

    static final class BuilderImpl implements JukeboxPlayable.Builder {

        private Holder<net.minecraft.world.item.JukeboxSong> song;

        BuilderImpl(final Holder<net.minecraft.world.item.JukeboxSong> song) {
            this.song = song;
        }

        @Override
        public JukeboxPlayable.Builder jukeboxSong(final JukeboxSong song) {
            this.song = CraftJukeboxSong.bukkitToMinecraftHolder(song);
            return this;
        }

        @Override
        public JukeboxPlayable.Builder jukeboxSong(final TypedKey<JukeboxSong> song) {
            this.song = Conversions.current().getReferenceHolder(song);
            return this;
        }

        @Override
        public JukeboxPlayable build() {
            return new PaperJukeboxPlayable(new net.minecraft.world.item.JukeboxPlayable(this.song));
        }
    }
}
