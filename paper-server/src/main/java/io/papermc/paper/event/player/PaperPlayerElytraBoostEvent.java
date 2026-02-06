package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerElytraBoostEvent extends CraftPlayerEvent implements PlayerElytraBoostEvent {

    private final ItemStack itemStack;
    private final Firework firework;
    private boolean consume = true;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public PaperPlayerElytraBoostEvent(final Player player, final ItemStack itemStack, final Firework firework, final EquipmentSlot hand) {
        super(player);
        this.itemStack = itemStack;
        this.firework = firework;
        this.hand = hand;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public Firework getFirework() {
        return this.firework;
    }

    @Override
    public boolean shouldConsume() {
        return this.consume;
    }

    @Override
    public void setShouldConsume(final boolean consume) {
        this.consume = consume;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerElytraBoostEvent.getHandlerList();
    }
}
