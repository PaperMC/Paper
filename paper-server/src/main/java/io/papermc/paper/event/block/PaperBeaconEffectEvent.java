package io.papermc.paper.event.block;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

public class PaperBeaconEffectEvent extends CraftBlockEvent implements BeaconEffectEvent {

    private final Player player;
    private final boolean primary;
    private PotionEffect effect;

    private boolean cancelled;

    public PaperBeaconEffectEvent(final Block beacon, final PotionEffect effect, final Player player, final boolean primary) {
        super(beacon);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    public static void applyEffects(final Level level, final BlockPos pos, final List<net.minecraft.world.entity.player.Player> players, final MobEffectInstance effect, final boolean isPrimary) {
        final PotionEffect apiEffect = CraftPotionUtil.toBukkit(effect);
        final Block apiBlock = CraftBlock.at(level, pos);
        for (final net.minecraft.world.entity.player.Player player : players) {
            final BeaconEffectEvent event = new PaperBeaconEffectEvent(apiBlock, apiEffect, (Player) player.getBukkitEntity(), isPrimary);
            if (event.callEvent()) {
                player.addEffect(CraftPotionUtil.fromBukkit(event.getEffect()), EntityPotionEffectEvent.Cause.BEACON);
            }
        }
    }

    @Override
    public PotionEffect getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(final PotionEffect effect) {
        this.effect = effect;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isPrimary() {
        return this.primary;
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
        return BeaconEffectEvent.getHandlerList();
    }
}
