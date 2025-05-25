package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.SplashPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.PotionMeta;

public class CraftThrownSplashPotion extends CraftThrownPotion implements SplashPotion {

    public CraftThrownSplashPotion(final CraftServer server, final ThrownSplashPotion entity) {
        super(server, entity);
    }

    @Override
    public ThrownSplashPotion getHandle() {
        return (ThrownSplashPotion) this.entity;
    }

    @Override
    public void setItem(final ItemStack item) {
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");
        final PotionMeta meta = item.getType() == Material.SPLASH_POTION ? null : this.getPotionMeta();
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
        if (meta != null) {
            this.setPotionMeta(meta);
        }
    }

    @Override
    public PotionMeta getPotionMeta() {
        return (PotionMeta) CraftItemStack.getItemMeta(this.getHandle().getItem(), ItemType.SPLASH_POTION);
    }
}
