package com.destroystokyo.paper.loottable;

import io.papermc.paper.configuration.WorldConfiguration;
import io.papermc.paper.configuration.type.DurationOrDisabled;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperLootableInventoryData {

    private static final Random RANDOM = new Random();

    private long lastFill = -1;
    private long nextRefill = -1;
    private int numRefills = 0;
    private @Nullable Map<UUID, Long> lootedPlayers;

    public long getLastFill() {
        return this.lastFill;
    }

    long getNextRefill() {
        return this.nextRefill;
    }

    long setNextRefill(final long nextRefill) {
        final long prev = this.nextRefill;
        this.nextRefill = nextRefill;
        return prev;
    }

    public <T> boolean shouldReplenish(final T lootTableHolder, final LootTableInterface<T> holderInterface, final net.minecraft.world.entity.player.@Nullable Player player) {

        // No Loot Table associated
        if (!holderInterface.hasLootTable(lootTableHolder)) {
            return false;
        }

        // ALWAYS process the first fill or if the feature is disabled
        if (this.lastFill == -1 || !holderInterface.paperConfig(lootTableHolder).lootables.autoReplenish) {
            return true;
        }

        // Only process refills when a player is set
        if (player == null) {
            return false;
        }

        // Chest is not scheduled for refill
        if (this.nextRefill == -1) {
            return false;
        }

        final WorldConfiguration paperConfig = holderInterface.paperConfig(lootTableHolder);

        // Check if max refills has been hit
        if (paperConfig.lootables.maxRefills != -1 && this.numRefills >= paperConfig.lootables.maxRefills) {
            return false;
        }

        // Refill has not been reached
        if (this.nextRefill > System.currentTimeMillis()) {
            return false;
        }


        final Player bukkitPlayer = (Player) player.getBukkitEntity();
        final LootableInventoryReplenishEvent event = new LootableInventoryReplenishEvent(bukkitPlayer, holderInterface.getInventoryForEvent(lootTableHolder));
        event.setCancelled(!this.canPlayerLoot(player.getUUID(), paperConfig));
        return event.callEvent();
    }

    public interface LootTableInterface<T> {

        WorldConfiguration paperConfig(T holder);

        void setSeed(T holder, long seed);

        boolean hasLootTable(T holder);

        LootableInventory getInventoryForEvent(T holder);
    }

    public static final LootTableInterface<RandomizableContainer> CONTAINER = new LootTableInterface<>() {
        @Override
        public WorldConfiguration paperConfig(final RandomizableContainer holder) {
            return Objects.requireNonNull(holder.getLevel(), "Can only manager loot replenishment on block entities in a world").paperConfig();
        }

        @Override
        public void setSeed(final RandomizableContainer holder, final long seed) {
            holder.setLootTableSeed(seed);
        }

        @Override
        public boolean hasLootTable(final RandomizableContainer holder) {
            return holder.getLootTable() != null;
        }

        @Override
        public LootableInventory getInventoryForEvent(final RandomizableContainer holder) {
            return holder.getLootableInventory();
        }
    };

    public static final LootTableInterface<ContainerEntity> ENTITY = new LootTableInterface<>() {
        @Override
        public WorldConfiguration paperConfig(final ContainerEntity holder) {
            return holder.level().paperConfig();
        }

        @Override
        public void setSeed(final ContainerEntity holder, final long seed) {
            holder.setContainerLootTableSeed(seed);
        }

        @Override
        public boolean hasLootTable(final ContainerEntity holder) {
            return holder.getContainerLootTable() != null;
        }

        @Override
        public LootableInventory getInventoryForEvent(final ContainerEntity holder) {
            return holder.getLootableInventory();
        }
    };

    public <T> boolean shouldClearLootTable(final T lootTableHolder, final LootTableInterface<T> holderInterface, final net.minecraft.world.entity.player.@Nullable Player player) {
        this.lastFill = System.currentTimeMillis();
        final WorldConfiguration paperConfig = holderInterface.paperConfig(lootTableHolder);
        if (paperConfig.lootables.autoReplenish) {
            final long min = paperConfig.lootables.refreshMin.seconds();
            final long max = paperConfig.lootables.refreshMax.seconds();
            this.nextRefill = this.lastFill + (min + RANDOM.nextLong(max - min + 1)) * 1000L;
            this.numRefills++;
            if (paperConfig.lootables.resetSeedOnFill) {
                holderInterface.setSeed(lootTableHolder, 0);
            }
            if (player != null) { // This means that numRefills can be incremented without a player being in the lootedPlayers list - Seems to be EntityMinecartChest specific
                this.setPlayerLootedState(player.getUUID(), true);
            }
            return false;
        }
        return true;
    }

    private static final String ROOT = "Paper.LootableData";
    private static final String LAST_FILL = "lastFill";
    private static final String NEXT_REFILL = "nextRefill";
    private static final String NUM_REFILLS = "numRefills";
    private static final String LOOTED_PLAYERS = "lootedPlayers";

    public void loadNbt(final CompoundTag base) {
        if (!base.contains(ROOT, Tag.TAG_COMPOUND)) {
            return;
        }
        final CompoundTag comp = base.getCompound(ROOT);
        if (comp.contains(LAST_FILL)) {
            this.lastFill = comp.getLong(LAST_FILL);
        }
        if (comp.contains(NEXT_REFILL)) {
            this.nextRefill = comp.getLong(NEXT_REFILL);
        }

        if (comp.contains(NUM_REFILLS)) {
            this.numRefills = comp.getInt(NUM_REFILLS);
        }
        if (comp.contains(LOOTED_PLAYERS, Tag.TAG_LIST)) {
            final ListTag list = comp.getList(LOOTED_PLAYERS, Tag.TAG_COMPOUND);
            final int size = list.size();
            if (size > 0) {
                this.lootedPlayers = new HashMap<>(list.size());
            }
            for (int i = 0; i < size; i++) {
                final CompoundTag cmp = list.getCompound(i);
                this.lootedPlayers.put(cmp.getUUID("UUID"), cmp.getLong("Time"));
            }
        }
    }

    public void saveNbt(final CompoundTag base) {
        final CompoundTag comp = new CompoundTag();
        if (this.nextRefill != -1) {
            comp.putLong(NEXT_REFILL, this.nextRefill);
        }
        if (this.lastFill != -1) {
            comp.putLong(LAST_FILL, this.lastFill);
        }
        if (this.numRefills != 0) {
            comp.putInt(NUM_REFILLS, this.numRefills);
        }
        if (this.lootedPlayers != null && !this.lootedPlayers.isEmpty()) {
            final ListTag list = new ListTag();
            for (final Map.Entry<UUID, Long> entry : this.lootedPlayers.entrySet()) {
                final CompoundTag cmp = new CompoundTag();
                cmp.putUUID("UUID", entry.getKey());
                cmp.putLong("Time", entry.getValue());
                list.add(cmp);
            }
            comp.put(LOOTED_PLAYERS, list);
        }

        if (!comp.isEmpty()) {
            base.put(ROOT, comp);
        }
    }

    void setPlayerLootedState(final UUID player, final boolean looted) {
        if (looted && this.lootedPlayers == null) {
            this.lootedPlayers = new HashMap<>();
        }
        if (looted) {
            this.lootedPlayers.put(player, System.currentTimeMillis());
        } else if (this.lootedPlayers != null) {
            this.lootedPlayers.remove(player);
        }
    }

    boolean canPlayerLoot(final UUID player, final WorldConfiguration worldConfiguration) {
        final @Nullable Long lastLooted = this.getLastLooted(player);
        if (!worldConfiguration.lootables.restrictPlayerReloot || lastLooted == null) return true;

        final DurationOrDisabled restrictPlayerRelootTime = worldConfiguration.lootables.restrictPlayerRelootTime;
        if (restrictPlayerRelootTime.value().isEmpty()) return false;

        return TimeUnit.SECONDS.toMillis(restrictPlayerRelootTime.value().get().seconds()) + lastLooted < System.currentTimeMillis();
    }

    boolean hasPlayerLooted(final UUID player) {
        return this.lootedPlayers != null && this.lootedPlayers.containsKey(player);
    }

    @Nullable Long getLastLooted(final UUID player) {
        return this.lootedPlayers != null ? this.lootedPlayers.get(player) : null;
    }
}
