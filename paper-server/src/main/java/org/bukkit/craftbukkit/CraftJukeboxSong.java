package org.bukkit.craftbukkit;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.JukeboxSong;

public class CraftJukeboxSong extends HolderableBase<net.minecraft.world.item.JukeboxSong> implements JukeboxSong {

    public static JukeboxSong minecraftToBukkit(net.minecraft.world.item.JukeboxSong minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.JUKEBOX_SONG);
    }

    public static JukeboxSong minecraftHolderToBukkit(Holder<net.minecraft.world.item.JukeboxSong> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.JUKEBOX_SONG);
    }

    public static net.minecraft.world.item.JukeboxSong bukkitToMinecraft(JukeboxSong bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.JukeboxSong> bukkitToMinecraftHolder(JukeboxSong bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftJukeboxSong(final Holder<net.minecraft.world.item.JukeboxSong> holder) {
        super(holder);
    }

    @Override
    public String getTranslationKey() {
        if (!(this.getHandle().description().getContents() instanceof TranslatableContents)) throw new UnsupportedOperationException("Description isn't translatable!"); // Paper
        return ((TranslatableContents) this.getHandle().description().getContents()).getKey();
    }
}
