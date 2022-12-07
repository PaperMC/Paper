package org.bukkit.craftbukkit.generator.strucutre;

import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.generator.structure.StructureType;

public class CraftStructureType extends StructureType {

    public static StructureType minecraftToBukkit(net.minecraft.world.level.levelgen.structure.StructureType<?> minecraft) {
        if (minecraft == null) {
            return null;
        }

        return Registry.STRUCTURE_TYPE.get(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.STRUCTURE_TYPE.getKey(minecraft)));
    }

    public static net.minecraft.world.level.levelgen.structure.StructureType<?> bukkitToMinecraft(StructureType bukkit) {
        if (bukkit == null) {
            return null;
        }

        return ((CraftStructureType) bukkit).getHandle();
    }

    private final NamespacedKey key;
    private final net.minecraft.world.level.levelgen.structure.StructureType<?> structureType;

    public CraftStructureType(NamespacedKey key, net.minecraft.world.level.levelgen.structure.StructureType<?> structureType) {
        this.key = key;
        this.structureType = structureType;
    }

    public net.minecraft.world.level.levelgen.structure.StructureType<?> getHandle() {
        return structureType;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
