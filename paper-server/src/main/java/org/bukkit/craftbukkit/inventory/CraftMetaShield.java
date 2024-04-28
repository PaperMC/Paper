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
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ShieldMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaShield extends CraftMetaItem implements ShieldMeta, BlockStateMeta {

    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> BASE_COLOR = new ItemMetaKeyType<>(DataComponents.BASE_COLOR, "Base", "base-color");

    // Paper start - general item meta fixes - decoupled base colour and patterns
    private @org.jetbrains.annotations.Nullable List<Pattern> patterns;
    private @org.jetbrains.annotations.Nullable DyeColor baseColor;

    // An empty pattern list is the same as the default on the Shield item, and will hence not be present in the data components of the stack.
    private boolean hasPatterns() {
        return this.patterns != null && !this.patterns.isEmpty();
    }
    // Paper end - general item meta fixes - decoupled base colour and patterns

    CraftMetaShield(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaShield craftMetaShield) {
            // Paper start - general item meta fixes - decoupled base colour and patterns
            if (craftMetaShield.patterns != null) this.patterns = new ArrayList<>(craftMetaShield.getPatterns());
            if (craftMetaShield.baseColor != null) this.baseColor = craftMetaShield.baseColor;
            // Paper end - general item meta fixes - decoupled base colour and patterns
        } else if (meta instanceof CraftMetaBlockState state && state.hasBlockState() && state.getBlockState() instanceof Banner banner) {
            // Paper start - general item meta fixes - decoupled base colour and patterns
            this.patterns = banner.getPatterns();
            this.baseColor = banner.getBaseColor();
            // Paper end - general item meta fixes - decoupled base colour and patterns
        }
    }

    CraftMetaShield(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper - improve checking handled tags in item meta
        super(tag, extraHandledDcts); // Paper - improve checking handled tags in item meta

        getOrEmpty(tag, CraftMetaShield.BASE_COLOR).ifPresent((color) -> {
            this.baseColor = DyeColor.getByWoolData((byte) color.getId()); // Paper - general item meta fixes - decoupled base colour and patterns
        });

        getOrEmpty(tag, CraftMetaBanner.PATTERNS).ifPresent((entityTag) -> {
            List<BannerPatternLayers.Layer> patterns = entityTag.layers();
            for (int i = 0; i < Math.min(patterns.size(), 20); i++) {
                BannerPatternLayers.Layer p = patterns.get(i);
                DyeColor color = DyeColor.getByWoolData((byte) p.color().getId());
                PatternType pattern = CraftRegistry.unwrapAndConvertHolder(io.papermc.paper.registry.RegistryKey.BANNER_PATTERN, p.pattern()).orElse(null); // Paper - fix upstream not being correct

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
            this.baseColor = DyeColor.valueOf(baseColor); // Paper - general item meta fixes - decoupled base colour and patterns
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

        // Paper start - general item meta fixes - decoupled base colour and patterns
        if (this.baseColor != null) tag.put(CraftMetaShield.BASE_COLOR, net.minecraft.world.item.DyeColor.byId(this.baseColor.getWoolData()));
        if (this.patterns != null && !this.patterns.isEmpty()) {
            {
        // Paper end - general item meta fixes - decoupled base colour and patterns
                List<BannerPatternLayers.Layer> newPatterns = new ArrayList<>();

                for (Pattern p : this.patterns) { // Paper - general item meta fixes - decoupled base colour and patterns
                    newPatterns.add(new BannerPatternLayers.Layer(CraftPatternType.bukkitToMinecraftHolder(p.getPattern()), net.minecraft.world.item.DyeColor.byId(p.getColor().getWoolData())));
                }

                tag.put(CraftMetaBanner.PATTERNS, new BannerPatternLayers(newPatterns));
            }
        }
    }

    @Override
    public List<Pattern> getPatterns() {
        if (this.patterns == null) { // Paper - general item meta fixes - decoupled base colour and patterns
            return new ArrayList<>();
        }

        return new ArrayList<>(this.patterns); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<>(patterns); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public void addPattern(Pattern pattern) {
        // Paper start - general item meta fixes - decoupled base colour and patterns
        if (this.patterns == null) this.patterns = new ArrayList<>();
        this.patterns.add(pattern);
        // Paper end - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public Pattern getPattern(int i) {
        if (this.patterns == null) { // Paper - general item meta fixes - decoupled base colour and patterns
            throw new IndexOutOfBoundsException(i);
        }

        return this.patterns.get(i); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public Pattern removePattern(int i) {
        if (this.patterns == null) { // Paper - general item meta fixes - decoupled base colour and patterns
            throw new IndexOutOfBoundsException(i);
        }

        return this.patterns.remove(i); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        if (this.patterns == null) { // Paper - general item meta fixes - decoupled base colour and patterns
            throw new IndexOutOfBoundsException(i);
        }

        this.patterns.set(i, pattern); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public int numberOfPatterns() {
        if (this.patterns == null) { // Paper - general item meta fixes - decoupled base colour and patterns
            return 0;
        }

        return this.patterns.size(); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public DyeColor getBaseColor() {
        return this.baseColor; // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public void setBaseColor(DyeColor baseColor) {
        this.baseColor = baseColor; // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        // Paper start - general item meta fixes - decoupled base colour and patterns
        if (this.baseColor != null) {
            builder.put(CraftMetaShield.BASE_COLOR.BUKKIT, this.baseColor.toString());
        }
        if (hasPatterns()) {
            builder.put(CraftMetaBanner.PATTERNS.BUKKIT, this.patterns);
        }
        // Paper end - general item meta fixes - decoupled base colour and patterns

        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        // Paper start - general item meta fixes - decoupled base colour and patterns
        if (this.baseColor != null) {
            hash = 61 * hash + this.baseColor.hashCode();
        }
        if (hasPatterns()) {
            hash = 61 * hash + this.patterns.hashCode();
        // Paper end - general item meta fixes - decoupled base colour and patterns
        }
        return original != hash ? CraftMetaShield.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaShield that) {
            return Objects.equal(this.baseColor, that.baseColor) && Objects.equal(this.patterns, that.patterns); // Paper - general item meta fixes - decoupled base colour and patterns
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaShield || (this.baseColor == null && !hasPatterns())); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.baseColor == null && !hasPatterns(); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public boolean hasBlockState() {
        return this.baseColor != null || hasPatterns(); // Paper - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public BlockState getBlockState() {
        // Paper start - general item meta fixes - decoupled base colour and patterns
        final Banner banner = CraftMetaShield.getBlockState(this.baseColor);
        if (this.patterns != null) banner.setPatterns(this.patterns);
        return banner;
        // Paper end - general item meta fixes - decoupled base colour and patterns
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Preconditions.checkArgument(blockState != null, "blockState must not be null");
        Preconditions.checkArgument(blockState instanceof Banner, "Invalid blockState");

        // Paper start - general item meta fixes - decoupled base colour and patterns
        final Banner banner = (Banner) blockState;
        this.baseColor = banner.getBaseColor();
        this.patterns = banner.getPatterns();
        // Paper end - general item meta fixes - decoupled base colour and patterns
    }

    // Paper start - add method to clear block state
    @Override
    public void clearBlockState() {
        this.baseColor = null;
        this.patterns = null;
    }
    // Paper end - add method to clear block state

    private static Banner getBlockState(DyeColor color) {
        BlockPos pos = BlockPos.ZERO;
        Material stateMaterial = CraftMetaShield.shieldToBannerHack(color);

        return (Banner) CraftBlockStates.getBlockState(pos, stateMaterial, null);
    }

    @Override
    public CraftMetaShield clone() {
        CraftMetaShield meta = (CraftMetaShield) super.clone();
        // Paper start - general item meta fixes - decoupled base colour and patterns
        meta.baseColor = this.baseColor;
        meta.patterns = this.patterns == null ? null : new ArrayList<>(this.patterns);
        // Paper start - general item meta fixes - decoupled base colour and patterns
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
