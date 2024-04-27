package io.papermc.testplugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.Minecart;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.memory.MemoryKey;
import java.util.Collections;
import io.papermc.paper.registry.keys.ActivityKeys;
import org.bukkit.Registry;
import org.bukkit.NamespacedKey;
import java.util.Map;
import io.papermc.paper.registry.keys.SensorTypeKeys;

public final class TestPlugin extends JavaPlugin implements Listener {
    public class SnifferCommand extends org.bukkit.command.Command {
        public SnifferCommand() {
            super("sniffer");
        }
        public boolean execute(CommandSender sender, String label, String []params) {
            var player = (Player)sender;

            var sniffer = (Sniffer)player.getWorld()
                .spawnEntity(player.getLocation(), EntityType.SNIFFER);

            var target = player.getLocation();
            target.add(10, 0, 0);

            var server = Bukkit.getServer();
            var nearestLivingEntitiesKey = SensorTypeKeys.NEAREST_LIVING_ENTITIES.key();
            var nearestLivingEntities = Registry.SENSOR_TYPE.get(new NamespacedKey(nearestLivingEntitiesKey.namespace(), nearestLivingEntitiesKey.value()));
            var hurtByKey = SensorTypeKeys.HURT_BY.key();
            var hurtBy = Registry.SENSOR_TYPE.get(new NamespacedKey(hurtByKey.namespace(), hurtByKey.value()));
            var nearestPlayersKey = SensorTypeKeys.NEAREST_PLAYERS.key();
            var nearestPlayers = Registry.SENSOR_TYPE.get(new NamespacedKey(nearestPlayersKey.namespace(), nearestPlayersKey.value()));
            var snifferTemptationsKey = SensorTypeKeys.SNIFFER_TEMPTATIONS.key();
            var snifferTemptations = Registry.SENSOR_TYPE.get(new NamespacedKey(snifferTemptationsKey.namespace(), snifferTemptationsKey.value()));
            var core = Registry.SENSOR_TYPE.get(new NamespacedKey(ActivityKeys.CORE.key().namespace(), ActivityKeys.CORE.key().value()));
            // var core = Registry.ACTIVITY.get(new NamespacedKey(ActivityKeys.CORE.key().namespace(), ActivityKeys.CORE.key().value()));
            // var idle = Registry.ACTIVITY.get(new NamespacedKey(ActivityKeys.IDLE.key().namespace(), ActivityKeys.IDLE.key().value()));
            var brain = Bukkit.getServer().getBrain(sniffer);
            var tasks = server.getTasks();
            // brain.setWalkTarget(target, 1.25F, 0);
            // brain.clearActivities();
            brain.setSensors(List.of(nearestLivingEntities, hurtBy, nearestPlayers, snifferTemptations));
            // brain.setTasksForActivity(core, 0, List.of(
            //     tasks.swimIfInWater(0.8f),
            //     tasks.panicOnDamage(9.0f),
            //     tasks.setLookTarget(entity -> entity.getVehicle() instanceof Minecart, 30f),
            //     tasks.setWalkTargetToLookTarget(entity -> true, entity -> 9.0f, 3)
            // ));
            // brain.setTasksForActivity(idle, 0, List.of(
            //     tasks.lookAtLookTarget(150, 250),
            //     tasks.walkToWalkTarget(150, 250)
            // ));
            // brain.useActivityIfPossible(idle);

            // sender.sendMessage(brain.isActive(idle) ? "Yes!" : "no no no");

            return true;
        }
    }
    @Override
    public void onEnable() {
        this.getServer().getCommandMap().register("sniffer", new SnifferCommand());
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

}
