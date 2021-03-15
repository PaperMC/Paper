package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityBed;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;

public class CraftBed extends CraftBlockEntityState<TileEntityBed> implements Bed {

    public CraftBed(Block block) {
        super(block, TileEntityBed.class);
    }

    public CraftBed(Material material, TileEntityBed te) {
        super(material, te);
    }

    @Override
    public DyeColor getColor() {
        switch (getType()) {
            case BLACK_BED:
                return DyeColor.BLACK;
            case BLUE_BED:
                return DyeColor.BLUE;
            case BROWN_BED:
                return DyeColor.BROWN;
            case CYAN_BED:
                return DyeColor.CYAN;
            case GRAY_BED:
                return DyeColor.GRAY;
            case GREEN_BED:
                return DyeColor.GREEN;
            case LIGHT_BLUE_BED:
                return DyeColor.LIGHT_BLUE;
            case LIGHT_GRAY_BED:
                return DyeColor.LIGHT_GRAY;
            case LIME_BED:
                return DyeColor.LIME;
            case MAGENTA_BED:
                return DyeColor.MAGENTA;
            case ORANGE_BED:
                return DyeColor.ORANGE;
            case PINK_BED:
                return DyeColor.PINK;
            case PURPLE_BED:
                return DyeColor.PURPLE;
            case RED_BED:
                return DyeColor.RED;
            case WHITE_BED:
                return DyeColor.WHITE;
            case YELLOW_BED:
                return DyeColor.YELLOW;
            default:
                throw new IllegalArgumentException("Unknown DyeColor for " + getType());
        }
    }

    @Override
    public void setColor(DyeColor color) {
        throw new UnsupportedOperationException("Must set block type to appropriate bed colour");
    }
}
