package org.bukkit.craftbukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerAdvancementDoneEvent extends CraftPlayerEvent implements PlayerAdvancementDoneEvent {

    private final Advancement advancement;
    private @Nullable Component message;

    public CraftPlayerAdvancementDoneEvent(final Player player, final Advancement advancement, final @Nullable Component message) {
        super(player);
        this.advancement = advancement;
        this.message = message;
    }

    @Override
    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public @Nullable Component message() {
        return this.message;
    }

    @Override
    public void message(@Nullable Component message) {
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerAdvancementDoneEvent.getHandlerList();
    }
}
