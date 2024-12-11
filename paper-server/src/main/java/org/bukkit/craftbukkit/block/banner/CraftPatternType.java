package org.bukkit.craftbukkit.block.banner;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;

public class CraftPatternType implements PatternType, Handleable<BannerPattern> {

    private static int count = 0;

    public static PatternType minecraftToBukkit(BannerPattern minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BANNER_PATTERN, Registry.BANNER_PATTERN);
    }

    public static PatternType minecraftHolderToBukkit(Holder<BannerPattern> minecraft) {
        return CraftPatternType.minecraftToBukkit(minecraft.value());
    }

    public static BannerPattern bukkitToMinecraft(PatternType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<BannerPattern> bukkitToMinecraftHolder(PatternType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        net.minecraft.core.Registry<BannerPattern> registry = CraftRegistry.getMinecraftRegistry(Registries.BANNER_PATTERN);

        if (registry.wrapAsHolder(CraftPatternType.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<BannerPattern> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own banner pattern without properly registering it.");
    }

    private final NamespacedKey key;
    private final BannerPattern bannerPatternType;
    private final String name;
    private final int ordinal;

    public CraftPatternType(NamespacedKey key, BannerPattern bannerPatternType) {
        this.key = key;
        this.bannerPatternType = bannerPatternType;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive pattern type specific values.
        // Custom pattern types will return the key with namespace. For a plugin this should look than like a new pattern type
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT);
        } else {
            this.name = key.toString();
        }
        this.ordinal = CraftPatternType.count++;
    }

    @Override
    public BannerPattern getHandle() {
        return this.bannerPatternType;
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public int compareTo(PatternType patternType) {
        return this.ordinal - patternType.ordinal();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int ordinal() {
        return this.ordinal;
    }

    @Override
    public String toString() {
        // For backwards compatibility
        return this.name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftPatternType)) {
            return false;
        }

        return this.getKey().equals(((PatternType) other).getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
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
