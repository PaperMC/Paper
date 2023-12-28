package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Axis;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        switch (effect) {
        case VILLAGER_PLANT_GROW:
            datavalue = (Integer) data;
            break;
        case POTION_BREAK:
            datavalue = ((Potion) data).toDamageValue() & 0x3F;
            break;
        case INSTANT_POTION_BREAK:
            datavalue = ((Color) data).asRGB();
            break;
        case RECORD_PLAY:
            Preconditions.checkArgument(data == Material.AIR || ((Material) data).isRecord(), "Invalid record type for Material %s!", data);
            datavalue = Item.getId(CraftItemType.bukkitToMinecraft((Material) data));
            break;
        case SMOKE:
            switch ((BlockFace) data) {
            case DOWN:
            // SPIGOT-6318: Fallback value for the old directions
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case SELF:
                datavalue = 0;
                break;
            case UP:
                datavalue = 1;
                break;
            case NORTH:
                datavalue = 2;
                break;
            case SOUTH:
                datavalue = 3;
                break;
            case WEST:
                datavalue = 4;
                break;
            case EAST:
                datavalue = 5;
                break;
            default:
                throw new IllegalArgumentException("Bad smoke direction!");
            }
            break;
        case STEP_SOUND:
            Preconditions.checkArgument(((Material) data).isBlock(), "Material %s is not a block!", data);
            datavalue = Block.getId(CraftBlockType.bukkitToMinecraft((Material) data).defaultBlockState());
            break;
        case COMPOSTER_FILL_ATTEMPT:
            datavalue = ((Boolean) data) ? 1 : 0;
            break;
        case BONE_MEAL_USE:
            datavalue = (Integer) data;
            break;
        case ELECTRIC_SPARK:
            if (data == null) {
                datavalue = -1;
            } else {
                switch ((Axis) data) {
                case X:
                    datavalue = 0;
                    break;
                case Y:
                    datavalue = 1;
                    break;
                case Z:
                    datavalue = 2;
                    break;
                default:
                    throw new IllegalArgumentException("Bad electric spark axis!");
                }
            }
            break;
        default:
            datavalue = 0;
        }
        return datavalue;
    }
}
