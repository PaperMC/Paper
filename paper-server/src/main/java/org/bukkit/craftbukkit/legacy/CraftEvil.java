package org.bukkit.craftbukkit.legacy;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * @deprecated do not use for any reason
 */
@Deprecated
public final class CraftEvil {

    private static final Int2ObjectMap<Material> byId = new Int2ObjectLinkedOpenHashMap<>();

    static {
        for (Material material : Material.values()) {
            if (!material.isLegacy()) {
                continue;
            }

            Preconditions.checkState(!byId.containsKey(material.getId()), "Duplicate material ID for", material);
            byId.put(material.getId(), material);
        }
    }

    private CraftEvil() {
    }

    public static void setDurability(ItemStack itemStack, short durability) {
        itemStack.setDurability(durability);
        MaterialData materialData = CraftLegacy.toLegacyData(itemStack.getType(), true);

        if (materialData.getItemType().getMaxDurability() <= 0) {
            itemStack.setType(CraftLegacy.fromLegacy(new MaterialData(materialData.getItemType(), (byte) itemStack.getDurability()), true));
        }
    }

    public static int getBlockTypeIdAt(World world, int x, int y, int z) {
        return CraftEvil.getId(world.getBlockAt(x, y, z).getType());
    }

    public static int getBlockTypeIdAt(World world, Location location) {
        return CraftEvil.getId(world.getBlockAt(location).getType());
    }

    public static int getTypeId(Block block) {
        return CraftEvil.getId(block.getType());
    }

    public static boolean setTypeId(Block block, int type) {
        block.setType(CraftEvil.getMaterial(type));
        return true;
    }

    public static boolean setTypeId(Block block, int type, boolean applyPhysics) {
        block.setType(CraftEvil.getMaterial(type), applyPhysics);
        return true;
    }

    public static boolean setTypeIdAndData(Block block, int type, byte data, boolean applyPhysics) {
        block.setType(CraftEvil.getMaterial(type), applyPhysics);
        CraftEvil.setData(block, data);
        return true;
    }

    public static void setData(Block block, byte data) {
        ((CraftBlock) block).setData(data);
    }

    public static void setData(Block block, byte data, boolean applyPhysics) {
        ((CraftBlock) block).setData(data, applyPhysics);
    }

    public static int getTypeId(BlockState state) {
        return CraftEvil.getId(state.getType());
    }

    public static boolean setTypeId(BlockState state, int type) {
        state.setType(CraftEvil.getMaterial(type));
        return true;
    }

    public static int getTypeId(ItemStack stack) {
        return CraftEvil.getId(stack.getType());
    }

    public static void setTypeId(ItemStack stack, int type) {
        stack.setType(CraftEvil.getMaterial(type));
    }

    public static Material getMaterial(int id) {
        return CraftEvil.byId.get(id);
    }

    public static int getId(Material material) {
        return CraftLegacy.toLegacy(material).getId();
    }

    public static Class<?> getDataType(Particle particle) {
        Class<?> clazz = particle.getDataType();

        if (clazz == BlockData.class) {
            return MaterialData.class;
        }

        return clazz;
    }
}
