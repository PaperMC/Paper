package org.bukkit.craftbukkit;

import java.util.Objects;
import org.bukkit.Input;

public class CraftInput implements Input {

    private final net.minecraft.world.entity.player.Input handle;

    public CraftInput(net.minecraft.world.entity.player.Input handle) {
        this.handle = handle;
    }

    @Override
    public boolean isForward() {
        return this.handle.forward();
    }

    @Override
    public boolean isBackward() {
        return this.handle.backward();
    }

    @Override
    public boolean isLeft() {
        return this.handle.left();
    }

    @Override
    public boolean isRight() {
        return this.handle.right();
    }

    @Override
    public boolean isJump() {
        return this.handle.jump();
    }

    @Override
    public boolean isSneak() {
        return this.handle.shift();
    }

    @Override
    public boolean isSprint() {
        return this.handle.sprint();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftInput other = (CraftInput) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public String toString() {
        return "CraftInput{" + this.handle + '}';
    }
}
