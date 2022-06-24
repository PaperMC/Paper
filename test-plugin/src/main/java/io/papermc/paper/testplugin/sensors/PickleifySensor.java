package io.papermc.paper.testplugin.sensors;

import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class PickleifySensor implements Sensor<LivingEntity> {
    @Override
    public int getInterval() {
        return 20;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        if (entity.getHealth() < (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2)) {
            entity.setMemory(TestPlugin.PICKLEIFYING, true);
        } else {
            entity.forgetMemory(TestPlugin.PICKLEIFYING);
        }

    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.PICKLEIFYING);
    }

}
