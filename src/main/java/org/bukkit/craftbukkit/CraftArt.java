package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPainting.EnumArt;
import org.bukkit.Art;

// Safety class, will break if either side changes
public class CraftArt {

    public static Art NotchToBukkit(EnumArt art) {
        switch (art) {
            case KEBAB: return Art.KEBAB;
            case AZTEC: return Art.AZTEC;
            case ALBAN: return Art.ALBAN;
            case AZTEC_2: return Art.AZTEC2;
            case BOMB: return Art.BOMB;
            case PLANT: return Art.PLANT;
            case WASTELAND: return Art.WASTELAND;
            case POOL: return Art.POOL;
            case COURBET: return Art.COURBET;
            case SEA: return Art.SEA;
            case SUNSET: return Art.SUNSET;
            case CREEBET: return Art.CREEBET;
            case WANDERER: return Art.WANDERER;
            case GRAHAM: return Art.GRAHAM;
            case MATCH: return Art.MATCH;
            case BUST: return Art.BUST;
            case STAGE: return Art.STAGE;
            case VOID: return Art.VOID;
            case SKULL_AND_ROSES: return Art.SKULL_AND_ROSES;
            case FIGHTERS: return Art.FIGHTERS;
            case POINTER: return Art.POINTER;
            case PIGSCENE: return Art.PIGSCENE;
            case BURNING_SKULL: return Art.BURNINGSKULL;
            case SKELETON: return Art.SKELETON;
            case DONKEY_KONG: return Art.DONKEYKONG;
            case WITHER: return Art.WITHER;
            default:
                throw new AssertionError(art);
        }
    }

    public static EnumArt BukkitToNotch(Art art) {
        switch (art) {
            case KEBAB: return EnumArt.KEBAB;
            case AZTEC: return EnumArt.AZTEC;
            case ALBAN: return EnumArt.ALBAN;
            case AZTEC2: return EnumArt.AZTEC_2;
            case BOMB: return EnumArt.BOMB;
            case PLANT: return EnumArt.PLANT;
            case WASTELAND: return EnumArt.WASTELAND;
            case POOL: return EnumArt.POOL;
            case COURBET: return EnumArt.COURBET;
            case SEA: return EnumArt.SEA;
            case SUNSET: return EnumArt.SUNSET;
            case CREEBET: return EnumArt.CREEBET;
            case WANDERER: return EnumArt.WANDERER;
            case GRAHAM: return EnumArt.GRAHAM;
            case MATCH: return EnumArt.MATCH;
            case BUST: return EnumArt.BUST;
            case STAGE: return EnumArt.STAGE;
            case VOID: return EnumArt.VOID;
            case SKULL_AND_ROSES: return EnumArt.SKULL_AND_ROSES;
            case FIGHTERS: return EnumArt.FIGHTERS;
            case POINTER: return EnumArt.POINTER;
            case PIGSCENE: return EnumArt.PIGSCENE;
            case BURNINGSKULL: return EnumArt.BURNING_SKULL;
            case SKELETON: return EnumArt.SKELETON;
            case DONKEYKONG: return EnumArt.DONKEY_KONG;
            case WITHER: return EnumArt.WITHER;
            default:
                throw new AssertionError(art);
        }
    }
}
