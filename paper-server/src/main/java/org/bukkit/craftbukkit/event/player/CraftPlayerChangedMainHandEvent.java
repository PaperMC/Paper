package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.inventory.MainHand;

public class CraftPlayerChangedMainHandEvent extends CraftPlayerEvent implements PlayerChangedMainHandEvent {

    private final MainHand newMainHand;

    public CraftPlayerChangedMainHandEvent(final Player player, final MainHand newMainHand) {
        super(player);
        this.newMainHand = newMainHand;
    }

    public CraftPlayerChangedMainHandEvent(final ServerPlayer player, final HumanoidArm newMainHand) {
        this(player.getBukkitEntity(), newMainHand == HumanoidArm.LEFT ? MainHand.LEFT : MainHand.RIGHT);
    }

    @Override
    public MainHand getNewMainHand() {
        return this.newMainHand;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChangedMainHandEvent.getHandlerList();
    }
}
