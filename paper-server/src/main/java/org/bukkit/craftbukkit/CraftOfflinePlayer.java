package org.bukkit.craftbukkit;

import io.papermc.paper.statistic.PaperStatistics;
import io.papermc.paper.statistic.Statistic;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;

@SerializableAs("Player")
public class CraftOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {
    private final NameAndId nameAndId;
    private final CraftServer server;
    private final PlayerDataStorage storage;

    protected CraftOfflinePlayer(CraftServer server, NameAndId nameAndId) {
        this.server = server;
        this.nameAndId = nameAndId;
        this.storage = server.console.playerDataStorage;
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String getName() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getName();
        }

        // This might not match lastKnownName but if not it should be more correct
        if (!this.nameAndId.name().isEmpty()) {
            return this.nameAndId.name();
        }

        CompoundTag data = this.getBukkitData();

        if (data != null) {
            return data.getString("lastKnownName").orElse(null);
        }

        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.nameAndId.id();
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() { // Paper
        return com.destroystokyo.paper.profile.CraftPlayerProfile.asBukkitCopy(this.nameAndId.toUncompletedGameProfile()); // Paper
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean isOp() {
        return this.server.getHandle().isOp(this.nameAndId);
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) {
            return;
        }

        if (value) {
            this.server.getHandle().op(this.nameAndId);
        } else {
            this.server.getHandle().deop(this.nameAndId);
        }
    }

