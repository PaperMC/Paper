package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ShieldMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaShield extends CraftMetaItem implements ShieldMeta, BlockStateMeta {

    static final ItemMetaKeyType<EnumColor> BASE_COLOR = new ItemMetaKeyType<>(DataComponents.BASE_COLOR, "Base", "base-color");

    private Banner banner;

    CraftMetaShield(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaShield craftMetaShield) {
            if (craftMetaShield.banner != null) {
                this.banner = (Banner) craftMetaShield.banner.copy();
            }
        } else if (meta instanceof CraftMetaBlockState state && state.hasBlockState() && state.getBlockState() instanceof Banner banner) {
            this.banner = (Banner) banner.copy();
        }
    }

    CraftMetaShield(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, BASE_COLOR).ifPresent((color) -> {
            banner = getBlockState(DyeColor.getByWoolData((byte) color.getId()));
        });

        getOrEmpty(tag, CraftMetaBanner.PATTERNS).ifPresent((entityTag) -> {
            List<BannerPatternLayers.b> patterns = entityTag.layers();
            for (int i = 0; i < Math.min(patterns.size(), 20); i++) {
                BannerPatternLayers.b p = patterns.get(i);
                DyeColor color = DyeColor.getByWoolData((byte) p.color().getId());
                PatternType pattern = CraftPatternType.minecraftHolderToBukkit(p.pattern());

                if (color != null && pattern != null) {
                    addPattern(new Pattern(color, pattern));
                }
            }
        });
    }

    CraftMetaShield(Map<String, Object> map) {
        super(map);

        String baseColor = SerializableMeta.getString(map, BASE_COLOR.BUKKIT, true);
        if (baseColor != null) {
            banner = getBlockState(DyeColor.valueOf(baseColor));
        }

        Iterable<?> rawPatternList = SerializableMeta.getObject(Iterable.class, map, CraftMetaBanner.PATTERNS.BUKKIT, true);
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

        if (banner != null) {
            tag.put(BASE_COLOR, EnumColor.byId(banner.getBaseColor().getWoolData()));

            if (banner.numberOfPatterns() > 0) {
                List<BannerPatternLayers.b> newPatterns = new ArrayList<>();

                for (Pattern p : banner.getPatterns()) {
                    newPatterns.add(new BannerPatternLayers.b(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), EnumColor.byId(p.getColor().getWoolData())));
                }

                tag.put(CraftMetaBanner.PATTERNS, new BannerPatternLayers(newPatterns));
            }
        }
    }

    @Override
    public List<Pattern> getPatterns() {
        if (banner == null) {
            return new ArrayList<>();
        }

        return banner.getPatterns();
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        if (banner == null) {
            if (patterns.isEmpty()) {
                return;
            }

            banner = getBlockState(null);
        }

        banner.setPatterns(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        if (banner == null) {
            banner = getBlockState(null);
        }

        banner.addPattern(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        if (banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        return banner.getPattern(i);
    }

    @Override
    public Pattern removePattern(int i) {
        if (banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        return banner.removePattern(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        if (banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        banner.setPattern(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        if (banner == null) {
            return 0;
        }

        return banner.numberOfPatterns();
    }

    @Override
    public DyeColor getBaseColor() {
        if (banner == null) {
            return null;
        }

        return banner.getBaseColor();
    }

    @Override
    public void setBaseColor(DyeColor baseColor) {
        if (baseColor == null) {
            if (banner.numberOfPatterns() > 0) {
                banner.setBaseColor(DyeColor.WHITE);
            } else {
                banner = null;
            }
        } else {
            if (banner == null) {
                banner = getBlockState(baseColor);
            }

            banner.setBaseColor(baseColor);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (banner != null) {
            builder.put(BASE_COLOR.BUKKIT, banner.getBaseColor().toString());

            if (banner.numberOfPatterns() > 0) {
                builder.put(CraftMetaBanner.PATTERNS.BUKKIT, banner.getPatterns());
            }
        }

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (banner != null) {
            hash = 61 * hash + banner.hashCode();
        }
        return original != hash ? CraftMetaShield.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaShield that) {
            return Objects.equal(this.banner, that.banner);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaShield || this.banner == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.banner == null;
    }

    @Override
    public boolean hasBlockState() {
        return banner != null;
    }

    @Override
    public BlockState getBlockState() {
        return (banner != null) ? banner.copy() : getBlockState(null);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");
        Preconditions.checkArgument(blockState instanceof Banner, "Invalid blockState");

        this.banner = (Banner) blockState;
    }

    private static Banner getBlockState(DyeColor color) {
        BlockPosition pos = BlockPosition.ZERO;
        Material stateMaterial = shieldToBannerHack(color);

        return (Banner) CraftBlockStates.getBlockState(pos, stateMaterial, null);
    }

    @Override
    public CraftMetaShield clone() {
        CraftMetaShield meta = (CraftMetaShield) super.clone();
        if (this.banner != null) {
            meta.banner = (Banner) banner.copy();
        }
        return meta;
    }

    static Material shieldToBannerHack(DyeColor color) {
        if (color == null) {
            return Material.WHITE_BANNER;
        }

        return switch (color) {
            case WHITE -> Material.WHITE_BANNER;
            case ORANGE -> Material.ORANGE_BANNER;
            case MAGENTA -> Material.MAGENTA_BANNER;
            case LIGHT_BLUE -> Material.LIGHT_BLUE_BANNER;
            case YELLOW -> Material.YELLOW_BANNER;
            case LIME -> Material.LIME_BANNER;
            case PINK -> Material.PINK_BANNER;
            case GRAY -> Material.GRAY_BANNER;
            case LIGHT_GRAY -> Material.LIGHT_GRAY_BANNER;
            case CYAN -> Material.CYAN_BANNER;
            case PURPLE -> Material.PURPLE_BANNER;
            case BLUE -> Material.BLUE_BANNER;
            case BROWN -> Material.BROWN_BANNER;
            case GREEN -> Material.GREEN_BANNER;
            case RED -> Material.RED_BANNER;
            case BLACK -> Material.BLACK_BANNER;
            default -> throw new IllegalArgumentException("Unknown banner colour");
        };
    }
}
