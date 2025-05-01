package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.inventory.ItemStack;
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
    public void setItem(final ItemStack item) {
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");
        final PotionMeta meta = item.getType() == Material.LINGERING_POTION ? null : this.getPotionMeta();
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
        if (meta != null) {
            this.setPotionMeta(meta);
        }
    }

    @Override
    public PotionMeta getPotionMeta() {
        return (PotionMeta) CraftItemStack.getItemMeta(this.getHandle().getItem(), ItemType.LINGERING_POTION);
    }
}
