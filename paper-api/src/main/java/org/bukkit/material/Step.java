package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

/**
 * Represents the different types of steps.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Step extends TexturedMaterial {
    private static final List<Material> textures = new ArrayList<Material>();
    static {
        textures.add(Material.LEGACY_STONE);
        textures.add(Material.LEGACY_SANDSTONE);
        textures.add(Material.LEGACY_WOOD);
        textures.add(Material.LEGACY_COBBLESTONE);
        textures.add(Material.LEGACY_BRICK);
        textures.add(Material.LEGACY_SMOOTH_BRICK);
        textures.add(Material.LEGACY_NETHER_BRICK);
        textures.add(Material.LEGACY_QUARTZ_BLOCK);
    }

    public Step() {
        super(Material.LEGACY_STEP);
    }

    public Step(final Material type) {
        super((textures.contains(type)) ? Material.LEGACY_STEP : type);
        if (textures.contains(type)) {
            setMaterial(type);
        }
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Step(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public List<Material> getTextures() {
        return textures;
    }

    /**
     * Test if step is inverted
     *
     * @return true if inverted (top half), false if normal (bottom half)
     */
    public boolean isInverted() {
        return ((getData() & 0x8) != 0);
    }

    /**
     * Set step inverted state
     *
     * @param inv - true if step is inverted (top half), false if step is
     *     normal (bottom half)
     */
    public void setInverted(boolean inv) {
        int dat = getData() & 0x7;
        if (inv) {
            dat |= 0x8;
        }
        setData((byte) dat);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Magic value
     */
    @Override
    protected int getTextureIndex() {
        return getData() & 0x7;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Magic value
     */
    @Deprecated
    @Override
    protected void setTextureIndex(int idx) {
        setData((byte) ((getData() & 0x8) | idx));
    }

    @Override
    public Step clone() {
        return (Step) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + (isInverted() ? "inverted" : "");
    }
}
