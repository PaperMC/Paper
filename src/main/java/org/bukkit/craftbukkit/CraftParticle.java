package org.bukkit.craftbukkit;

import net.minecraft.server.Block;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class CraftParticle {

    public static EnumParticle toNMS(Particle bukkit) {
        return EnumParticle.valueOf(bukkit.name());
    }

    public static Particle toBukkit(EnumParticle nms) {
        return Particle.valueOf(nms.name());
    }

    public static int[] toData(Particle particle, Object obj) {
        if (particle.getDataType().equals(Void.class)) {
            return new int[0];
        }
        if (particle.getDataType().equals(ItemStack.class)) {
            if (obj == null) {
                return new int[]{0, 0};
            }
            ItemStack itemStack = (ItemStack) obj;
            return new int[]{itemStack.getType().getId(), itemStack.getDurability()};
        }
        if (particle.getDataType().equals(MaterialData.class)) {
            if (obj == null) {
                return new int[]{0};
            }
            MaterialData data = (MaterialData) obj;
            return new int[]{data.getItemTypeId() + ((int)(data.getData()) << 12)};
        }
        throw new IllegalArgumentException(particle.getDataType().toString());
    }
}
