package org.bukkit.craftbukkit;

import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;

public class CraftGameEvent extends GameEvent implements Holderable<net.minecraft.world.level.gameevent.GameEvent> {

    public static GameEvent minecraftToBukkit(net.minecraft.world.level.gameevent.GameEvent minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.GAME_EVENT);
    }

    public static net.minecraft.world.level.gameevent.GameEvent bukkitToMinecraft(GameEvent bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final Holder<net.minecraft.world.level.gameevent.GameEvent> holder;

    public CraftGameEvent(final Holder<net.minecraft.world.level.gameevent.GameEvent> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.gameevent.GameEvent> getHolder() {
        return this.holder;
    }

    @Override
    public int getRange() {
        return this.getHandle().notificationRadius();
    }

    @Override
    public int getVibrationLevel() {
        return net.minecraft.world.level.gameevent.vibrations.VibrationSystem.getGameEventFrequency(this.getHolder().unwrapKey().orElseThrow());
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public boolean equals(Object other) {
        return Holderable.super.implEquals(other);
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
