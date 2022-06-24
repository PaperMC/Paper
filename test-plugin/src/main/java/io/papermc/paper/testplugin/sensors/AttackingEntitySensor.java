package io.papermc.paper.testplugin.sensors;

import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class AttackingEntitySensor implements Sensor<LivingEntity> {
    @Override
    public int getInterval() {
        return 20;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        EntityDamageEvent event = entity.getLastDamageCause();
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            // you prolly wanna use an event instead
            entity.setMemory(TestPlugin.ENTITY_TARGET, damageByEntityEvent.getDamager());
            entity.setLastDamageCause(null);
        }
       
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.ENTITY_TARGET);
    }

}
