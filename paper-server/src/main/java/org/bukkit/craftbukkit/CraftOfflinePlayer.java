package org.bukkit.craftbukkit;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryMapper;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;

@SerializableAs("Player")
public class CraftOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {
    private final GameProfile profile;
    private final CraftServer server;
    private final PlayerDataStorage storage;

    protected CraftOfflinePlayer(CraftServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = server.console.playerDataStorage;

    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public String getName() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getName();
        }

        // This might not match lastKnownName but if not it should be more correct
        if (!this.profile.getName().isEmpty()) {
            return this.profile.getName();
        }

        CompoundTag data = this.getBukkitData();

        if (data != null) {
            if (data.contains("lastKnownName")) {
                return data.getString("lastKnownName");
            }
        }

        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.profile.getId();
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return new CraftPlayerProfile(this.profile);
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean isOp() {
        return this.server.getHandle().isOp(this.profile);
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }

        if (value) {
            this.server.getHandle().op(this.profile);
        } else {
            this.server.getHandle().deop(this.profile);
        }
    }

    @Override
    public boolean isBanned() {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).isBanned(this.getPlayerProfile());
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Date expires, String source) {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Instant expires, String source) {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Duration duration, String source) {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, duration, source);
    }

    public void setBanned(boolean value) {
        if (value) {
            ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), null, (Date) null, null);
        } else {
            ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).pardon(this.getPlayerProfile());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().getWhiteList().isWhiteListed(this.profile);
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().getWhiteList().add(new UserWhiteListEntry(this.profile));
        } else {
            this.server.getHandle().getWhiteList().remove(this.profile);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("UUID", this.profile.getId().toString());

        return result;
    }

    public static OfflinePlayer deserialize(Map<String, Object> args) {
        // Backwards comparability
        if (args.get("name") != null) {
            return Bukkit.getServer().getOfflinePlayer((String) args.get("name"));
        }

        return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String) args.get("UUID")));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[UUID=" + this.profile.getId() + "]";
    }

    @Override
    public Player getPlayer() {
        return this.server.getPlayer(this.getUniqueId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer other)) {
            return false;
        }

        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        return this.getUniqueId().equals(other.getUniqueId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        return hash;
    }

    private CompoundTag getData() {
        return this.storage.load(this.profile.getName(), this.profile.getId().toString()).orElse(null);
    }

    private CompoundTag getBukkitData() {
        CompoundTag result = this.getData();

        if (result != null) {
            if (!result.contains("bukkit")) {
                result.put("bukkit", new CompoundTag());
            }
            result = result.getCompound("bukkit");
        }

        return result;
    }

    private File getDataFile() {
        return new File(this.storage.getPlayerDir(), this.getUniqueId() + ".dat");
    }

    @Override
    public long getFirstPlayed() {
        Player player = this.getPlayer();
        if (player != null) return player.getFirstPlayed();

        CompoundTag data = this.getBukkitData();

        if (data != null) {
            if (data.contains("firstPlayed")) {
                return data.getLong("firstPlayed");
            } else {
                File file = this.getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getLastPlayed() {
        Player player = this.getPlayer();
        if (player != null) return player.getLastPlayed();

        CompoundTag data = this.getBukkitData();

        if (data != null) {
            if (data.contains("lastPlayed")) {
                return data.getLong("lastPlayed");
            } else {
                File file = this.getDataFile();
                return file.lastModified();
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.getData() != null;
    }

    @Override
    public Location getLastDeathLocation() {
        if (this.getData().contains("LastDeathLocation", 10)) {
            return GlobalPos.CODEC.parse(NbtOps.INSTANCE, this.getData().get("LastDeathLocation")).result().map(CraftMemoryMapper::fromNms).orElse(null);
        }
        return null;
    }

    @Override
    public Location getLocation() {
        CompoundTag data = this.getData();
        if (data == null) {
            return null;
        }

        if (data.contains("Pos") && data.contains("Rotation")) {
            ListTag position = (ListTag) data.get("Pos");
            ListTag rotation = (ListTag) data.get("Rotation");

            UUID uuid = new UUID(data.getLong("WorldUUIDMost"), data.getLong("WorldUUIDLeast"));

            return new Location(this.server.getWorld(uuid),
                position.getDouble(0),
                position.getDouble(1),
                position.getDouble(2),
                rotation.getFloat(0),
                rotation.getFloat(1)
            );
        }

        return null;
    }

    @Override
    public Location getBedSpawnLocation() {
        return this.getRespawnLocation();
    }

    @Override
    public Location getRespawnLocation() {
        CompoundTag data = this.getData();
        if (data == null) return null;

        if (data.contains("SpawnX") && data.contains("SpawnY") && data.contains("SpawnZ")) {
            String spawnWorld = data.getString("SpawnWorld");
            if (spawnWorld.equals("")) {
                spawnWorld = this.server.getWorlds().get(0).getName();
            }
            return new Location(this.server.getWorld(spawnWorld), data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"));
        }
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue metadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, metadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin plugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, plugin);
    }

    private ServerStatsCounter getStatisticManager() {
        return this.server.getHandle().getPlayerStats(this.getUniqueId(), this.getName());
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, null);
            manager.save();
        }
    }

    @Override
    public int getStatistic(Statistic statistic) {
        if (this.isOnline()) {
            return this.getPlayer().getStatistic(statistic);
        } else {
            return CraftStatistic.getStatistic(this.getStatisticManager(), statistic);
        }
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, amount, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, amount, null);
            manager.save();
        }
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        if (this.isOnline()) {
            this.getPlayer().setStatistic(statistic, newValue);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.setStatistic(manager, statistic, newValue, null);
            manager.save();
        }
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, material);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, material, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic, material);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, material, null);
            manager.save();
        }
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        if (this.isOnline()) {
            return this.getPlayer().getStatistic(statistic, material);
        } else {
            return CraftStatistic.getStatistic(this.getStatisticManager(), statistic, material);
        }
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, material, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, material, amount, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic, material, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, material, amount, null);
            manager.save();
        }
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        if (this.isOnline()) {
            this.getPlayer().setStatistic(statistic, material, newValue);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.setStatistic(manager, statistic, material, newValue, null);
            manager.save();
        }
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, entityType);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, entityType, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic, entityType);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, entityType, null);
            manager.save();
        }
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        if (this.isOnline()) {
            return this.getPlayer().getStatistic(statistic, entityType);
        } else {
            return CraftStatistic.getStatistic(this.getStatisticManager(), statistic, entityType);
        }
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, entityType, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.incrementStatistic(manager, statistic, entityType, amount, null);
            manager.save();
        }
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        if (this.isOnline()) {
            this.getPlayer().decrementStatistic(statistic, entityType, amount);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.decrementStatistic(manager, statistic, entityType, amount, null);
            manager.save();
        }
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        if (this.isOnline()) {
            this.getPlayer().setStatistic(statistic, entityType, newValue);
        } else {
            ServerStatsCounter manager = this.getStatisticManager();
            CraftStatistic.setStatistic(manager, statistic, entityType, newValue, null);
            manager.save();
        }
    }
}
