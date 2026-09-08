package org.bukkit.craftbukkit.event.player;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.material.MaterialData;
import org.jspecify.annotations.Nullable;

public class CraftPlayerStatisticIncrementEvent extends CraftPlayerEvent implements PlayerStatisticIncrementEvent {

    protected final Statistic statistic;
    private final int initialValue;
    private final int newValue;
    private @Nullable EntityType entityType;
    private @Nullable Material material;

    private boolean cancelled;

    public CraftPlayerStatisticIncrementEvent(final Player player, final Statistic statistic, final int initialValue, final int newValue, final @Nullable Keyed element) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        switch (element) {
            case final EntityType type -> this.entityType = type;
            case final Material type -> {
                if (type.isLegacy()) {
                    if (statistic.getType() == Statistic.Type.BLOCK) {
                        this.material = Bukkit.getUnsafe().fromLegacy(new MaterialData(type), false);
                    } else if (statistic.getType() == Statistic.Type.ITEM) {
                        this.material = Bukkit.getUnsafe().fromLegacy(new MaterialData(type), true);
                    } else {
                        // Theoretically, this should not happen, can probably print a warning, but for now it should be fine.
                        this.material = Bukkit.getUnsafe().fromLegacy(new MaterialData(type), false);
                    }
                }
            }
            case null, default -> {}
        }
    }

    public CraftPlayerStatisticIncrementEvent(
        final net.minecraft.world.entity.player.Player player, final Statistic statistic, final int initialValue, final int newValue, final @Nullable Keyed element
    ) {
        this((Player) player.getBukkitEntity(), statistic, initialValue, newValue, element);
    }

    @Override
    public Statistic getStatistic() {
        return this.statistic;
    }

    @Override
    public int getPreviousValue() {
        return this.initialValue;
    }

    @Override
    public int getNewValue() {
        return this.newValue;
    }

    @Override
    public @Nullable EntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public @Nullable Material getMaterial() {
        return this.material;
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
        return PlayerStatisticIncrementEvent.getHandlerList();
    }
}
