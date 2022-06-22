package io.papermc.paper.testplugin.behaviors;

import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClosestParrotSensor implements Sensor<LivingEntity> {

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        List<Squid> squids = entity.getNearbyEntities(10, 5, 10)
            .stream()
            .filter((nearbyEntity) -> nearbyEntity instanceof Squid)
            .map((squid) -> ((Squid) squid))
            .toList();

        if (entity instanceof BrainHolder<?> brainHolder) {
            brainHolder.setMemory();
            brainHolder.setMemory(entity, TestPlugin.SQUID_CANDIDATES, new ArrayList<>(squids), Long.MAX_VALUE);

            if (squids.size() == 3) { // Once 3 squids are found
                brainManager.setMemory(entity, TestPlugin.SQUID_RAGE, true, 200); // angry for 10 secs
            }
        }

    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return Set.of(TestPlugin.NEARBY_PARROTS);
    }
}
