package io.papermc.paper.testplugin;

import io.leangen.geantyref.TypeToken;
import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.activity.ActivityKey;
import io.papermc.paper.entity.brain.sensor.SensorKey;
import io.papermc.paper.testplugin.sensors.NearbyPicklesSensor;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;


public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public static final MemoryKey<Location> PICKLE_TARGET = Bukkit.registerMemoryKey(of("pickle_target"), new TypeToken<>() {}, null); // Don't serialize

    public static final MemoryKey<Boolean> GUARDING_PICKLE = Bukkit.registerMemoryKey(of("guarding_pickle"), TypeToken.get(Boolean.class), null);
    public static final MemoryKey<Player> PICKLE_INTRUDER  = Bukkit.registerMemoryKey(of("pickle_intruder"), TypeToken.get(Player.class), null);

    public static final ActivityKey SUPER_SQUID_IDLE = Bukkit.createActivityKey(of("super_squid_idle"));
    public static final ActivityKey SUPER_SQUID_CORE = Bukkit.createActivityKey(of("super_squid_core"));
    public static final ActivityKey SUPER_SQUID_PROTECTING = Bukkit.createActivityKey(of("super_squid_attack"));

    // Sensors are responsible for updating memories usually
    private static final SensorKey NEARBY_PICKLES_SENSOR = Bukkit.createSensorKey(of("nearby_pickles"));
    private static final SensorKey INTRUDER_SENSOR = Bukkit.createSensorKey(of("intruder"));

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        BrainHolder<LivingEntity> brainHolder = ((LivingEntity) event.getRightClicked());
        System.out.println("Activities: ");
        for (var activities : brainHolder.getPrioritizedActivities().entrySet()) {
            debug("Priority: " + activities.getKey(), activities.getValue());
        }
        debug("Core Activities", brainHolder.getCoreActivities());
        debug("Active Activities", brainHolder.getActiveActivities());
        System.out.println("Sensors: " + brainHolder.getSensors());
        debug("Memories", brainHolder.getMemories());

        if (event.getPlayer().isSneaking()) {
            return;
        }

        // Clear all the default minecraft behaviors inorder to give us all the control.
        brainHolder.unregisterActivities();
        brainHolder.unregisterMemories();
        brainHolder.unregisterSensors();
        Bukkit.getMobGoals().removeAllGoals((Mob) brainHolder); // Remove all goals

        brainHolder.registerMemory(PICKLE_TARGET); // Register our custom memory
        brainHolder.setActivityQueue(List.of(SUPER_SQUID_PROTECTING, SUPER_SQUID_IDLE)); // Force these activities to be prioritized each tick
        brainHolder.setDefaultActivity(SUPER_SQUID_IDLE); // Set idle
        brainHolder.setCoreActivities(List.of(SUPER_SQUID_CORE)); // Set core activity that
        brainHolder.forceTickBrain(true);

        brainHolder.addSensor(NEARBY_PICKLES_SENSOR, new NearbyPicklesSensor()); // Add pickle sensor

        manager.addActivity(brainHolder, VanillaActivityKey.IDLE, 1, List.of(new HuntSquidsBehavior(), new SpinBehavior()));
        manager.addActivity(brainHolder, VanillaActivityKey.CORE, 1, List.of(new SniffSquidsBehavior(), new ScreamAtParrotsBehavior()));

        manager.addSensor(brainHolder, SNIFF_SQUID_SENSOR, new ClosestSquidsSensor()); // Add the scary mob finder sensor
        manager.addSensor(brainHolder, PARROT_SENSOR, new ClosestParrotSensor());

        manager.setDefaultActivity(brainHolder, VanillaActivityKey.IDLE); // If no activities at this moment can activate, it goes to default.
        manager.setCoreActivities(brainHolder, List.of(VanillaActivityKey.CORE)); // This activity is ALWAYS active during other activities

    }

    private static void debug(String name, Collection<? extends Keyed> collection) {
        System.out.println(name + ":");
        for (Keyed keyed : collection) {
            if (keyed == null) {
                continue;
            }
            System.out.println(keyed.getKey());
        }
    }

    private static void debugKey(String name, Collection<? extends NamespacedKey> keyCollection) {
        System.out.println(name + ":");
        System.out.println(keyCollection);
    }

    private static NamespacedKey of(String name) {
        return new NamespacedKey("example", name);
    }
}
