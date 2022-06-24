package io.papermc.paper.testplugin;

import io.leangen.geantyref.TypeToken;
import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.activity.ActivityKey;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.entity.brain.sensor.SensorKey;
import io.papermc.paper.testplugin.behaviors.attack.AttackTarget;
import io.papermc.paper.testplugin.behaviors.attack.FollowTarget;
import io.papermc.paper.testplugin.behaviors.core.LookAtTarget;
import io.papermc.paper.testplugin.behaviors.core.PlayFunSound;
import io.papermc.paper.testplugin.behaviors.core.SparkleAroundSelf;
import io.papermc.paper.testplugin.behaviors.guard.GuardPickle;
import io.papermc.paper.testplugin.behaviors.guard.InkParticleGuardPickleDefense;
import io.papermc.paper.testplugin.behaviors.idle.HoverAroundPickle;
import io.papermc.paper.testplugin.behaviors.idle.SparkleAroundPickle;
import io.papermc.paper.testplugin.behaviors.pickleifying.MoveToPickle;
import io.papermc.paper.testplugin.behaviors.pickleifying.Pickleify;
import io.papermc.paper.testplugin.behaviors.pickleifying.PrePickleify;
import io.papermc.paper.testplugin.datatypes.BooleanDataType;
import io.papermc.paper.testplugin.datatypes.LocationDataType;
import io.papermc.paper.testplugin.sensors.AttackingEntitySensor;
import io.papermc.paper.testplugin.sensors.NearbyIntruderSensor;
import io.papermc.paper.testplugin.sensors.NearbyPicklesSensor;
import io.papermc.paper.testplugin.sensors.NearbyPlayerSensor;
import io.papermc.paper.testplugin.sensors.PickleifySensor;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }


    // "Generic" memories
    /*
    These memories could be used by other entities, and are generally not specific to the entity
    that is using them.

    sensors have the behavior of registering their own memories, so these might be
    able to be registered by their corresponding sensor. However, this is optional and
    can instead be registered manually.
    TODO: Should we even allow sensors to register memories? Should we just make them be registered manually?
     */
    public static final MemoryKey<Player> NEARBY_PLAYER = Bukkit.registerMemoryKey(of("nearby_player"), TypeToken.get(Player.class), null);
    public static final MemoryKey<Entity> ENTITY_TARGET = Bukkit.registerMemoryKey(of("entity_target"), TypeToken.get(Entity.class), null);

    // Specific memories
    public static final MemoryKey<Location> PICKLE_TARGET = Bukkit.registerMemoryKey(of("pickle_target"), TypeToken.get(Location.class), new LocationDataType()); // Don't serialize
    public static final MemoryKey<Location> PRE_PICKLIFY_TARGET = Bukkit.registerMemoryKey(of("pre_picklify_target"), TypeToken.get(Location.class), new LocationDataType()); // Don't serialize
    public static final MemoryKey<Boolean> GUARDING_PICKLE = Bukkit.registerMemoryKey(of("guarding_pickle"), TypeToken.get(Boolean.class), new BooleanDataType()); // TODO: UNIT?


    public static final MemoryKey<Location> PICKLEIFY_TARGET = Bukkit.registerMemoryKey(of("pickleify_target"), TypeToken.get(Location.class), new LocationDataType()); // Don't serialize
    public static final MemoryKey<Boolean> PICKLEIFYING = Bukkit.registerMemoryKey(of("pickleifying"), TypeToken.get(Boolean.class), new BooleanDataType()); // Don't serialize

    public static final ActivityKey SUPER_SQUID_IDLE = Bukkit.createActivityKey(of("super_squid_idle"));
    public static final ActivityKey SUPER_SQUID_CORE = Bukkit.createActivityKey(of("super_squid_core"));
    public static final ActivityKey SUPER_SQUID_FIGHT = Bukkit.createActivityKey(of("super_squid_fight"));
    public static final ActivityKey SUPER_SQUID_GUARD = Bukkit.createActivityKey(of("super_squid_guard"));
    public static final ActivityKey SUPER_SQUID_PICKLIFY = Bukkit.createActivityKey(of("super_squid_pickleify"));

    // Sensors are responsible for updating memories usually
    private static final SensorKey NEARBY_PICKLES_SENSOR = Bukkit.createSensorKey(of("nearby_pickles"));
    private static final SensorKey NEARBY_PLAYERS_SENSOR = Bukkit.createSensorKey(of("nearby_players"));
    private static final SensorKey INTRUDER_SENSOR = Bukkit.createSensorKey(of("intruder"));
    private static final SensorKey ATTACKER_SENSOR = Bukkit.createSensorKey(of("attacker_sensor"));
    private static final SensorKey PICKLEIFYING_SENSOR = Bukkit.createSensorKey(of("pickleify_sensor"));


    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Mob mob)) {
            return;
        }
        BrainHolder<Mob> brainHolder = ((BrainHolder<Mob>) event.getRightClicked()); // TODO: How should we fix this?
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
        Bukkit.getMobGoals().removeAllGoals(mob); // Remove all goals

        // Memories
        brainHolder.registerMemories(List.of(
                PICKLE_TARGET,
                NEARBY_PLAYER,
                ENTITY_TARGET,
                PICKLEIFY_TARGET,
                PICKLEIFYING,
                PRE_PICKLIFY_TARGET
            )
        ); // Register our custom memory
        brainHolder.setActivityQueue(List.of(SUPER_SQUID_PICKLIFY, SUPER_SQUID_FIGHT, SUPER_SQUID_GUARD, SUPER_SQUID_IDLE)); // Force these activities to be prioritized each tick
        brainHolder.setDefaultActivity(SUPER_SQUID_IDLE); // Set idle
        brainHolder.setCoreActivities(List.of(SUPER_SQUID_CORE)); // Set core activity
        brainHolder.forceTickBrain(true); // The brain must be force ticked because this entity normally doesn't tick their brain.

        brainHolder.addSensor(NEARBY_PICKLES_SENSOR, new NearbyPicklesSensor()); // Add pickle sensor
        brainHolder.addSensor(NEARBY_PLAYERS_SENSOR, new NearbyPlayerSensor()); // Update NEARBY_PLAYER to the first player in a 5x5x5 radius.
        brainHolder.addSensor(INTRUDER_SENSOR, new NearbyIntruderSensor()); // Update GUARDING_PICKLE if NEARBY_PLAYER is present and is holding a weapon
        brainHolder.addSensor(ATTACKER_SENSOR, new AttackingEntitySensor()); // Update ENTITY_TARGET with last attacked entity (neutral mob)
        brainHolder.addSensor(PICKLEIFYING_SENSOR, new PickleifySensor());

        brainHolder.addActivity(SUPER_SQUID_IDLE, 1, List.of(
            new SparkleAroundPickle(),
            new HoverAroundPickle()
        ));

        brainHolder.addActivity(SUPER_SQUID_CORE, 1, List.of(
            new SparkleAroundSelf(),
            new PlayFunSound(),
            new LookAtTarget()
        ));

        brainHolder.addActivity(SUPER_SQUID_GUARD, 1, List.of(
                new GuardPickle(),
                new InkParticleGuardPickleDefense()
            ),
            List.of(
                new MemoryPair(MemoryKeyStatus.ABSENT, TestPlugin.ENTITY_TARGET),
                new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.NEARBY_PLAYER),
                new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.GUARDING_PICKLE)
            )
        );

        brainHolder.addActivity(SUPER_SQUID_FIGHT, 1, List.of(
                new AttackTarget(),
                new FollowTarget()
            ),
            List.of(
                new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.ENTITY_TARGET)
            )
        );

        brainHolder.addActivity(SUPER_SQUID_PICKLIFY, 1, List.of(
                new Pickleify(),
                new PrePickleify(),
                new MoveToPickle()
            ),
            List.of(
                new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.PICKLEIFYING),
                new MemoryPair(MemoryKeyStatus.REGISTERED, TestPlugin.PICKLE_TARGET)
            )
        );
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
