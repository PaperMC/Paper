package io.papermc.paper.testplugin.sensors;

import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class NearbyPicklesSensor implements Sensor<LivingEntity> {
    @Override
    public int getInterval() {
        return 20; // Update pickles every second
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        // Check to make sure the target pickle is still there
        if (entity.isMemoryStatus(TestPlugin.PICKLE_TARGET, MemoryKeyStatus.PRESENT)) {
           Location location = entity.getMemory(TestPlugin.PICKLE_TARGET);
           if (location.getBlock().getType() != Material.SEA_PICKLE) {
               entity.forgetMemory(TestPlugin.PICKLE_TARGET); // Forget the target if it isn't there!
           }
        }

        // Only update if the mob currently isn't guarding a pickle
        if (entity.isMemoryStatus(TestPlugin.GUARDING_PICKLE, MemoryKeyStatus.ABSENT)) {
           repeat((location) -> {
               if (location.getBlock().getType() == Material.SEA_PICKLE) {
                   entity.setMemory(TestPlugin.PICKLE_TARGET, location);
                   return false;
               }

               return true;
           }, entity.getLocation(), new Vector(3, 3, 3), new Vector(3, 3, 3)); // Search 9 block radius for any pickles. Pick the first one!
        }
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.PICKLE_TARGET, TestPlugin.GUARDING_PICKLE);
    }

    public static void repeat(Predicate<Location> consumer, Location base, Vector min, Vector max) {
        int xMin = base.getBlockX() - min.getBlockX();
        int yMin = base.getBlockY() - min.getBlockY();
        int zMin = base.getBlockZ() - min.getBlockZ();
        int xMax = base.getBlockX() + max.getBlockX();
        int yMax = base.getBlockY() + max.getBlockY();
        int zMax = base.getBlockZ() + max.getBlockZ();

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    if (!consumer.test(new Location(base.getWorld(), x, y, z))) {
                        break;
                    }
                }
            }
        }
    }
}
