package org.bukkit.craftbukkit.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TropicalFish;

public class CraftTropicalFish extends io.papermc.paper.entity.PaperSchoolableFish implements TropicalFish { // Paper - Schooling Fish API

    public CraftTropicalFish(CraftServer server, net.minecraft.world.entity.animal.TropicalFish entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.TropicalFish getHandle() {
        return (net.minecraft.world.entity.animal.TropicalFish) this.entity;
    }

    @Override
    public DyeColor getPatternColor() {
        return CraftTropicalFish.getPatternColor(this.getHandle().getPackedVariant());
    }

    @Override
    public void setPatternColor(DyeColor color) {
        this.getHandle().setPackedVariant(CraftTropicalFish.getData(color, this.getBodyColor(), this.getPattern()));
    }

    @Override
    public DyeColor getBodyColor() {
        return CraftTropicalFish.getBodyColor(this.getHandle().getPackedVariant());
    }

    @Override
    public void setBodyColor(DyeColor color) {
        this.getHandle().setPackedVariant(CraftTropicalFish.getData(this.getPatternColor(), color, this.getPattern()));
    }

    @Override
    public Pattern getPattern() {
        return CraftTropicalFish.getPattern(this.getHandle().getPackedVariant());
    }

    @Override
    public void setPattern(Pattern pattern) {
        this.getHandle().setPackedVariant(CraftTropicalFish.getData(this.getPatternColor(), this.getBodyColor(), pattern));
    }

    public static int getData(DyeColor patternColor, DyeColor bodyColor, Pattern type) {
        net.minecraft.world.entity.animal.TropicalFish.Pattern pattern = net.minecraft.world.entity.animal.TropicalFish.Pattern.valueOf(type.name());
        return patternColor.getWoolData() << 24 | bodyColor.getWoolData() << 16 | pattern.getPackedId();
    }

    public static DyeColor getPatternColor(int data) {
        return DyeColor.getByWoolData((byte) ((data >> 24) & 0xFF));
    }

    public static DyeColor getBodyColor(int data) {
        return DyeColor.getByWoolData((byte) ((data >> 16) & 0xFF));
    }

    public static Pattern getPattern(int data) {
        return Pattern.valueOf(net.minecraft.world.entity.animal.TropicalFish.getPattern(data).name());
    }
}
