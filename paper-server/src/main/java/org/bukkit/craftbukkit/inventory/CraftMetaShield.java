package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
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

    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> BASE_COLOR = new ItemMetaKeyType<>(DataComponents.BASE_COLOR, "Base", "base-color");

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

        getOrEmpty(tag, CraftMetaShield.BASE_COLOR).ifPresent((color) -> {
            this.banner = CraftMetaShield.getBlockState(DyeColor.getByWoolData((byte) color.getId()));
        });

        getOrEmpty(tag, CraftMetaBanner.PATTERNS).ifPresent((entityTag) -> {
            List<BannerPatternLayers.Layer> patterns = entityTag.layers();
            for (int i = 0; i < Math.min(patterns.size(), 20); i++) {
                BannerPatternLayers.Layer p = patterns.get(i);
                DyeColor color = DyeColor.getByWoolData((byte) p.color().getId());
                PatternType pattern = CraftPatternType.minecraftHolderToBukkit(p.pattern());

                if (color != null && pattern != null) {
                    this.addPattern(new Pattern(color, pattern));
                }
            }
        });
    }

    CraftMetaShield(Map<String, Object> map) {
        super(map);

        String baseColor = SerializableMeta.getString(map, CraftMetaShield.BASE_COLOR.BUKKIT, true);
        if (baseColor != null) {
            this.banner = CraftMetaShield.getBlockState(DyeColor.valueOf(baseColor));
        }

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

        if (this.banner != null) {
            tag.put(CraftMetaShield.BASE_COLOR, net.minecraft.world.item.DyeColor.byId(this.banner.getBaseColor().getWoolData()));

            if (this.banner.numberOfPatterns() > 0) {
                List<BannerPatternLayers.Layer> newPatterns = new ArrayList<>();

                for (Pattern p : this.banner.getPatterns()) {
                    newPatterns.add(new BannerPatternLayers.Layer(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), net.minecraft.world.item.DyeColor.byId(p.getColor().getWoolData())));
                }

                tag.put(CraftMetaBanner.PATTERNS, new BannerPatternLayers(newPatterns));
            }
        }
    }

    @Override
    public List<Pattern> getPatterns() {
        if (this.banner == null) {
            return new ArrayList<>();
        }

        return this.banner.getPatterns();
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        if (this.banner == null) {
            if (patterns.isEmpty()) {
                return;
            }

            this.banner = CraftMetaShield.getBlockState(null);
        }

        this.banner.setPatterns(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        if (this.banner == null) {
            this.banner = CraftMetaShield.getBlockState(null);
        }

        this.banner.addPattern(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        if (this.banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        return this.banner.getPattern(i);
    }

    @Override
    public Pattern removePattern(int i) {
        if (this.banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        return this.banner.removePattern(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        if (this.banner == null) {
            throw new IndexOutOfBoundsException(i);
        }

        this.banner.setPattern(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        if (this.banner == null) {
            return 0;
        }

        return this.banner.numberOfPatterns();
    }

    @Override
    public DyeColor getBaseColor() {
        if (this.banner == null) {
            return null;
        }

        return this.banner.getBaseColor();
    }

    @Override
    public void setBaseColor(DyeColor baseColor) {
        if (baseColor == null) {
            if (this.banner.numberOfPatterns() > 0) {
                this.banner.setBaseColor(DyeColor.WHITE);
            } else {
                this.banner = null;
            }
        } else {
            if (this.banner == null) {
                this.banner = CraftMetaShield.getBlockState(baseColor);
            }

            this.banner.setBaseColor(baseColor);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.banner != null) {
            builder.put(CraftMetaShield.BASE_COLOR.BUKKIT, this.banner.getBaseColor().toString());

            if (this.banner.numberOfPatterns() > 0) {
                builder.put(CraftMetaBanner.PATTERNS.BUKKIT, this.banner.getPatterns());
            }
        }

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.banner != null) {
            hash = 61 * hash + this.banner.hashCode();
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
        return this.banner != null;
    }

    @Override
    public BlockState getBlockState() {
        return (this.banner != null) ? this.banner.copy() : CraftMetaShield.getBlockState(null);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");
        Preconditions.checkArgument(blockState instanceof Banner, "Invalid blockState");

        this.banner = (Banner) blockState;
    }

    private static Banner getBlockState(DyeColor color) {
        BlockPos pos = BlockPos.ZERO;
        Material stateMaterial = CraftMetaShield.shieldToBannerHack(color);

        return (Banner) CraftBlockStates.getBlockState(pos, stateMaterial, null);
    }

    @Override
    public CraftMetaShield clone() {
        CraftMetaShield meta = (CraftMetaShield) super.clone();
        if (this.banner != null) {
            meta.banner = (Banner) this.banner.copy();
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
