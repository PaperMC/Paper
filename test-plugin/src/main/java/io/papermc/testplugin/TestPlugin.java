package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.event.world.StructuresLocateEvent;
import java.util.function.BiFunction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.generator.structure.Structure;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.test(new Location(null, 0, 0, 0));
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        this.test(event.getPlayer().getLocation());
    }

    private void test(Location loc) {
        final BiFunction<Location, Structure, StructuresLocateEvent.Result> biFunction = StructuresLocateEvent.Result::new;
        final StructuresLocateEvent.Result resultByConstructor = new StructuresLocateEvent.Result(loc, Structure.FORTRESS);
        System.out.println(resultByConstructor.position());
        final StructuresLocateEvent.Result result = biFunction.apply(loc, Structure.FORTRESS);
        System.out.println(result.position());
    }
}
