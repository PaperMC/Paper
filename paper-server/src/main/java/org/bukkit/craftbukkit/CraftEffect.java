package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import org.bukkit.Axis;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.JukeboxSong;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.inventory.ItemType;

public class CraftEffect {

    public static <T> int getDataValue(Effect effect, T data, RegistryAccess registryAccess) {
        int dataValue;
        switch (effect) {
            case SCULK_CHARGE:
            case TRIAL_SPAWNER_DETECT_PLAYER:
            case BEE_GROWTH:
            case TURTLE_EGG_PLACEMENT:
            case SMASH_ATTACK:
            case TRIAL_SPAWNER_DETECT_PLAYER_OMINOUS:
            case BONE_MEAL_USE:
                dataValue = (Integer) data;
                break;
            case POTION_BREAK:
            case INSTANT_POTION_BREAK:
                final Color color = (Color) data;
                Preconditions.checkArgument(color.getAlpha() == 0xFF, "Alpha channel is not supported");
                dataValue = color.asRGB();
                break;
            case RECORD_PLAY:
                JukeboxSong song;
                if (data instanceof Material material) {
                    ItemType item = material.asItemType();
                    Preconditions.checkArgument(item != null && item.hasDefaultData(DataComponentTypes.JUKEBOX_PLAYABLE), "Material %s is not a music disc", data);
                    song = item.getDefaultData(DataComponentTypes.JUKEBOX_PLAYABLE).jukeboxSong();
                } else {
                    song = (JukeboxSong) data;
                }
                dataValue = registryAccess.lookupOrThrow(Registries.JUKEBOX_SONG).getId(CraftJukeboxSong.bukkitToMinecraftHolder(song).value());
                break;
            case WHITE_SMOKE_SHOOT:
                final BlockFace face = (BlockFace) data;
                Preconditions.checkArgument(face.isCartesian(), face + " isn't cartesian");
                dataValue = org.bukkit.craftbukkit.block.CraftBlock.blockFaceToNotch(face).get3DDataValue();
                break;
            case SMOKE_SHOOT:
                switch ((BlockFace) data) {
                    case DOWN:
                        // SPIGOT-6318: Fallback value for the old directions
                    case NORTH_EAST:
                    case NORTH_WEST:
                    case SOUTH_EAST:
                    case SOUTH_WEST:
                    case SELF:
                        dataValue = 0;
                        break;
                    case UP:
                        dataValue = 1;
                        break;
                    case NORTH:
                        dataValue = 2;
                        break;
                    case SOUTH:
                        dataValue = 3;
                        break;
                    case WEST:
                        dataValue = 4;
                        break;
                    case EAST:
                        dataValue = 5;
                        break;
                    default:
                        throw new IllegalArgumentException("Bad smoke direction!");
                }
                break;
            case STEP_SOUND:
                if (data instanceof Material) {
                    Preconditions.checkArgument(((Material) data).isBlock(), "Material %s is not a block!", data);
                    dataValue = Block.getId(CraftBlockType.bukkitToMinecraft((Material) data).defaultBlockState());
                    break;
                }
                // use the blockdata otherwise
            case DESTROY_BLOCK:
            case BRUSH_BLOCK_COMPLETE:
                dataValue = Block.getId(((org.bukkit.craftbukkit.block.data.CraftBlockData) data).getState());
                break;
            case EXTINGUISH:
            case COMPOSTER_FILL_ATTEMPT:
            case TRIAL_SPAWNER_SPAWN:
            case TRIAL_SPAWNER_SPAWN_MOB_AT:
            case VAULT_ACTIVATE:
            case VAULT_DEACTIVATE:
            case TRIAL_SPAWNER_BECOME_OMINOUS:
            case TRIAL_SPAWNER_SPAWN_ITEM:
                dataValue = ((Boolean) data) ? 1 : 0;
                break;
            case ENDER_DRAGON_BREATH:
                dataValue = ((Boolean) data) ? -1 : 1;
                break;
            case ELECTRIC_SPARK:
                if (data == null) {
                    dataValue = -1;
                } else {
                    switch ((Axis) data) {
                        case X:
                            dataValue = 0;
                            break;
                        case Y:
                            dataValue = 1;
                            break;
                        case Z:
                            dataValue = 2;
                            break;
                        default:
                            throw new IllegalArgumentException("Bad electric spark axis!");
                    }
                }
                break;
            default:
                dataValue = 0;
        }
        return dataValue;
    }
}
