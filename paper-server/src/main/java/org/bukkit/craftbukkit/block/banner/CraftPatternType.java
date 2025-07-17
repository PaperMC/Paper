package org.bukkit.craftbukkit.block.banner;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftRegistry;

public class CraftPatternType extends OldEnumHolderable<PatternType, BannerPattern> implements PatternType {

    private static int count = 0;

    public static PatternType minecraftToBukkit(BannerPattern minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BANNER_PATTERN);
    }

    public static PatternType minecraftHolderToBukkit(Holder<BannerPattern> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.BANNER_PATTERN);
    }

    public static BannerPattern bukkitToMinecraft(PatternType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<BannerPattern> bukkitToMinecraftHolder(PatternType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftPatternType(Holder<BannerPattern> bannerPatternType) {
       super(bannerPatternType, count++);
    }

    @Override
    public String getIdentifier() {
        return switch (this.name()) {
            case "BASE" -> "b";
            case "SQUARE_BOTTOM_LEFT" -> "bl";
            case "SQUARE_BOTTOM_RIGHT" -> "br";
            case "SQUARE_TOP_LEFT" -> "tl";
            case "SQUARE_TOP_RIGHT" -> "tr";
            case "STRIPE_BOTTOM" -> "bs";
            case "STRIPE_TOP" -> "ts";
            case "STRIPE_LEFT" -> "ls";
            case "STRIPE_RIGHT" -> "rs";
            case "STRIPE_CENTER" -> "cs";
            case "STRIPE_MIDDLE" -> "ms";
            case "STRIPE_DOWNRIGHT" -> "drs";
            case "STRIPE_DOWNLEFT" -> "dls";
            case "SMALL_STRIPES" -> "ss";
            case "CROSS" -> "cr";
            case "STRAIGHT_CROSS" -> "sc";
            case "TRIANGLE_BOTTOM" -> "bt";
            case "TRIANGLE_TOP" -> "tt";
            case "TRIANGLES_BOTTOM" -> "bts";
            case "TRIANGLES_TOP" -> "tts";
            case "DIAGONAL_LEFT" -> "ld";
            case "DIAGONAL_UP_RIGHT" -> "rd";
            case "DIAGONAL_UP_LEFT" -> "lud";
            case "DIAGONAL_RIGHT" -> "rud";
            case "CIRCLE" -> "mc";
            case "RHOMBUS" -> "mr";
            case "HALF_VERTICAL" -> "vh";
            case "HALF_HORIZONTAL" -> "hh";
            case "HALF_VERTICAL_RIGHT" -> "vhr";
            case "HALF_HORIZONTAL_BOTTOM" -> "hhb";
            case "BORDER" -> "bo";
            case "CURLY_BORDER" -> "cbo";
            case "CREEPER" -> "cre";
            case "GRADIENT" -> "gra";
            case "GRADIENT_UP" -> "gru";
            case "BRICKS" -> "bri";
            case "SKULL" -> "sku";
            case "FLOWER" -> "flo";
            case "MOJANG" -> "moj";
            case "GLOBE" -> "glb";
            case "PIGLIN" -> "pig";
            case "FLOW" -> "flw";
            case "GUSTER" -> "gus";
            default -> this.getKey().toString();
        };
    }
}
