package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class CraftPlayerExpChangeEvent extends CraftPlayerEvent implements PlayerExpChangeEvent {

    private final ExperienceOrb source;
    private int amount;

    public CraftPlayerExpChangeEvent(final Player player, final ExperienceOrb source, final int amount) {
        super(player);
        this.source = source;
        this.amount = amount;
    }

    public CraftPlayerExpChangeEvent(final ServerPlayer player, final net.minecraft.world.entity.ExperienceOrb source, final int amount) {
        this(player.getBukkitEntity(), (ExperienceOrb) source.getBukkitEntity(), amount);
    }

    @Override
    public ExperienceOrb getSource() {
        return this.source;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerExpChangeEvent.getHandlerList();
    }
}
