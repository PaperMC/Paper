package io.papermc.paper.testplugin.behaviors.core;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

public class SparkleAroundSelf implements Behavior<LivingEntity> {


    @Override
    public void start(LivingEntity entity) {
        entity.setGlowing(true);
    }

    @Override
    public void tick(LivingEntity entity) {
        entity.getWorld().spawnParticle(Particle.SCRAPE, entity.getLocation(), 5, 1, 1, 1);
    }

    @Override
    public void stop(LivingEntity entity) {
        entity.setGlowing(false);
    }

    @Override
    public int getMinRuntime() {
        return 5;
    }

    @Override
    public int getMaxRuntime() {
        return 10;
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
