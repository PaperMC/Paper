package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftAttribute implements Attribute, Handleable<AttributeBase> {

    private static int count = 0;

    public static Attribute minecraftToBukkit(AttributeBase minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ATTRIBUTE, Registry.ATTRIBUTE);
    }

    public static Attribute minecraftHolderToBukkit(Holder<AttributeBase> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static Attribute stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        // We currently do not have any version-dependent remapping, so we can use current version
        // First convert from when only the names where saved
        string = FieldRename.convertAttributeName(ApiVersion.CURRENT, string);
        string = string.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);

        // Now also convert from when keys where saved
        return CraftRegistry.get(Registry.ATTRIBUTE, key, ApiVersion.CURRENT);
    }

    public static AttributeBase bukkitToMinecraft(Attribute bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<AttributeBase> bukkitToMinecraftHolder(Attribute bukkit) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<AttributeBase> registry = CraftRegistry.getMinecraftRegistry(Registries.ATTRIBUTE);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<AttributeBase> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own sound effect with out properly registering it.");
    }

    public static String bukkitToString(Attribute bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return bukkit.getKey().toString();
    }

    private final NamespacedKey key;
    private final AttributeBase attributeBase;
    private final String name;
    private final int ordinal;

    public CraftAttribute(NamespacedKey key, AttributeBase attributeBase) {
        this.key = key;
        this.attributeBase = attributeBase;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive attribute specific values.
        // Custom attributes will return the key with namespace. For a plugin this should look than like a new attribute
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT);
        } else {
            this.name = key.toString();
        }
        this.ordinal = count++;
    }

    @Override
    public AttributeBase getHandle() {
        return attributeBase;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return attributeBase.getDescriptionId();
    }

    @Override
    public int compareTo(@NotNull Attribute attribute) {
        return ordinal - attribute.ordinal();
    }

    @NotNull
    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        // For backwards compatibility
        return name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftAttribute otherAttribute)) {
            return false;
        }

        return getKey().equals(otherAttribute.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
