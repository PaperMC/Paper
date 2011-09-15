package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet131;
import net.minecraft.server.Packet200Statistic;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet51MapChunk;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.server.Packet54PlayNoteBlock;
import net.minecraft.server.Packet61;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.Packet70Bed;
import net.minecraft.server.WorldServer;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.map.MapView;

public class CraftPlayer extends CraftHumanEntity implements Player {
    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOp(getName());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().e(getName());
        } else {
            server.getHandle().f(getName());
        }

        perm.recalculatePermissions();
    }

    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        for (Object obj: server.getHandle().players) {
            EntityPlayer player = (EntityPlayer) obj;
            if (player.name.equalsIgnoreCase(getName())) {
                return true;
            }
        }
        return false;
    }

    public InetSocketAddress getAddress() {
        SocketAddress addr = getHandle().netServerHandler.networkManager.getSocketAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        if (ignoreSneaking) {
            return 1.62D;
        } else {
            if (isSneaking()) {
                return 1.42D;
            } else {
                return 1.62D;
            }
        }
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle((EntityHuman) entity);
        this.entity = entity;
    }

    public void sendRawMessage(String message) {
        getHandle().netServerHandler.sendPacket(new Packet3Chat(message));
    }

    public void sendMessage(String message) {
        this.sendRawMessage(message);
    }

    public String getDisplayName() {
        return getHandle().displayName;
    }

    public void setDisplayName(final String name) {
        getHandle().displayName = name;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftPlayer other = (CraftPlayer) obj;
        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        return hash;
    }

    public void kickPlayer(String message) {
        getHandle().netServerHandler.disconnect(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().netServerHandler.sendPacket(new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    public void chat(String msg) {
        getHandle().netServerHandler.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    public void playNote(Location loc, byte instrument, byte note) {
        getHandle().netServerHandler.sendPacket(new Packet54PlayNoteBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument, note));
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        getHandle().netServerHandler.sendPacket(new Packet54PlayNoteBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument.getType(), note.getId()));
    }

    public void playEffect(Location loc, Effect effect, int data) {
        int packetData = effect.getId();
        Packet61 packet = new Packet61(packetData, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), data);
        getHandle().netServerHandler.sendPacket(packet);
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        Packet53BlockChange packet = new Packet53BlockChange(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), ((CraftWorld) loc.getWorld()).getHandle());

        packet.material = material;
        packet.data = data;
        getHandle().netServerHandler.sendPacket(packet);
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int cx = x >> 4;
        int cz = z >> 4;

        if (sx <= 0 || sy <= 0 || sz <= 0) {
            return false;
        }

        if ((x + sx - 1) >> 4 != cx || (z + sz - 1) >> 4 != cz || y < 0 || y + sy > 128) {
            return false;
        }

        if (data.length != (sx * sy * sz * 5) / 2) {
            return false;
        }

        Packet51MapChunk packet = new Packet51MapChunk(x, y, z, sx, sy, sz, data);

        getHandle().netServerHandler.sendPacket(packet);

        return true;
    }

    public void sendMap(MapView map) {
        RenderData data = ((CraftMapView) map).render(this);
        for (int x = 0; x < 128; ++x) {
            byte[] bytes = new byte[131];
            bytes[1] = (byte) x;
            for (int y = 0; y < 128; ++y) {
                bytes[y + 3] = data.buffer[y * 128 + x];
            }
            Packet131 packet = new Packet131((short) Material.MAP.getId(), map.getId(), bytes);
            getHandle().netServerHandler.sendPacket(packet);
        }
    }

    @Override
    public boolean teleport(Location location) {
        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent((Player) this, from, to);
        server.getPluginManager().callEvent(event);
        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled() == true) {
            return false;
        }
        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        WorldServer fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();
        // Grab the EntityPlayer
        EntityPlayer entity = getHandle();

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.netServerHandler.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld.dimension, to);
        }
        return true;
    }

    public void setSneaking(boolean sneak) {
        getHandle().setSneak(sneak);
    }

    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    public void loadData() {
        server.getHandle().playerFileData.b(getHandle());
    }

    public void saveData() {
        server.getHandle().playerFileData.a(getHandle());
    }

    public void updateInventory() {
        getHandle().updateInventory(getHandle().activeContainer);
    }

    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().checkSleepStatus();
    }

    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    public void awardAchievement(Achievement achievement) {
        sendStatistic(achievement.getId(), 1);
    }

    public void incrementStatistic(Statistic statistic) {
        incrementStatistic(statistic, 1);
    }

    public void incrementStatistic(Statistic statistic, int amount) {
        sendStatistic(statistic.getId(), amount);
    }

    public void incrementStatistic(Statistic statistic, Material material) {
        incrementStatistic(statistic, material, 1);
    }

    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        if (!statistic.isSubstatistic()) {
            throw new IllegalArgumentException("Given statistic is not a substatistic");
        }
        if (statistic.isBlock() != material.isBlock()) {
            throw new IllegalArgumentException("Given material is not valid for this substatistic");
        }

        int mat = material.getId();

        if (!material.isBlock()) {
            mat -= 255;
        }

        sendStatistic(statistic.getId() + mat, amount);
    }

    private void sendStatistic(int id, int amount) {
        while (amount > Byte.MAX_VALUE) {
            sendStatistic(id, Byte.MAX_VALUE);
            amount -= Byte.MAX_VALUE;
        }

        getHandle().netServerHandler.sendPacket(new Packet200Statistic(id, amount));
    }

    public void setPlayerTime(long time, boolean relative) {
        getHandle().timeOffset = time;
        getHandle().relativeTime = relative;
    }

    public long getPlayerTimeOffset() {
        return getHandle().timeOffset;
    }

    public long getPlayerTime() {
        return getHandle().getPlayerTime();
    }

    public boolean isPlayerTimeRelative() {
        return getHandle().relativeTime;
    }

    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    public boolean isBanned() {
        return server.getHandle().banByName.contains(getName().toLowerCase());
    }

    public void setBanned(boolean value) {
        if (value) {
            server.getHandle().a(getName().toLowerCase());
        } else {
            server.getHandle().b(getName().toLowerCase());
        }
    }

    public boolean isWhitelisted() {
        return server.getHandle().e().contains(getName().toLowerCase());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().k(getName().toLowerCase());
        } else {
            server.getHandle().l(getName().toLowerCase());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        if (mode != getGameMode()) {
            getHandle().itemInWorldManager.a(mode.getValue());
            getHandle().netServerHandler.sendPacket(new Packet70Bed(3, mode.getValue()));
        }
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().itemInWorldManager.a());
    }

    public int getExperience() {
        return getHandle().exp;
    }

    public void setExperience(int exp) {
        getHandle().d(exp - getExperience());
    }

    public int getLevel() {
        return getHandle().expLevel;
    }

    public void setLevel(int level) {
        getHandle().expLevel = level;
    }

    public int getTotalExperience() {
        return getHandle().expTotal;
    }

    public void setTotalExperience(int exp) {
        getHandle().expTotal = exp;

        if (getTotalExperience() > getExperience()) {
            getHandle().exp = getTotalExperience();
        }
    }

    public float getExhaustion() {
        return getHandle().getFoodData().exhaustionLevel;
    }

    public void setExhaustion(float value) {
        getHandle().getFoodData().exhaustionLevel = value;
    }

    public float getSaturation() {
        return getHandle().getFoodData().saturationLevel;
    }

    public void setSaturation(float value) {
        getHandle().getFoodData().saturationLevel = value;
    }

    public int getFoodLevel() {
        return getHandle().getFoodData().foodLevel;
    }

    public void setFoodLevel(int value) {
        getHandle().getFoodData().foodLevel = value;
    }
}
