package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Armadillo;

public class CraftArmadillo extends CraftAnimals implements Armadillo {

    public CraftArmadillo(CraftServer server, net.minecraft.world.entity.animal.armadillo.Armadillo entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.armadillo.Armadillo getHandle() {
        return (net.minecraft.world.entity.animal.armadillo.Armadillo) super.getHandle();
    }

    @Override
    public State getState() {
        return this.stateToBukkit(this.getHandle().getState());
    }

    @Override
    public void setState(final State state) {
        Preconditions.checkArgument(state != null, "state cannot be null");
        this.getHandle().switchToState(this.stateToNMS(state));
    }

    @Override
    public void rollUp() {
        this.getHandle().rollUp();
    }

    @Override
    public void rollOut() {
        this.getHandle().rollOut();
    }

    @Override
    public String toString() {
        return "CraftArmadillo";
    }

    public static State stateToBukkit(ArmadilloState state) {
        return switch (state) {
            case IDLE -> State.IDLE;
            case ROLLING -> State.ROLLING;
            case SCARED -> State.SCARED;
            case UNROLLING -> State.UNROLLING;
        };
    }

    public static ArmadilloState stateToNMS(State state) {
        return switch (state) {
            case State.IDLE -> ArmadilloState.IDLE;
            case State.ROLLING -> ArmadilloState.ROLLING;
            case State.SCARED -> ArmadilloState.SCARED;
            case State.UNROLLING -> ArmadilloState.UNROLLING;
        };
    }
}
