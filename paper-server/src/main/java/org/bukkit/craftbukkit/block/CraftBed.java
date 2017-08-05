package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.server.EnumColor;
import net.minecraft.server.TileEntityBed;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;

public class CraftBed extends CraftBlockEntityState<TileEntityBed> implements Bed {

    private DyeColor color;

    public CraftBed(Block block) {
        super(block, TileEntityBed.class);
    }

    public CraftBed(Material material, TileEntityBed te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityBed bed) {
        super.load(bed);

        color = DyeColor.getByWoolData((byte) bed.a().getColorIndex());
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
    public void applyTo(TileEntityBed bed) {
        super.applyTo(bed);

        bed.a(EnumColor.fromColorIndex(color.getWoolData()));
    }
}
