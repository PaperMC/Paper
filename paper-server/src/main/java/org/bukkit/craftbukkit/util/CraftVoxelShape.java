package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.util.BoundingBox;

public final class CraftVoxelShape implements org.bukkit.util.VoxelShape {

    private final VoxelShape shape;

    public CraftVoxelShape(VoxelShape shape) {
        this.shape = shape;
    }

    @Override
    public Collection<BoundingBox> getBoundingBoxes() {
        List<AxisAlignedBB> boxes = shape.d(); // PAIL rename toList
        List<BoundingBox> craftBoxes = new ArrayList<>(boxes.size());
        for (AxisAlignedBB aabb : boxes) {
            craftBoxes.add(new BoundingBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
        }
        return craftBoxes;
    }

    @Override
    public boolean overlaps(BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other cannot be null");

        for (BoundingBox box : getBoundingBoxes()) {
            if (box.overlaps(other)) {
                return true;
            }
        }

        return false;
    }
}
