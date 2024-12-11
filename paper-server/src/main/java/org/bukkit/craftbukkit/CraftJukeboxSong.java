package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftJukeboxSong implements JukeboxSong, Handleable<net.minecraft.world.item.JukeboxSong> {

    public static JukeboxSong minecraftToBukkit(net.minecraft.world.item.JukeboxSong minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.JUKEBOX_SONG, Registry.JUKEBOX_SONG);
    }

    public static JukeboxSong minecraftHolderToBukkit(Holder<net.minecraft.world.item.JukeboxSong> minecraft) {
        return CraftJukeboxSong.minecraftToBukkit(minecraft.value());
    }

    public static net.minecraft.world.item.JukeboxSong bukkitToMinecraft(JukeboxSong bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.JukeboxSong> bukkitToMinecraftHolder(JukeboxSong bukkit) {
        Preconditions.checkArgument(bukkit != null);

        net.minecraft.core.Registry<net.minecraft.world.item.JukeboxSong> registry = CraftRegistry.getMinecraftRegistry(Registries.JUKEBOX_SONG);

        if (registry.wrapAsHolder(CraftJukeboxSong.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<net.minecraft.world.item.JukeboxSong> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own trim pattern without properly registering it.");
    }

    private final NamespacedKey key;
    private final net.minecraft.world.item.JukeboxSong handle;

    public CraftJukeboxSong(NamespacedKey key, net.minecraft.world.item.JukeboxSong handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    public net.minecraft.world.item.JukeboxSong getHandle() {
        return this.handle;
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return this.key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return ((TranslatableContents) this.handle.description().getContents()).getKey();
    }
}
