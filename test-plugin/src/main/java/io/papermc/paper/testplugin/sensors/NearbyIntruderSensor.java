package io.papermc.paper.testplugin.sensors;

import com.destroystokyo.paper.MaterialTags;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class NearbyIntruderSensor implements Sensor<LivingEntity> {
    @Override
    public int getInterval() {
        return 20;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        if (entity.isMemoryStatus(TestPlugin.PICKLE_TARGET, MemoryKeyStatus.PRESENT) && entity.isMemoryStatus(TestPlugin.NEARBY_PLAYER, MemoryKeyStatus.PRESENT)) {
            Player player = entity.getMemory(TestPlugin.NEARBY_PLAYER);
            // They have a sword! Angry and attack
            if (MaterialTags.SWORDS.isTagged(player.getInventory().getItemInMainHand())) {
                entity.setMemory(TestPlugin.GUARDING_PICKLE, true);
            } else {
                entity.forgetMemory(TestPlugin.GUARDING_PICKLE);
            }

        }

    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.NEARBY_PLAYER, TestPlugin.PICKLE_TARGET, TestPlugin.GUARDING_PICKLE);
    }

}
