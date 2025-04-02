package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BedBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Bed;

public class CraftBed extends CraftBlockEntityState<BedBlockEntity> implements Bed {

    public CraftBed(World world, BedBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBed(CraftBed state, Location location) {
        super(state, location);
    }

    @Override
    public DyeColor getColor() {
        return switch (this.getType()) {
            case BLACK_BED -> DyeColor.BLACK;
            case BLUE_BED -> DyeColor.BLUE;
            case BROWN_BED -> DyeColor.BROWN;
            case CYAN_BED -> DyeColor.CYAN;
            case GRAY_BED -> DyeColor.GRAY;
            case GREEN_BED -> DyeColor.GREEN;
            case LIGHT_BLUE_BED -> DyeColor.LIGHT_BLUE;
            case LIGHT_GRAY_BED -> DyeColor.LIGHT_GRAY;
            case LIME_BED -> DyeColor.LIME;
            case MAGENTA_BED -> DyeColor.MAGENTA;
            case ORANGE_BED -> DyeColor.ORANGE;
            case PINK_BED -> DyeColor.PINK;
            case PURPLE_BED -> DyeColor.PURPLE;
            case RED_BED -> DyeColor.RED;
            case WHITE_BED -> DyeColor.WHITE;
            case YELLOW_BED -> DyeColor.YELLOW;
            default -> throw new IllegalArgumentException("Unknown DyeColor for " + this.getType());
        };
    }

    @Override
    public void setColor(DyeColor color) {
        throw new UnsupportedOperationException("Must set block type to appropriate bed colour");
    }

    @Override
    public CraftBed copy() {
        return new CraftBed(this, null);
    }

    @Override
    public CraftBed copy(Location location) {
        return new CraftBed(this, location);
    }
}
