package com.destroystokyo.paper.loottable;

import com.destroystokyo.paper.PaperWorldConfig;
import net.minecraft.server.*;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PaperLootableInventoryData {

    private static final Random RANDOM = new Random();

    private long lastFill = -1;
    private long nextRefill = -1;
    private int numRefills = 0;
    private Map<UUID, Long> lootedPlayers;
    private final PaperLootableInventory lootable;

    public PaperLootableInventoryData(PaperLootableInventory lootable) {
        this.lootable = lootable;
    }

    long getLastFill() {
        return this.lastFill;
    }

    long getNextRefill() {
        return this.nextRefill;
    }

    long setNextRefill(long nextRefill) {
        long prev = this.nextRefill;
        this.nextRefill = nextRefill;
        return prev;
    }

    public boolean shouldReplenish(@Nullable EntityHuman player) {
        LootTable table = this.lootable.getLootTable();

        // No Loot Table associated
        if (table == null) {
            return false;
        }

        // ALWAYS process the first fill or if the feature is disabled
        if (this.lastFill == -1 || !this.lootable.getNMSWorld().paperConfig.autoReplenishLootables) {
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

        final PaperWorldConfig paperConfig = this.lootable.getNMSWorld().paperConfig;

        // Check if max refills has been hit
        if (paperConfig.maxLootableRefills != -1 && this.numRefills >= paperConfig.maxLootableRefills) {
            return false;
        }

        // Refill has not been reached
        if (this.nextRefill > System.currentTimeMillis()) {
            return false;
        }


        final Player bukkitPlayer = (Player) player.getBukkitEntity();
        LootableInventoryReplenishEvent event = new LootableInventoryReplenishEvent(bukkitPlayer, lootable.getAPILootableInventory());
        if (paperConfig.restrictPlayerReloot && hasPlayerLooted(player.getUniqueID())) {
            event.setCancelled(true);
        }
        return event.callEvent();
    }
    public void processRefill(@Nullable EntityHuman player) {
        this.lastFill = System.currentTimeMillis();
        final PaperWorldConfig paperConfig = this.lootable.getNMSWorld().paperConfig;
        if (paperConfig.autoReplenishLootables) {
            int min = paperConfig.lootableRegenMin;
            int max = paperConfig.lootableRegenMax;
            this.nextRefill = this.lastFill + (min + RANDOM.nextInt(max - min + 1)) * 1000L;
            this.numRefills++;
            if (paperConfig.changeLootTableSeedOnFill) {
                this.lootable.setSeed(0);
            }
            if (player != null) { // This means that numRefills can be incremented without a player being in the lootedPlayers list - Seems to be EntityMinecartChest specific
                this.setPlayerLootedState(player.getUniqueID(), true);
            }
        } else {
            this.lootable.clearLootTable();
        }
    }


    public void loadNbt(NBTTagCompound base) {
        if (!base.hasKeyOfType("Paper.LootableData", 10)) { // 10 = compound
            return;
        }
        NBTTagCompound comp = base.getCompound("Paper.LootableData");
        if (comp.hasKey("lastFill")) {
            this.lastFill = comp.getLong("lastFill");
        }
        if (comp.hasKey("nextRefill")) {
            this.nextRefill = comp.getLong("nextRefill");
        }

        if (comp.hasKey("numRefills")) {
            this.numRefills = comp.getInt("numRefills");
        }
        if (comp.hasKeyOfType("lootedPlayers", 9)) { // 9 = list
            NBTTagList list = comp.getList("lootedPlayers", 10); // 10 = compound
            final int size = list.size();
            if (size > 0) {
                this.lootedPlayers = new HashMap<>(list.size());
            }
            for (int i = 0; i < size; i++) {
                final NBTTagCompound cmp = list.getCompound(i);
                lootedPlayers.put(cmp.getUUID("UUID"), cmp.getLong("Time"));
            }
        }
    }
    public void saveNbt(NBTTagCompound base) {
        NBTTagCompound comp = new NBTTagCompound();
        if (this.nextRefill != -1) {
            comp.setLong("nextRefill", this.nextRefill);
        }
        if (this.lastFill != -1) {
            comp.setLong("lastFill", this.lastFill);
        }
        if (this.numRefills != 0) {
            comp.setInt("numRefills", this.numRefills);
        }
        if (this.lootedPlayers != null && !this.lootedPlayers.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (Map.Entry<UUID, Long> entry : this.lootedPlayers.entrySet()) {
                NBTTagCompound cmp = new NBTTagCompound();
                cmp.setUUID("UUID", entry.getKey());
                cmp.setLong("Time", entry.getValue());
                list.add(cmp);
            }
            comp.set("lootedPlayers", list);
        }

        if (!comp.isEmpty()) {
            base.set("Paper.LootableData", comp);
        }
    }

    void setPlayerLootedState(UUID player, boolean looted) {
        if (looted && this.lootedPlayers == null) {
            this.lootedPlayers = new HashMap<>();
        }
        if (looted) {
            if (!this.lootedPlayers.containsKey(player)) {
                this.lootedPlayers.put(player, System.currentTimeMillis());
            }
        } else if (this.lootedPlayers != null) {
            this.lootedPlayers.remove(player);
        }
    }

    boolean hasPlayerLooted(UUID player) {
        return this.lootedPlayers != null && this.lootedPlayers.containsKey(player);
    }

    Long getLastLooted(UUID player) {
        return lootedPlayers != null ? lootedPlayers.get(player) : null;
    }
}
