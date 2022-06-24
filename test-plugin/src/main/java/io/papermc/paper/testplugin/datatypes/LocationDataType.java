package io.papermc.paper.testplugin.datatypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LocationDataType implements PersistentDataType<String, Location> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Location> getComplexType() {
        return Location.class;
    }

    // before you ask, no i do not care for null worlds
    @Override
    public @NotNull String toPrimitive(@NotNull Location complex, @NotNull PersistentDataAdapterContext context) {
        return complex.getX() + " " + complex.getY() + " " + complex.getZ() + " " + complex.getWorld().key().asString();
    }

    @Override
    public @NotNull Location fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        String[] tokens = primitive.split(" ");
        NamespacedKey namespacedKey = NamespacedKey.fromString(tokens[3]);

        return new Location(Bukkit.getWorld(namespacedKey), Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
    }
}
