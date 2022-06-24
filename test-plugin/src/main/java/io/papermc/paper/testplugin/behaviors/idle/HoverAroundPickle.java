package io.papermc.paper.testplugin.behaviors.idle;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

/*
This behavior will occasionally move towards the pickle and try to pathfind to it.
 */
public class HoverAroundPickle implements Behavior<Mob> {


    @Override
    public void start(Mob entity) {
        Location target = entity.getMemory(TestPlugin.PICKLE_TARGET);
        entity.getPathfinder().moveTo(target);
    }

    @Override
    public void tick(Mob entity) {

    }

    @Override
    public void stop(Mob entity) {
    }

    @Override
    public int getMinRuntime() {
        return 50;
    }

    @Override
    public int getMaxRuntime() {
        return 100;
    }

    @Override
    public Collection<MemoryPair> getInitialMemoryRequirements() {
        return List.of(
            new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.PICKLE_TARGET)
        );
    }

}
