package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;

public class CraftBanner extends CraftBlockEntityState<BannerBlockEntity> implements Banner {

    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(World world, BannerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBanner(CraftBanner state, Location location) {
        super(state, location);
        this.base = state.getBaseColor();
        this.patterns = state.getPatterns();
    }

    @Override
    public void load(BannerBlockEntity banner) {
        super.load(banner);

        this.base = DyeColor.getByWoolData((byte) ((AbstractBannerBlock) this.data.getBlock()).getColor().getId());
        this.patterns = new ArrayList<Pattern>();

        if (banner.getPatterns() != null) {
            for (int i = 0; i < banner.getPatterns().layers().size(); i++) {
                BannerPatternLayers.Layer p = banner.getPatterns().layers().get(i);
                this.patterns.add(new Pattern(DyeColor.getByWoolData((byte) p.color().getId()), CraftPatternType.minecraftHolderToBukkit(p.pattern())));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "color");
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(this.patterns);
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
        return this.patterns.size();
    }

    @Override
    public void applyTo(BannerBlockEntity banner) {
        super.applyTo(banner);

        banner.baseColor = net.minecraft.world.item.DyeColor.byId(this.base.getWoolData());

        List<BannerPatternLayers.Layer> newPatterns = new ArrayList<>();

        for (Pattern p : this.patterns) {
            newPatterns.add(new net.minecraft.world.level.block.entity.BannerPatternLayers.Layer(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), net.minecraft.world.item.DyeColor.byId(p.getColor().getWoolData())));
        }
        banner.setPatterns(new BannerPatternLayers(newPatterns));
    }

    @Override
    public CraftBanner copy() {
        return new CraftBanner(this, null);
    }

    @Override
    public CraftBanner copy(Location location) {
        return new CraftBanner(this, location);
    }
}
