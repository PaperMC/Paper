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
        net.minecraft.world.level.levelgen.structure.BoundingBox box = this.handle.getBoundingBox();
        return new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }
}
