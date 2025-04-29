package org.bukkit.craftbukkit.inventory.components;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.component.CustomModelData;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

@SerializableAs("CustomModelData")
public final class CraftCustomModelDataComponent implements CustomModelDataComponent {

    private CustomModelData handle;

    public CraftCustomModelDataComponent(CustomModelData handle) {
        this.handle = handle;
    }

    public CraftCustomModelDataComponent(CraftCustomModelDataComponent craft) {
        this.handle = craft.handle;
    }

    public CraftCustomModelDataComponent(Map<String, Object> map) {
        this.handle = new CustomModelData(
                SerializableMeta.getList(Float.class, map, "floats"),
                SerializableMeta.getList(Boolean.class, map, "flags"),
                SerializableMeta.getList(String.class, map, "strings"),
                SerializableMeta.getList(Color.class, map, "colors").stream().map(Color::asRGB).toList()
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("floats", this.getFloats());
        result.put("flags", this.getFlags());
        result.put("strings", this.getStrings());
        result.put("colors", this.getColors());

        return result;
    }

    public CustomModelData getHandle() {
        return this.handle;
    }

    @Override
    public List<Float> getFloats() {
        return Collections.unmodifiableList(this.handle.floats());
    }

    @Override
    public void setFloats(List<Float> floats) {
        this.handle = new CustomModelData(List.copyOf(floats), this.handle.flags(), this.handle.strings(), this.handle.colors());
    }

    @Override
    public List<Boolean> getFlags() {
        return Collections.unmodifiableList(this.handle.flags());
    }

    @Override
    public void setFlags(List<Boolean> flags) {
        this.handle = new CustomModelData(this.handle.floats(), List.copyOf(flags), this.handle.strings(), this.handle.colors());
    }

    @Override
    public List<String> getStrings() {
        return Collections.unmodifiableList(this.handle.strings());
    }

    @Override
    public void setStrings(List<String> strings) {
        this.handle = new CustomModelData(this.handle.floats(), this.handle.flags(), List.copyOf(strings), this.handle.colors());
    }

    @Override
    public List<Color> getColors() {
        return this.getHandle().colors().stream().map(color -> Color.fromRGB(color & 0x00FFFFFF)).toList(); // skip alpha channel
    }

    @Override
    public void setColors(List<Color> colors) {
        this.handle = new CustomModelData(this.handle.floats(), this.handle.flags(), this.handle.strings(), colors.stream().map(Color::asRGB).toList());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.handle.hashCode();
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
        final CraftCustomModelDataComponent other = (CraftCustomModelDataComponent) obj;
        return this.handle.equals(other.handle);
    }

    @Override
    public String toString() {
        return "CraftCustomModelDataComponent{component=" + this.handle + '}';
    }
}
