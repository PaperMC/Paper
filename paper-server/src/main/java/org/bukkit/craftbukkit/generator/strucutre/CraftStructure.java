package org.bukkit.craftbukkit.generator.strucutre;

import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;

public class CraftStructure extends Structure {

    public static Structure minecraftToBukkit(net.minecraft.world.level.levelgen.structure.Structure minecraft, IRegistryCustom registryHolder) {
        if (minecraft == null) {
            return null;
        }

        return Registry.STRUCTURE.get(CraftNamespacedKey.fromMinecraft(registryHolder.registryOrThrow(Registries.STRUCTURE).getKey(minecraft)));
    }

    public static net.minecraft.world.level.levelgen.structure.Structure bukkitToMinecraft(Structure bukkit) {
        if (bukkit == null) {
            return null;
        }

        return ((CraftStructure) bukkit).getHandle();
    }

    private final NamespacedKey key;
    private final net.minecraft.world.level.levelgen.structure.Structure structure;
    private final StructureType structureType;

    public CraftStructure(NamespacedKey key, net.minecraft.world.level.levelgen.structure.Structure structure) {
        this.key = key;
        this.structure = structure;
        this.structureType = CraftStructureType.minecraftToBukkit(structure.type());
    }

    public net.minecraft.world.level.levelgen.structure.Structure getHandle() {
        return structure;
    }

    @Override
    public StructureType getStructureType() {
        return structureType;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
