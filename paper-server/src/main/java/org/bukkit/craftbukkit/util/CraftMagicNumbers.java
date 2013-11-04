package org.bukkit.craftbukkit.util;

import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.Item;
import org.bukkit.Material;

public final class CraftMagicNumbers {
    private CraftMagicNumbers() {}

    public static Block getBlock(org.bukkit.block.Block block) {
        return getBlock(block.getType());
    }

    @Deprecated
    // A bad method for bad magic.
    public static Block getBlock(int id) {
        return getBlock(Material.getMaterial(id));
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(Block block) {
        return Block.b(block);
    }

    public static Material getMaterial(Block block) {
        return Material.getMaterial(Block.b(block));
    }

    public static Item getItem(Material material) {
        // TODO: Don't use ID
        Item item = Item.d(material.getId());
        return item;
    }

    @Deprecated
    // A bad method for bad magic.
    public static Item getItem(int id) {
        return Item.d(id);
    }

    @Deprecated
    // A bad method for bad magic.
    public static int getId(Item item) {
        return Item.b(item);
    }

    public static Material getMaterial(Item item) {
        // TODO: Don't use ID
        Material material = Material.getMaterial(Item.b(item));

        if (material == null) {
            return Material.AIR;
        }

        return material;
    }

    public static Block getBlock(Material material) {
        // TODO: Don't use ID
        Block block = Block.e(material.getId());

        if (block == null) {
            return Blocks.AIR;
        }

        return block;
    }
}
