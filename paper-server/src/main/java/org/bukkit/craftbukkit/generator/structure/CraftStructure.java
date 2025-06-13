package org.bukkit.craftbukkit.generator.structure;

import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;

public class CraftStructure extends Structure implements Holderable<net.minecraft.world.level.levelgen.structure.Structure> {

    public static Structure minecraftToBukkit(net.minecraft.world.level.levelgen.structure.Structure minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.STRUCTURE);
    }

    public static net.minecraft.world.level.levelgen.structure.Structure bukkitToMinecraft(Structure bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final Holder<net.minecraft.world.level.levelgen.structure.Structure> holder;

    public CraftStructure(Holder<net.minecraft.world.level.levelgen.structure.Structure> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.levelgen.structure.Structure> getHolder() {
        return this.holder;
    }

    @Override
    public StructureType getStructureType() {
        return CraftStructureType.minecraftToBukkit(this.getHandle().type());
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
