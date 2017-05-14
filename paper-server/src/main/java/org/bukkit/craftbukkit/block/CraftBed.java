package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.server.EnumColor;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBed;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftBed extends CraftBlockState implements Bed {

    private final TileEntityBed bed;
    private DyeColor color;

    public CraftBed(Block block) {
        super(block);

        bed = (TileEntityBed) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
        color = DyeColor.getByWoolData((byte) bed.a().getColorIndex());
    }

    public CraftBed(Material material, TileEntityBed te) {
        super(material);

        bed = te;
        color = DyeColor.getByWoolData((byte) bed.a().getColorIndex());
    }

    @Override
    public TileEntity getTileEntity() {
        return bed;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "color");

        this.color = color;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            bed.a(EnumColor.fromColorIndex(color.getWoolData()));
            bed.update();
        }

        return result;
    }
}
