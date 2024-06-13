package org.bukkit.craftbukkit;

import net.minecraft.core.registries.Registries;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftGameEvent extends GameEvent implements Handleable<net.minecraft.world.level.gameevent.GameEvent> {

    public static GameEvent minecraftToBukkit(net.minecraft.world.level.gameevent.GameEvent minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.GAME_EVENT, Registry.GAME_EVENT);
    }

    public static net.minecraft.world.level.gameevent.GameEvent bukkitToMinecraft(GameEvent bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final NamespacedKey key;
    private final net.minecraft.resources.ResourceKey<net.minecraft.world.level.gameevent.GameEvent> handleKey; // Paper
    private final net.minecraft.world.level.gameevent.GameEvent handle;

    public CraftGameEvent(NamespacedKey key, net.minecraft.world.level.gameevent.GameEvent handle) {
        this.key = key;
        this.handleKey = net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.GAME_EVENT, org.bukkit.craftbukkit.util.CraftNamespacedKey.toMinecraft(key)); // Paper
        this.handle = handle;
    }

    @Override
    public net.minecraft.world.level.gameevent.GameEvent getHandle() {
        return this.handle;
    }

    // Paper start
    @Override
    public int getRange() {
        return this.handle.notificationRadius();
    }

    @Override
    public int getVibrationLevel() {
        return net.minecraft.world.level.gameevent.vibrations.VibrationSystem.getGameEventFrequency(this.handleKey);
    }
    // Paper end

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftGameEvent)) {
            return false;
        }

        return this.getKey().equals(((GameEvent) other).getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    @Override
    public String toString() {
        return "CraftGameEvent{key=" + this.key + "}";
    }
}
