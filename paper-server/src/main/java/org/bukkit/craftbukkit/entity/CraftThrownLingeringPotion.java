package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.PotionMeta;

public class CraftThrownLingeringPotion extends CraftThrownPotion implements LingeringPotion {

    public CraftThrownLingeringPotion(final CraftServer server, final ThrownLingeringPotion entity) {
        super(server, entity);
    }

    @Override
    public ThrownLingeringPotion getHandle() {
        return (ThrownLingeringPotion) this.entity;
    }

    @Override
    public PotionMeta getPotionMeta() {
        return (PotionMeta) CraftItemStack.getItemMeta(this.getHandle().getItem(), ItemType.LINGERING_POTION);
    }
}
