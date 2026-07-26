package org.bukkit.craftbukkit;

import org.bukkit.Input;

public class CraftInput implements Input {

    private final net.minecraft.world.entity.player.Input input;

    public CraftInput(net.minecraft.world.entity.player.Input input) {
        this.input = input;
    }

    @Override
    public boolean isForward() {
        return this.input.forward();
    }

    @Override
    public boolean isBackward() {
        return this.input.backward();
    }

    @Override
    public boolean isLeft() {
        return this.input.left();
    }

    @Override
    public boolean isRight() {
        return this.input.right();
    }

    @Override
    public boolean isJump() {
        return this.input.jump();
    }

    @Override
    public boolean isSneak() {
        return this.input.shift();
    }

    @Override
    public boolean isSprint() {
        return this.input.sprint();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.input.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final CraftInput other = (CraftInput) obj;
        return this.input.equals(other.input);
    }

    @Override
    public String toString() {
        return "CraftInput{" + this.input + '}';
    }
}
