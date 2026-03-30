package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.JukeboxPlayable;
import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.CraftJukeboxSong;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;

@SerializableAs("JukeboxPlayable")
public final class CraftJukeboxComponent implements JukeboxPlayableComponent {

    private JukeboxPlayable handle;

    public CraftJukeboxComponent(JukeboxPlayable jukebox) {
        this.handle = jukebox;
    }

    public CraftJukeboxComponent(CraftJukeboxComponent jukebox) {
        this.handle = jukebox.handle;
    }

    public CraftJukeboxComponent(Map<String, Object> map) {
        String song = SerializableMeta.getObject(String.class, map, "song", false);

        final net.minecraft.core.Registry<net.minecraft.world.item.JukeboxSong> registry = CraftRegistry.getMinecraftRegistry(Registries.JUKEBOX_SONG);
        final Holder.Reference<net.minecraft.world.item.JukeboxSong> holder = registry.get(Identifier.parse(song)).orElseThrow();
        this.handle = new JukeboxPlayable(holder);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("song", this.getSongKey().toString());
        return result;
    }

    public JukeboxPlayable getHandle() {
        return this.handle;
    }

    @Override
    public JukeboxSong getSong() {
        return CraftJukeboxSong.minecraftHolderToBukkit(this.handle.song());
    }

    @Override
    public NamespacedKey getSongKey() {
        return CraftNamespacedKey.fromMinecraft(this.handle.song().unwrapKey().orElseThrow().identifier());
    }

    @Override
    public void setSong(JukeboxSong song) {
        Preconditions.checkArgument(song != null, "song cannot be null");

        this.handle = new JukeboxPlayable(CraftJukeboxSong.bukkitToMinecraftHolder(song));
    }

    @Override
    public void setSongKey(NamespacedKey song) {
        Preconditions.checkArgument(song != null, "song cannot be null");

        final net.minecraft.core.Registry<net.minecraft.world.item.JukeboxSong> registry = CraftRegistry.getMinecraftRegistry(Registries.JUKEBOX_SONG);
        final Holder.Reference<net.minecraft.world.item.JukeboxSong> holder = registry.get(CraftNamespacedKey.toMinecraft(song)).orElseThrow();
        this.handle = new JukeboxPlayable(holder);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.handle.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftJukeboxComponent other = (CraftJukeboxComponent) obj;
        return this.handle.equals(other.handle);
    }

    @Override
    public String toString() {
        return "CraftJukeboxComponent{component=" + this.handle + '}';
    }
}
