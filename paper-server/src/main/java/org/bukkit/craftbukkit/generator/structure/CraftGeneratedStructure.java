package org.bukkit.craftbukkit.generator.structure;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructurePiece;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.BoundingBox;

public class CraftGeneratedStructure implements GeneratedStructure {

    private final StructureStart handle;
    private List<StructurePiece> pieces;

    public CraftGeneratedStructure(StructureStart handle) {
        this.handle = handle;
    }

    @Override
    public BoundingBox getBoundingBox() {
        net.minecraft.world.level.levelgen.structure.BoundingBox box = this.handle.getBoundingBox();
        return new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    @Override
    public Structure getStructure() {
        return CraftStructure.minecraftToBukkit(this.handle.getStructure());
    }

    @Override
    public Collection<StructurePiece> getPieces() {
        if (this.pieces == null) { // Cache the pieces on first request
            ImmutableList.Builder<StructurePiece> builder = ImmutableList.builderWithExpectedSize(this.handle.getPieces().size());
            for (net.minecraft.world.level.levelgen.structure.StructurePiece piece : this.handle.getPieces()) {
                builder.add(new CraftStructurePiece(piece));
            }

            this.pieces = builder.build();
        }

        return this.pieces;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.handle.persistentDataContainer;
    }
}
