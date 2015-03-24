package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntityBanner;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftBanner extends CraftBlockState implements Banner {

    private final TileEntityBanner banner;
    private DyeColor base;
    private List<Pattern> patterns = new ArrayList<Pattern>();

    public CraftBanner(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        banner = (TileEntityBanner) world.getTileEntityAt(getX(), getY(), getZ());

        base = DyeColor.getByDyeData((byte) banner.color);

        if (banner.patterns != null) {
            for (int i = 0; i < banner.patterns.size(); i++) {
                NBTTagCompound p = (NBTTagCompound) banner.patterns.get(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    public CraftBanner(final Material material, final TileEntityBanner te) {
        super(material);
        banner = te;

        base = DyeColor.getByDyeData((byte) banner.color);

        if (banner.patterns != null) {
            for (int i = 0; i < banner.patterns.size(); i++) {
                NBTTagCompound p = (NBTTagCompound) banner.patterns.get(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInt("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return this.patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return this.patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            banner.color = base.getDyeData();

            NBTTagList newPatterns = new NBTTagList();

            for (Pattern p : patterns) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInt("Color", p.getColor().getDyeData());
                compound.setString("Pattern", p.getPattern().getIdentifier());
                newPatterns.add(compound);
            }

            banner.patterns = newPatterns;

            banner.update();
        }

        return result;
    }
}
