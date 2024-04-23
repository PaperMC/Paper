package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.inventory.meta.BannerMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBanner extends CraftMetaItem implements BannerMeta {

    private static final Set<Material> BANNER_MATERIALS = Sets.newHashSet(
            Material.BLACK_BANNER,
            Material.BLACK_WALL_BANNER,
            Material.BLUE_BANNER,
            Material.BLUE_WALL_BANNER,
            Material.BROWN_BANNER,
            Material.BROWN_WALL_BANNER,
            Material.CYAN_BANNER,
            Material.CYAN_WALL_BANNER,
            Material.GRAY_BANNER,
            Material.GRAY_WALL_BANNER,
            Material.GREEN_BANNER,
            Material.GREEN_WALL_BANNER,
            Material.LIGHT_BLUE_BANNER,
            Material.LIGHT_BLUE_WALL_BANNER,
            Material.LIGHT_GRAY_BANNER,
            Material.LIGHT_GRAY_WALL_BANNER,
            Material.LIME_BANNER,
            Material.LIME_WALL_BANNER,
            Material.MAGENTA_BANNER,
            Material.MAGENTA_WALL_BANNER,
            Material.ORANGE_BANNER,
            Material.ORANGE_WALL_BANNER,
            Material.PINK_BANNER,
            Material.PINK_WALL_BANNER,
            Material.PURPLE_BANNER,
            Material.PURPLE_WALL_BANNER,
            Material.RED_BANNER,
            Material.RED_WALL_BANNER,
            Material.WHITE_BANNER,
            Material.WHITE_WALL_BANNER,
            Material.YELLOW_BANNER,
            Material.YELLOW_WALL_BANNER
    );

    static final ItemMetaKeyType<BannerPatternLayers> PATTERNS = new ItemMetaKeyType<>(DataComponents.BANNER_PATTERNS, "patterns");

    private List<Pattern> patterns = new ArrayList<Pattern>();

    CraftMetaBanner(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaBanner)) {
            return;
        }

        CraftMetaBanner banner = (CraftMetaBanner) meta;
        patterns = new ArrayList<Pattern>(banner.patterns);
    }

    CraftMetaBanner(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, PATTERNS).ifPresent((entityTag) -> {
            List<BannerPatternLayers.b> patterns = entityTag.layers();
            for (int i = 0; i < Math.min(patterns.size(), 20); i++) {
                BannerPatternLayers.b p = patterns.get(i);
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

        Iterable<?> rawPatternList = SerializableMeta.getObject(Iterable.class, map, PATTERNS.BUKKIT, true);
        if (rawPatternList == null) {
            return;
        }

        for (Object obj : rawPatternList) {
            Preconditions.checkArgument(obj instanceof Pattern, "Object (%s) in pattern list is not valid", obj.getClass());
            addPattern((Pattern) obj);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        List<BannerPatternLayers.b> newPatterns = new ArrayList<>();

        for (Pattern p : patterns) {
            newPatterns.add(new BannerPatternLayers.b(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), EnumColor.byId(p.getColor().getWoolData())));
        }

        tag.put(PATTERNS, new BannerPatternLayers(newPatterns));
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
        patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (!patterns.isEmpty()) {
            builder.put(PATTERNS.BUKKIT, ImmutableList.copyOf(patterns));
        }

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (!patterns.isEmpty()) {
            hash = 31 * hash + patterns.hashCode();
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

            return patterns.equals(that.patterns);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBanner || patterns.isEmpty());
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && patterns.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        return BANNER_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaBanner clone() {
        CraftMetaBanner meta = (CraftMetaBanner) super.clone();
        meta.patterns = new ArrayList<>(patterns);
        return meta;
    }
}
