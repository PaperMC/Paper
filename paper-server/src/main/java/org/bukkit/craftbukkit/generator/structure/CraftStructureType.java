package org.bukkit.craftbukkit.generator.structure;

import io.papermc.paper.util.HolderableElement;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.generator.structure.StructureType;

public class CraftStructureType extends StructureType implements HolderableElement<net.minecraft.world.level.levelgen.structure.StructureType<?>, StructureType> {

    public static StructureType minecraftToBukkit(net.minecraft.world.level.levelgen.structure.StructureType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.STRUCTURE_TYPE);
    }

    public static net.minecraft.world.level.levelgen.structure.StructureType<?> bukkitToMinecraft(StructureType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final Holder<net.minecraft.world.level.levelgen.structure.StructureType<?>> holder;

    public CraftStructureType(final Holder<net.minecraft.world.level.levelgen.structure.StructureType<?>> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.levelgen.structure.StructureType<?>> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return HolderableElement.super.getKey();
    }

    @Override
    public int hashCode() {
        return HolderableElement.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return HolderableElement.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return HolderableElement.super.implToString();
    }
}
