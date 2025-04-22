package io.papermc.paper.datacomponent.item;

import net.minecraft.world.item.EitherHolder;
import org.bukkit.JukeboxSong;
import org.bukkit.craftbukkit.CraftJukeboxSong;
import org.bukkit.craftbukkit.CraftRegistry;
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
        return this.impl.song()
            .unwrap(CraftRegistry.getMinecraftRegistry())
            .map(CraftJukeboxSong::minecraftHolderToBukkit)
            .orElseThrow();
    }

    static final class BuilderImpl implements JukeboxPlayable.Builder {

        private JukeboxSong song;

        BuilderImpl(final JukeboxSong song) {
            this.song = song;
        }

        @Override
        public JukeboxPlayable.Builder jukeboxSong(final JukeboxSong song) {
            this.song = song;
            return this;
        }

        @Override
        public JukeboxPlayable build() {
            return new PaperJukeboxPlayable(new net.minecraft.world.item.JukeboxPlayable(
                new EitherHolder<>(CraftJukeboxSong.bukkitToMinecraftHolder(this.song))
            ));
        }
    }
}
