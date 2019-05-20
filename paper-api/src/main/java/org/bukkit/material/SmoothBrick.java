package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

/**
 * Represents the different types of smooth bricks.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class SmoothBrick extends TexturedMaterial {

    private static final List<Material> textures = new ArrayList<Material>();
    static {
        textures.add(Material.LEGACY_STONE);
        textures.add(Material.LEGACY_MOSSY_COBBLESTONE);
        textures.add(Material.LEGACY_COBBLESTONE);
        textures.add(Material.LEGACY_SMOOTH_BRICK);
    }

    public SmoothBrick() {
        super(Material.LEGACY_SMOOTH_BRICK);
    }

    public SmoothBrick(final Material type) {
        super((textures.contains(type)) ? Material.LEGACY_SMOOTH_BRICK : type);
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
    public SmoothBrick(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public List<Material> getTextures() {
        return textures;
    }

    @Override
    public SmoothBrick clone() {
        return (SmoothBrick) super.clone();
    }
}
