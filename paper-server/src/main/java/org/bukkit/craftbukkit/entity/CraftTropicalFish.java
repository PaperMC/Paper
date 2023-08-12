package org.bukkit.craftbukkit.entity;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.animal.EntityTropicalFish;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;

public class CraftTropicalFish extends CraftFish implements TropicalFish {

    public CraftTropicalFish(CraftServer server, EntityTropicalFish entity) {
        super(server, entity);
    }

    @Override
    public EntityTropicalFish getHandle() {
        return (EntityTropicalFish) entity;
    }

    @Override
    public String toString() {
        return "CraftTropicalFish";
    }

    @Override
    public DyeColor getPatternColor() {
        return getPatternColor(getHandle().getPackedVariant());
    }

    @Override
    public void setPatternColor(DyeColor color) {
        getHandle().setPackedVariant(getData(color, getBodyColor(), getPattern()));
    }

    @Override
    public DyeColor getBodyColor() {
        return getBodyColor(getHandle().getPackedVariant());
    }

    @Override
    public void setBodyColor(DyeColor color) {
        getHandle().setPackedVariant(getData(getPatternColor(), color, getPattern()));
    }

    @Override
    public Pattern getPattern() {
        return getPattern(getHandle().getPackedVariant());
    }

    @Override
    public void setPattern(Pattern pattern) {
        getHandle().setPackedVariant(getData(getPatternColor(), getBodyColor(), pattern));
    }

    public static enum CraftPattern {
        KOB(0, false),
        SUNSTREAK(1, false),
        SNOOPER(2, false),
        DASHER(3, false),
        BRINELY(4, false),
        SPOTTY(5, false),
        FLOPPER(0, true),
        STRIPEY(1, true),
        GLITTER(2, true),
        BLOCKFISH(3, true),
        BETTY(4, true),
        CLAYFISH(5, true);

        private final int variant;
        private final boolean large;

        //
        private static final Map<Integer, Pattern> BY_DATA = new HashMap<>();

        static {
            for (CraftPattern type : values()) {
                BY_DATA.put(type.getDataValue(), Pattern.values()[type.ordinal()]);
            }
        }

        public static Pattern fromData(int data) {
            return BY_DATA.get(data);
        }

        private CraftPattern(int variant, boolean large) {
            this.variant = variant;
            this.large = large;
        }

        public int getDataValue() {
            return variant << 8 | ((large) ? 1 : 0);
        }
    }

    public static int getData(DyeColor patternColor, DyeColor bodyColor, Pattern type) {
        return patternColor.getWoolData() << 24 | bodyColor.getWoolData() << 16 | CraftPattern.values()[type.ordinal()].getDataValue();
    }

    public static DyeColor getPatternColor(int data) {
        return DyeColor.getByWoolData((byte) ((data >> 24) & 0xFF));
    }

    public static DyeColor getBodyColor(int data) {
        return DyeColor.getByWoolData((byte) ((data >> 16) & 0xFF));
    }

    public static Pattern getPattern(int data) {
        return CraftPattern.fromData(data & 0xFFFF);
    }
}
