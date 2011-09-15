package org.bukkit.material;

import java.util.List;

import org.bukkit.Material;

/**
 * Represents textured materials like steps and smooth bricks
 */
public abstract class TexturedMaterial extends MaterialData {

    public TexturedMaterial(Material m) {
        super(m);
    }

    public TexturedMaterial(int type) {
        super(type);
    }

    public TexturedMaterial(final int type, final byte data) {
        super(type, data);
    }

    public TexturedMaterial(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Retrieve a list of possible textures. The first element of the list will be used as a default.
     * 
     * @return a list of possible textures for this block
     */
    public abstract List<Material> getTextures();

    /**
     * Gets the current Material this block is made of
     * 
     * @return Material of this block
     */
    public Material getMaterial() {
        int n = (int) getData();
        if (n > getTextures().size() - 1) {
            n = 0;
        }
        
        return getTextures().get(n);
    }

    /**
     * Sets the material this block is made of
     * 
     * @param material
     *            New material of this block
     */
    public void setMaterial(Material material) {
        if (getTextures().contains(material)) {
            setData((byte) getTextures().indexOf(material));
        } else {
            setData((byte) 0x0);
        }
    }

    @Override
    public String toString() {
        return getMaterial() + " " + super.toString();
    }

}
