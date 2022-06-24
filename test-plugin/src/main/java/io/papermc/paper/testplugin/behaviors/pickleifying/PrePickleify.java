package io.papermc.paper.testplugin.behaviors.pickleifying;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;

import java.util.Collection;
import java.util.List;

public class PrePickleify implements Behavior<Mob> {

    @Override
    public void start(Mob entity) {
        entity.getPathfinder().stopPathfinding();
        Location location = entity.getMemory(TestPlugin.PICKLE_TARGET).clone();
        while (location.getY() < entity.getWorld().getMaxHeight() && location.getBlock().getType() == Material.WATER) {
            location.add(0, 1, 0);
        }
        entity.setMemory(TestPlugin.PRE_PICKLIFY_TARGET, location);
    }

    @Override
    public void tick(Mob entity) {
        // Cool animation, idk
        Location location = entity.getMemory(TestPlugin.PRE_PICKLIFY_TARGET);
        entity.getWorld().spawnParticle(Particle.SONIC_BOOM, location, 1);
    }

    @Override
    public void stop(Mob entity) {
        Location location = entity.getMemory(TestPlugin.PRE_PICKLIFY_TARGET);
        entity.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);

        entity.forgetMemory(TestPlugin.PRE_PICKLIFY_TARGET);
        entity.setMemory(TestPlugin.PICKLEIFY_TARGET, location);
    }

    @Override
    public int getMinRuntime() {
        return 20;
    }

    @Override
    public int getMaxRuntime() {
        return 40;
    }

    @Override
    public boolean canStillRun(Mob entity) {
        return true;
    }

    @Override
    public Collection<MemoryPair> getInitialMemoryRequirements() {
        return List.of(
            new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.PICKLE_TARGET),
            new MemoryPair(MemoryKeyStatus.ABSENT, TestPlugin.PICKLEIFY_TARGET),
            new MemoryPair(MemoryKeyStatus.REGISTERED, TestPlugin.PRE_PICKLIFY_TARGET)
        );
    }
}
