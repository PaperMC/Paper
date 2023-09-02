package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;

public class CraftMusicInstrument extends MusicInstrument {

    public static MusicInstrument minecraftToBukkit(Instrument minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<Instrument> registry = CraftRegistry.getMinecraftRegistry(Registries.INSTRUMENT);
        MusicInstrument bukkit = Registry.INSTRUMENT.get(CraftNamespacedKey.fromMinecraft(registry.getKey(minecraft)));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static Instrument bukkitToMinecraft(MusicInstrument bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((CraftMusicInstrument) bukkit).getHandle();
    }

    private final NamespacedKey key;
    private final Instrument handle;

    public CraftMusicInstrument(NamespacedKey key, Instrument handle) {
        this.key = key;
        this.handle = handle;
    }

    public Instrument getHandle() {
        return handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftMusicInstrument)) {
            return false;
        }

        return getKey().equals(((MusicInstrument) other).getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "CraftMusicInstrument{key=" + key + "}";
    }
}
