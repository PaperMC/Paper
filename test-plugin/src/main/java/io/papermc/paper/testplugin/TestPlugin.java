package io.papermc.paper.testplugin;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.google.common.collect.ImmutableList;
import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.activity.ActivityKey;
import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.activity.behavior.BehaviorPair;
import io.papermc.paper.entity.brain.memory.CustomMemoryKey;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.entity.brain.sensor.SensorKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }



    static MemoryKey<Boolean> IS_CRINGE = new CustomMemoryKey<>(NamespacedKey.fromString("myplugin:cringe"), Boolean.class);
    static MemoryKey<Boolean> DANCING = new CustomMemoryKey<>(NamespacedKey.fromString("myplugin:dancing"), Boolean.class);

    static SensorKey DANCE_SENSOR = SensorKey.of(NamespacedKey.fromString("dancer_watcher"));

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Goat brainHolder) {

            System.out.println(brainHolder.getActivities());
            System.out.println(brainHolder.getCoreActivities());
            System.out.println(brainHolder.getActiveActivities());
            System.out.println(brainHolder.getAllSensors());
            System.out.println(brainHolder.getMemories());

            if (event.getPlayer().isSneaking()) {
                return;
            }

            System.out.println("CLEARING MEMORIES AND SUCH");
            brainHolder.clearActivities();
            brainHolder.clearMemories();
            brainHolder.clearSensors();

            // TODO: Debug null memory keys; there is alot of them

            ActivityKey danceActivity = ActivityKey.of(NamespacedKey.fromString("myplugin:dance"));



            brainHolder.registerMemory(DANCING);
            brainHolder.registerMemory(IS_CRINGE);

            brainHolder.addActivity(danceActivity,
                List.of(new BehaviorPair<>(1, new DanceBehavior())),
                List.of(new MemoryPair(MemoryKeyStatus.PRESENT, DANCING), new MemoryPair(MemoryKeyStatus.REGISTERED, IS_CRINGE)),
                List.of(DANCING, IS_CRINGE) // If cringe is true, the entity is no longer cringe after dancing
                );


            System.out.println("Adding Custom Sensor");

            brainHolder.addSensor(DANCE_SENSOR, new SquidSensor());
            brainHolder.setDefaultActivity(danceActivity);
            brainHolder.setCoreActivities(List.of(danceActivity));


            new BukkitRunnable(){

                @Override
                public void run() {
                    brainHolder.startActivity(danceActivity);
                }
            }.runTaskTimer(this, 1, 200);
        }


    }

    private static class SquidSensor implements Sensor {

        @Override
        public int getInterval() {
            return 10;
        }

        @Override
        public void tick(LivingEntity entity) {
            if (entity.getNearbyEntities(5, 5, 5).stream().anyMatch((e) -> e.getType() == EntityType.SQUID)) {
                entity.setMemory(DANCING, true);
            } else {
                entity.setMemory(DANCING, null);
            }

        }

        @Override
        public Set<MemoryKey<?>> requiredMemories() {
            return Set.of(DANCING);
        }
    }

    private static class DanceBehavior implements Behavior<Goat> {

        boolean baby = true;

        @Override
        public void start(Goat entity) {
            entity.setJumping(true);
        }

        @Override
        public void tick(Goat entity) {
            if (baby) {
                entity.setBaby();
            } else {
                entity.setAdult();
            }
            baby = !baby;
        }

        @Override
        public void stop(Goat entity) {
            entity.setJumping(false);
        }

        @Override
        public int getMinRuntime() {
            return 40;
        }

        @Override
        public int getMaxRuntime() {
            return 90;
        }

        @Override
        public Map<MemoryKey<?>, MemoryKeyStatus> getMemoryRequirements() {
            return Map.of(DANCING, MemoryKeyStatus.PRESENT);
        }
    }
}
