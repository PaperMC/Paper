package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Armadillo;

public class CraftArmadillo extends CraftAnimals implements Armadillo {

    public CraftArmadillo(CraftServer server, net.minecraft.world.entity.animal.armadillo.Armadillo entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.armadillo.Armadillo getHandle() {
        return (net.minecraft.world.entity.animal.armadillo.Armadillo) this.entity;
    }

    @Override
    public State getState() {
        return State.valueOf(this.getHandle().getState().name());
    }

    @Override
    public void rollUp() {
        this.getHandle().getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, true, net.minecraft.world.entity.animal.armadillo.Armadillo.SCARE_CHECK_INTERVAL);
        this.getHandle().rollUp();
    }

    @Override
    public void rollOut() {
        if (this.getHandle().getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY) <= ArmadilloState.UNROLLING.animationDuration()) {
            // already unrolling or unrolled
            return;
        }

        this.getHandle().lastHurtByMob = null; // Clear this memory to not have the sensor trigger rollUp instantly for damaged armadillo
        this.getHandle().getBrain().setMemoryWithExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY, true, ArmadilloState.UNROLLING.animationDuration());
    }
}
