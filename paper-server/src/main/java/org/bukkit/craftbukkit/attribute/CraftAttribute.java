package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftAttribute {

    public static Attribute minecraftToBukkit(AttributeBase minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<AttributeBase> registry = CraftRegistry.getMinecraftRegistry(Registries.ATTRIBUTE);
        Attribute bukkit = Registry.ATTRIBUTE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static Attribute stringToBukkit(String bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return Registry.ATTRIBUTE.get(NamespacedKey.fromString(bukkit));
    }

    public static AttributeBase bukkitToMinecraft(Attribute bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.ATTRIBUTE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}
