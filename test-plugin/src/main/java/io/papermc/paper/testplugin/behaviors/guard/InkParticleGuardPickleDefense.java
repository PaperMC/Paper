package io.papermc.paper.testplugin.behaviors.guard;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

public class InkParticleGuardPickleDefense implements Behavior<LivingEntity> {


    @Override
    public void start(LivingEntity entity) {

    }

    @Override
    public void tick(LivingEntity entity) {
    }

    @Override
    public void stop(LivingEntity entity) {
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
    public Collection<MemoryPair> getInitialMemoryRequirements() {
        return List.of(
        );
    }
}
