package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftSound implements Sound, Handleable<SoundEvent> {

    private static int count = 0;

    public static Sound minecraftToBukkit(SoundEvent minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.SOUND_EVENT, Registry.SOUNDS);
    }

    public static SoundEvent bukkitToMinecraft(Sound bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<SoundEvent> bukkitToMinecraftHolder(Sound bukkit) {
        Preconditions.checkArgument(bukkit != null);

        net.minecraft.core.Registry<SoundEvent> registry = CraftRegistry.getMinecraftRegistry(Registries.SOUND_EVENT);

        if (registry.wrapAsHolder(CraftSound.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<SoundEvent> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own sound effect with out properly registering it.");
    }

    private final NamespacedKey key;
    private final SoundEvent soundEffect;
    private final String name;
    private final int ordinal;

    public CraftSound(NamespacedKey key, SoundEvent soundEffect) {
        this.key = key;
        this.soundEffect = soundEffect;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive sound specific values.
        // Custom sounds will return the key with namespace. For a plugin this should look than like a new sound
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT).replace('.', '_');
        } else {
            this.name = key.toString();
        }
        this.ordinal = CraftSound.count++;
    }

    @Override
    public SoundEvent getHandle() {
        return this.soundEffect;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public int compareTo(@NotNull Sound sound) {
        return this.ordinal - sound.ordinal();
    }

    @NotNull
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int ordinal() {
        return this.ordinal;
    }

    @Override
    public String toString() {
        // For backwards compatibility
        return this.name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftSound otherSound)) {
            return false;
        }

        return this.getKey().equals(otherSound.getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    // Paper start
    public static String getSound(Sound sound) {
        return sound.getKey().getKey();
    }
    // Paper end
}
