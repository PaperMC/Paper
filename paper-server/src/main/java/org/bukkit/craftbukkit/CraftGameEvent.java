package org.bukkit.craftbukkit;

import io.papermc.paper.util.HolderableElement;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;

public class CraftGameEvent extends GameEvent implements HolderableElement<net.minecraft.world.level.gameevent.GameEvent, GameEvent> {

    public static GameEvent minecraftHolderToBukkit(Holder<net.minecraft.world.level.gameevent.GameEvent> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.GAME_EVENT);
    }

    public static Holder<net.minecraft.world.level.gameevent.GameEvent> bukkitToMinecraftHolder(GameEvent bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
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
        return HolderableElement.super.getKey();
    }

    @Override
    public boolean equals(Object other) {
        return HolderableElement.super.implEquals(other);
    }

    @Override
    public int hashCode() {
        return HolderableElement.super.implHashCode();
    }

    @Override
    public String toString() {
        return HolderableElement.super.implToString();
    }
}
