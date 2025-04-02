package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
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

    public CraftBanner(World world, BannerBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBanner(CraftBanner state, Location location) {
        super(state, location);
        this.base = state.getBaseColor();
        this.patterns = state.getPatterns();
    }

    @Override
    public void load(BannerBlockEntity blockEntity) {
        super.load(blockEntity);

        this.base = DyeColor.getByWoolData((byte) ((AbstractBannerBlock) this.data.getBlock()).getColor().getId());
        this.patterns = new ArrayList<>();

        for (int i = 0; i < blockEntity.getPatterns().layers().size(); i++) {
            BannerPatternLayers.Layer p = blockEntity.getPatterns().layers().get(i);
            // Paper start - fix upstream not handling inlined banner pattern
            java.util.Optional<org.bukkit.block.banner.PatternType> type = org.bukkit.craftbukkit.CraftRegistry.unwrapAndConvertHolder(RegistryKey.BANNER_PATTERN, p.pattern());
            if (type.isEmpty()) continue;
            this.patterns.add(new Pattern(DyeColor.getByWoolData((byte) p.color().getId()), type.get()));
            // Paper end - fix upstream not handling inlined banner pattern
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
    public void applyTo(BannerBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        blockEntity.baseColor = net.minecraft.world.item.DyeColor.byId(this.base.getWoolData());

        List<BannerPatternLayers.Layer> newPatterns = new ArrayList<>();

        for (Pattern p : this.patterns) {
            newPatterns.add(new net.minecraft.world.level.block.entity.BannerPatternLayers.Layer(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), net.minecraft.world.item.DyeColor.byId(p.getColor().getWoolData())));
        }
        blockEntity.setPatterns(new BannerPatternLayers(newPatterns));
    }

    @Override
    public CraftBanner copy() {
        return new CraftBanner(this, null);
    }

    @Override
    public CraftBanner copy(Location location) {
        return new CraftBanner(this, location);
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        return this.getSnapshot().name == null ? null : io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getSnapshot().name);
    }

    @Override
    public void customName(net.kyori.adventure.text.Component customName) {
        this.getSnapshot().name = customName == null ? null : io.papermc.paper.adventure.PaperAdventure.asVanilla(customName);
    }

    @Override
    public String getCustomName() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serializeOrNull(this.customName());
    }

    @Override
    public void setCustomName(String name) {
       this.customName(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserializeOrNull(name));
    }
}
