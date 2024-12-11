package org.bukkit.craftbukkit.generator.structure;

import org.bukkit.generator.structure.StructurePiece;
import org.bukkit.util.BoundingBox;

public class CraftStructurePiece implements StructurePiece {

    private final net.minecraft.world.level.levelgen.structure.StructurePiece handle;

    public CraftStructurePiece(net.minecraft.world.level.levelgen.structure.StructurePiece handle) {
        this.handle = handle;
    }

    @Override
    public BoundingBox getBoundingBox() {
        net.minecraft.world.level.levelgen.structure.BoundingBox bb = this.handle.getBoundingBox();
        return new BoundingBox(bb.minX(), bb.minY(), bb.minZ(), bb.maxX(), bb.maxY(), bb.maxZ());
    }
}
