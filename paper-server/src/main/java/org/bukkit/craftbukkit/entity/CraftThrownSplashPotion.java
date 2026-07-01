package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.SplashPotion;
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
    public PotionMeta getPotionMeta() {
        return (PotionMeta) CraftItemStack.getItemMeta(this.getHandle().getItem(), ItemType.SPLASH_POTION);
    }
}
