package io.papermc.paper.testplugin.sensors;

import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class NearbyPlayerSensor implements Sensor<LivingEntity> {
    @Override
    public int getInterval() {
        return 20;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        Player nearbyPlayer = null;
        for (Entity nearby : entity.getNearbyEntities(5, 5, 5)) {
            if (nearby instanceof Player player) {
                nearbyPlayer = player;
                break;
            }
        }
        entity.setMemory(TestPlugin.NEARBY_PLAYER, nearbyPlayer);
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.NEARBY_PLAYER);
    }

}
