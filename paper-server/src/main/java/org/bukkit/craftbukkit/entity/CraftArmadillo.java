package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
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

    private State stateToBukkit(net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState state) {
        return switch (state) {
            case IDLE -> State.IDLE;
            case ROLLING -> State.ROLLING;
            case SCARED -> State.SCARED;
            case UNROLLING -> State.UNROLLING;
        };
    }

    private net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState stateToNMS(State state) {
        return switch (state) {
            case State.IDLE -> net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState.IDLE;
            case State.ROLLING -> net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState.ROLLING;
            case State.SCARED -> net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState.SCARED;
            case State.UNROLLING -> net.minecraft.world.entity.animal.armadillo.Armadillo.ArmadilloState.UNROLLING;
        };
    }
}
