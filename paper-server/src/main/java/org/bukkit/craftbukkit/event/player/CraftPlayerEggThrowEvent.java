package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import org.bukkit.craftbukkit.event.data.TransientEggInfo;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.checkerframework.checker.index.qual.NonNegative;

public class CraftPlayerEggThrowEvent extends CraftPlayerEvent implements PlayerEggThrowEvent {

    private final Egg egg;
    private final TransientEggInfo eggInfo;

    public CraftPlayerEggThrowEvent(final Player player, final Egg egg, final TransientEggInfo eggInfo) {
        super(player);
        this.egg = egg;
        this.eggInfo = eggInfo;
    }

    public CraftPlayerEggThrowEvent(final ServerPlayer player, final ThrownEgg egg, final TransientEggInfo eggInfo) {
        this(player.getBukkitEntity(), (Egg) egg.getBukkitEntity(), eggInfo);
    }

    @Override
    public Egg getEgg() {
        return this.egg;
    }

    @Override
    public boolean isHatching() {
        return this.eggInfo.isHatching();
    }

    @Override
    public void setHatching(final boolean hatching) {
        this.eggInfo.setHatching(hatching);
    }

    @Override
    public EntityType getHatchingType() {
        return this.eggInfo.getType();
    }

    @Override
    public void setHatchingType(final EntityType hatchType) {
        this.eggInfo.setType(hatchType);
    }

    @Override
    public @NonNegative byte getNumHatches() {
        return (byte) this.eggInfo.getNumHatches();
    }

    @Override
    public void setNumHatches(final @NonNegative byte numHatches) {
        this.eggInfo.setNumHatches(numHatches);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerEggThrowEvent.getHandlerList();
    }
}
