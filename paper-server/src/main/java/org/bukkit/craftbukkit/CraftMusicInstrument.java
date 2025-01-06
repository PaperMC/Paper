package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.util.Holderable;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class CraftMusicInstrument extends MusicInstrument implements Holderable<Instrument> {

    public static MusicInstrument minecraftToBukkit(Instrument minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.INSTRUMENT, Registry.INSTRUMENT);
    }

    public static MusicInstrument minecraftHolderToBukkit(Holder<Instrument> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registry.INSTRUMENT);
    }

    public static Instrument bukkitToMinecraft(MusicInstrument bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<Instrument> bukkitToMinecraftHolder(MusicInstrument bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.INSTRUMENT);
    }

    public static Object bukkitToString(MusicInstrument bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((CraftMusicInstrument) bukkit).toBukkitSerializationObject(Instrument.CODEC);
    }

    public static MusicInstrument stringToBukkit(Object string) {
        Preconditions.checkArgument(string != null);

        return io.papermc.paper.util.Holderable.fromBukkitSerializationObject(string, Instrument.CODEC, Registry.INSTRUMENT);
    }

    private final Holder<Instrument> holder;

    public CraftMusicInstrument(Holder<Instrument> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<Instrument> getHolder() {
        return this.holder;
    }

    @Override
    public float getDuration() {
        return this.getHandle().useDuration();
    }

    @Override
    public float getRange() {
        return this.getHandle().range();
    }

    @Override
    public Sound getSoundEvent() {
        return CraftSound.minecraftHolderToBukkit(this.getHandle().soundEvent());
    }

    @NotNull
    @Override
    public Component getDescription() {
        return PaperAdventure.asAdventure(this.getHandle().description());
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public @NotNull String translationKey() {
        if (!(this.getHandle().description().getContents() instanceof final net.minecraft.network.chat.contents.TranslatableContents translatableContents)) {
            throw new UnsupportedOperationException("Description isn't translatable!");
        }
        return translatableContents.getKey();
    }

    @Override
    public boolean equals(final Object o) {
        return this.implEquals(o);
    }

    @Override
    public int hashCode() {
        return this.implHashCode();
    }

    @Override
    public String toString() {
        return this.implToString();
    }
}
