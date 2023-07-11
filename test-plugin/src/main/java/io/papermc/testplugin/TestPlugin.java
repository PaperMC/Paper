package io.papermc.testplugin;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.inventory.item.properties.ItemProperties;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void swapHands(PlayerSwapHandItemsEvent event) {
        {
            ItemStack itemStack = Bukkit.getUnsafe().newItem(Material.PLAYER_HEAD, 1);

            PlayerProfile profile = Bukkit.createProfile("Owen1212055");
            profile.complete();
            itemStack.getPropertyHolder().set(ItemProperties.SKULL_OWNER, profile);
            itemStack.getPropertyHolder().getOrCreate(ItemProperties.DISPLAY).set(ItemProperties.DISPLAY_NAME, Component.text("GET REAL!"));

            event.getPlayer().getInventory().addItem(itemStack);
        }
        {
            ItemStack itemStack = new ItemStack(Material.SUSPICIOUS_STEW, 1);
            itemStack.editMeta(meta -> {
                SuspiciousStewMeta stewMeta = (SuspiciousStewMeta) meta;
                stewMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20, 1, true, true), true);
            });

            event.getPlayer().getInventory().addItem(itemStack);
        }
    }
}
