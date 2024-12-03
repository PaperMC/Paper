package org.bukkit.craftbukkit.inventory.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        handle = new CustomModelData(
                SerializableMeta.getList(Float.class, map, "floats"),
                SerializableMeta.getList(Boolean.class, map, "flags"),
                SerializableMeta.getList(String.class, map, "strings"),
                SerializableMeta.getList(Color.class, map, "colors").stream().map(Color::asRGB).toList()
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("floats", getFloats());
        result.put("flags", getFlags());
        result.put("strings", getStrings());
        result.put("colors", getColors());

        return result;
    }

    public CustomModelData getHandle() {
        return handle;
    }

    @Override
    public List<Float> getFloats() {
        return Collections.unmodifiableList(handle.floats());
    }

    @Override
    public void setFloats(List<Float> floats) {
        handle = new CustomModelData(new ArrayList<>(floats), handle.flags(), handle.strings(), handle.colors());
    }

    @Override
    public List<Boolean> getFlags() {
        return Collections.unmodifiableList(handle.flags());
    }

    @Override
    public void setFlags(List<Boolean> flags) {
        handle = new CustomModelData(handle.floats(), new ArrayList<>(handle.flags()), handle.strings(), handle.colors());
    }

    @Override
    public List<String> getStrings() {
        return Collections.unmodifiableList(handle.strings());
    }

    @Override
    public void setStrings(List<String> strings) {
        handle = new CustomModelData(handle.floats(), handle.flags(), new ArrayList<>(handle.strings()), handle.colors());
    }

    @Override
    public List<Color> getColors() {
        return getHandle().colors().stream().map(Color::fromRGB).toList();
    }

    @Override
    public void setColors(List<Color> colors) {
        handle = new CustomModelData(handle.floats(), handle.flags(), handle.strings(), new ArrayList<>(handle.colors()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftCustomModelDataComponent other = (CraftCustomModelDataComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public String toString() {
        return "CraftCustomModelDataComponent{" + "handle=" + handle + '}';
    }
}