    @Override
    public boolean isBanned() {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).isBanned(this.getPlayerProfile());
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Date expires, String source) { // Paper - fix ban list API
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Instant expires, String source) { // Paper - fix ban list API
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Duration duration, String source) { // Paper - fix ban list API
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
        return this.server.getHandle().getWhiteList().isWhiteListed(this.nameAndId);
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().getWhiteList().add(new UserWhiteListEntry(this.nameAndId));
        } else {
            this.server.getHandle().getWhiteList().remove(this.nameAndId);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("UUID", this.nameAndId.id().toString());

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
        return this.getClass().getSimpleName() + "[UUID=" + this.nameAndId.id() + "]";
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

        return this.getUniqueId().equals(other.getUniqueId());
    }

    @Override
    public int hashCode() {
        int hash = 5;

        hash = 97 * hash + this.getUniqueId().hashCode();
        return hash;
    }

    private CompoundTag getData() {
        // This method does not use the problem reporter
        return this.storage.load(this.nameAndId).orElse(null);
    }

    private CompoundTag getBukkitData() {
        CompoundTag result = this.getData();

        if (result != null) {
            result = result.getCompound("bukkit").orElse(null);
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
            return data.getLong("firstPlayed").orElseGet(() -> {
                File file = this.getDataFile();
                return file.lastModified();
            });
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
            return data.getLong("lastPlayed").orElseGet(() -> {
                File file = this.getDataFile();
                return file.lastModified();
            });
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.getData() != null;
    }

    @Override
    public long getLastLogin() {
        Player player = this.getPlayer();
        if (player != null) return player.getLastLogin();

        CompoundTag data = this.getPaperData();

        if (data != null) {
            return data.getLong("LastLogin").orElseGet(() -> {
                // if the player file cannot provide accurate data, this is probably the closest we can approximate
                File file = getDataFile();
                return file.lastModified();
            });
        } else {
            return 0;
        }
    }

    @Override
    public long getLastSeen() {
        Player player = this.getPlayer();
        if (player != null) return player.getLastSeen();

        CompoundTag data = this.getPaperData();

        if (data != null) {
            return data.getLong("LastSeen").orElseGet(() -> {
                // if the player file cannot provide accurate data, this is probably the closest we can approximate
                File file = getDataFile();
                return file.lastModified();
            });
        } else {
            return 0;
        }
    }

    private CompoundTag getPaperData() {
        CompoundTag result = this.getData();

        if (result != null) {
            result = result.getCompound("Paper").orElse(null);
        }

        return result;
    }

    private static final org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry();
    private io.papermc.paper.persistence.@org.checkerframework.checker.nullness.qual.MonotonicNonNull PersistentDataContainerView persistentDataContainerView;

    @Override
    public io.papermc.paper.persistence.PersistentDataContainerView getPersistentDataContainer() {
        if (this.persistentDataContainerView == null) {
            this.persistentDataContainerView = new io.papermc.paper.persistence.PaperPersistentDataContainerView(DATA_TYPE_REGISTRY) {

                private CompoundTag getPersistentTag() {
                    return net.minecraft.Optionull.map(CraftOfflinePlayer.this.getData(), data -> data.getCompound("BukkitValues").orElse(null));
                }

                @Override
                public CompoundTag toTagCompound() {
                    return java.util.Objects.requireNonNullElseGet(this.getPersistentTag(), CompoundTag::new);
                }

                @Override
                public net.minecraft.nbt.Tag getTag(String key) {
                    return net.minecraft.Optionull.map(this.getPersistentTag(), tag -> tag.get(key));
                }
            };
        }
        return this.persistentDataContainerView;
    }

    @Override
    public Location getLastDeathLocation() {
        CompoundTag data = this.getData();
        if (data == null) {
            return null;
        }

        return data.read("LastDeathLocation", GlobalPos.CODEC).map(CraftLocation::fromGlobalPos).orElse(null);
    }

    @Override
    public Location getLocation() {
        CompoundTag data = this.getData();
        if (data == null) {
            return null;
        }

        Vec3 pos = data.read("Pos", Vec3.CODEC).orElse(null);
        Vec2 rot = data.read("Rotation", Vec2.CODEC).orElse(null);
        if (pos != null && rot != null) {
            Long msb = data.getLong("WorldUUIDMost").orElse(null);
            Long lsb = data.getLong("WorldUUIDLeast").orElse(null);
            World world = msb != null && lsb != null ? this.server.getWorld(new UUID(msb, lsb)) : null;

            return new Location(
                world,
                pos.x(), pos.y(), pos.z(),
                rot.x, rot.y
            );
        }

        return null;
    }

    @Override
    public Location getRespawnLocation(final boolean loadLocationAndValidate) {
        final CompoundTag data = this.getData();
        if (data == null) return null;

        final ServerPlayer.RespawnConfig respawnConfig = data.read("respawn", ServerPlayer.RespawnConfig.CODEC).orElse(null);
        if (respawnConfig == null) return null;

        final ServerLevel level = this.server.console.getLevel(respawnConfig.respawnData().dimension());
        if (level == null) return null;

        if (!loadLocationAndValidate) {
            return CraftLocation.toBukkit(respawnConfig.respawnData().pos(), level, respawnConfig.respawnData().yaw(), respawnConfig.respawnData().pitch());
        }

        return ServerPlayer.findRespawnAndUseSpawnBlock(level, respawnConfig, false)
            .map(resolvedPos -> CraftLocation.toBukkit(resolvedPos.position(), level, resolvedPos.yaw(), resolvedPos.pitch()))
            .orElse(null);
    }

    private ServerStatsCounter getStatisticManager() {
        return this.server.getHandle().getPlayerStats(this.getUniqueId(), this.getName());
    }

    @Override
    public void incrementStatistic(final Statistic<?> statistic, final int amount) {
        if (this.isOnline()) {
            this.getPlayer().incrementStatistic(statistic, amount);
        } else {
            final ServerStatsCounter manager = this.getStatisticManager();
            PaperStatistics.changeStatistic(manager, statistic, amount);
            manager.save();
        }

    }

    @Override
    public void setStatistic(final Statistic<?> statistic, final int newAmount) {
        if (this.isOnline()) {
            this.getPlayer().setStatistic(statistic, newAmount);
        } else {
            final ServerStatsCounter manager = this.getStatisticManager();
            PaperStatistics.setStatistic(manager, statistic, newAmount);
            manager.save();
        }
    }

    @Override
    public int getStatistic(final Statistic<?> statistic) {
        if (this.isOnline()) {
            return this.getPlayer().getStatistic(statistic);
        } else {
            return PaperStatistics.getStatistic(this.getStatisticManager(), statistic);
        }
    }

    @Override
    public String getFormattedValue(final Statistic<?> statistic) {
        if (this.isOnline()) {
            return this.getPlayer().getFormattedValue(statistic);
        } else {
            return PaperStatistics.getFormattedValue(this.getStatisticManager(), statistic);
        }
    }
}
