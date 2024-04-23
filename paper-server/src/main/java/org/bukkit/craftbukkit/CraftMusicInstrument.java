package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftMusicInstrument extends MusicInstrument implements Handleable<Instrument> {

    public static MusicInstrument minecraftToBukkit(Instrument minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.INSTRUMENT, Registry.INSTRUMENT);
    }

    public static MusicInstrument minecraftHolderToBukkit(Holder<Instrument> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static Instrument bukkitToMinecraft(MusicInstrument bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<Instrument> bukkitToMinecraftHolder(MusicInstrument bukkit) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<Instrument> registry = CraftRegistry.getMinecraftRegistry(Registries.INSTRUMENT);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<Instrument> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own instrument without properly registering it.");
    }

    private final NamespacedKey key;
    private final Instrument handle;

    public CraftMusicInstrument(NamespacedKey key, Instrument handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
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
