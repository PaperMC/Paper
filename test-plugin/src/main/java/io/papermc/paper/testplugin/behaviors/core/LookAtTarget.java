package io.papermc.paper.testplugin.behaviors.core;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.Collection;
import java.util.List;

public class LookAtTarget implements Behavior<Mob> {


    @Override
    public void start(Mob entity) {
    }

    @Override
    public void tick(Mob entity) {
        Entity target = entity.getMemory(TestPlugin.ENTITY_TARGET);
        entity.lookAt(target);
    }

    @Override
    public void stop(Mob entity) {
    }

    @Override
    public boolean canStart(Mob entity) {
        if (Math.random() > 0.8) {
            return Behavior.super.canStart(entity); // Small chance, don't happen all the time
        }

        return false;
    }

    @Override
    public int getMinRuntime() {
        return 0;
    }

    @Override
    public int getMaxRuntime() {
        return 20;
    }

    @Override
    public Collection<MemoryPair> getInitialMemoryRequirements() {
        return List.of(new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.ENTITY_TARGET));
    }
}
