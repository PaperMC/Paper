package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Input;
import org.bukkit.craftbukkit.CraftInput;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInputEvent;

public class CraftPlayerInputEvent extends CraftPlayerEvent implements PlayerInputEvent {

    private final Input input;

    public CraftPlayerInputEvent(final Player player, final Input input) {
        super(player);
        this.input = input;
    }

    public CraftPlayerInputEvent(final ServerPlayer player, final net.minecraft.world.entity.player.Input input) {
        this(player.getBukkitEntity(), new CraftInput(input));
    }

    @Override
    public Input getInput() {
        return this.input;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerInputEvent.getHandlerList();
    }
}
