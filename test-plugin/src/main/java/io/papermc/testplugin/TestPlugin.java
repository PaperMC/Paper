package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.GameEvent;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.test();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        this.test();
    }

    void test() {
        final Function<NamespacedKey, Keyed> GET_KEYED = Registry.BIOME::get;
        final BiFunction<Registry<?>, NamespacedKey, Keyed> GET_KEYED_2 = Registry::get;

        final Keyed keyed = GET_KEYED.apply(Biome.PLAINS.getKey());
        System.out.println(keyed.getKey() + " " + keyed.getClass().getSimpleName());
        final Keyed keyed2 = GET_KEYED_2.apply(Registry.GAME_EVENT, GameEvent.EAT.getKey());
        System.out.println(keyed2.getKey() + " " + keyed2.getClass().getSimpleName());

        final Keyed keyed3 = Registry.BIOME.get(Biome.BEACH.getKey());
        System.out.println(keyed3.getKey() + " " + keyed3.getClass().getSimpleName());

        final Keyed keyed4 = Registry.ENCHANTMENT.get(Enchantment.LUCK.getKey());
        System.out.println(keyed4.getKey() + " " + keyed4.getClass().getSimpleName());
    }
}
