package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.inventory.meta.BannerMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBanner extends CraftMetaItem implements BannerMeta {

    static final ItemMetaKeyType<BannerPatternLayers> PATTERNS = new ItemMetaKeyType<>(DataComponents.BANNER_PATTERNS, "patterns");

    private List<Pattern> patterns = new ArrayList<Pattern>();

    CraftMetaBanner(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaBanner)) {
            return;
        }

        CraftMetaBanner banner = (CraftMetaBanner) meta;
        this.patterns = new ArrayList<Pattern>(banner.patterns);
    }

    CraftMetaBanner(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaBanner.PATTERNS).ifPresent((entityTag) -> {
            List<BannerPatternLayers.Layer> patterns = entityTag.layers();
            for (int i = 0; i < Math.min(patterns.size(), 20); i++) {
                BannerPatternLayers.Layer p = patterns.get(i);
                DyeColor color = DyeColor.getByWoolData((byte) p.color().getId());
                PatternType pattern = CraftPatternType.minecraftHolderToBukkit(p.pattern());

                if (color != null && pattern != null) {
                    this.patterns.add(new Pattern(color, pattern));
                }
            }
        });
    }

    CraftMetaBanner(Map<String, Object> map) {
        super(map);

        Iterable<?> rawPatternList = SerializableMeta.getObject(Iterable.class, map, CraftMetaBanner.PATTERNS.BUKKIT, true);
        if (rawPatternList == null) {
            return;
        }

        for (Object obj : rawPatternList) {
            Preconditions.checkArgument(obj instanceof Pattern, "Object (%s) in pattern list is not valid", obj.getClass());
            this.addPattern((Pattern) obj);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        List<BannerPatternLayers.Layer> newPatterns = new ArrayList<>();

        for (Pattern p : this.patterns) {
            newPatterns.add(new BannerPatternLayers.Layer(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), net.minecraft.world.item.DyeColor.byId(p.getColor().getWoolData())));
        }

        tag.put(CraftMetaBanner.PATTERNS, new BannerPatternLayers(newPatterns));
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
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (!this.patterns.isEmpty()) {
            builder.put(CraftMetaBanner.PATTERNS.BUKKIT, ImmutableList.copyOf(this.patterns));
        }

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (!this.patterns.isEmpty()) {
            hash = 31 * hash + this.patterns.hashCode();
        }
        return original != hash ? CraftMetaBanner.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBanner) {
            CraftMetaBanner that = (CraftMetaBanner) meta;

            return this.patterns.equals(that.patterns);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBanner || this.patterns.isEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.patterns.isEmpty();
    }

    @Override
    public CraftMetaBanner clone() {
        CraftMetaBanner meta = (CraftMetaBanner) super.clone();
        meta.patterns = new ArrayList<>(this.patterns);
        return meta;
    }
}
