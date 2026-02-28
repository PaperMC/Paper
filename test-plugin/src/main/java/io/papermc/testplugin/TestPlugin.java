package io.papermc.testplugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        System.out.println(PotionType.AWKWARD.ordinal());
        System.out.println(PotionType.LONG_FIRE_RESISTANCE.ordinal());
        System.out.println(PotionType.LONG_FIRE_RESISTANCE.compareTo(PotionType.FIRE_RESISTANCE));
        System.out.println(PotionType.values());
        System.out.println(PotionType.valueOf("AWKWARD"));
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
