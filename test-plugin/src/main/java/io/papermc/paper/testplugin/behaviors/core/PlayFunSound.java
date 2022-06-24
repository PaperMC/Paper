package io.papermc.paper.testplugin.behaviors.core;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

/*
For doing proper ratelimiting, use a memory.
 */
public class PlayFunSound implements Behavior<LivingEntity> {


    @Override
    public void start(LivingEntity entity) {
        entity.getWorld().spawnParticle(Particle.NOTE, entity.getLocation(), 5, 1, 1, 1);
        entity.getWorld().playSound(entity, Sound.ENTITY_CAT_PURREOW, 1, 1);
    }

    @Override
    public void tick(LivingEntity entity) {

    }

    @Override
    public void stop(LivingEntity entity) {
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
    public boolean canStillRun(LivingEntity entity) {
        return true;
    }

    @Override
    public Collection<MemoryPair> getInitialMemoryRequirements() {
        return List.of();
    }
}
