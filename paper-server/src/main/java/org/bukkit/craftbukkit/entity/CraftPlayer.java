package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.*;
import org.bukkit.*;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private Set<String> channels = new HashSet<String>();
    private Set<Player> hiddenPlayers = new HashSet<Player>();
    private int hash = 0;

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOp(getName());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().addOp(getName());
        } else {
            server.getHandle().removeOp(getName());
        }

        perm.recalculatePermissions();
    }

    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        for (Object obj : server.getHandle().players) {
            EntityPlayer player = (EntityPlayer) obj;
            if (player.name.equalsIgnoreCase(getName())) {
                return true;
            }
        }
        return false;
    }

    public InetSocketAddress getAddress() {
        if (getHandle().netServerHandler == null) return null;

        SocketAddress addr = getHandle().netServerHandler.networkManager.getSocketAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    @Override
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

    public void sendRawMessage(String message) {
        if (getHandle().netServerHandler == null) return;

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

    public String getPlayerListName() {
        return getHandle().listName;
    }

    public void setPlayerListName(String name) {
        String oldName = getHandle().listName;

        if (name == null) {
            name = getName();
        }

        if (oldName.equals(name)) {
            return;
        }

        if (name.length() > 16) {
            throw new IllegalArgumentException("Player list names can only be a maximum of 16 characters long");
        }

        // Collisions will make for invisible people
        for (int i = 0; i < server.getHandle().players.size(); ++i) {
            if (((EntityPlayer) server.getHandle().players.get(i)).listName.equals(name)) {
                throw new IllegalArgumentException(name + " is already assigned as a player list name for someone");
            }
        }

        getHandle().listName = name;

        // Change the name on the client side
        Packet201PlayerInfo oldpacket = new Packet201PlayerInfo(oldName, false, 9999);
        Packet201PlayerInfo packet = new Packet201PlayerInfo(name, true, getHandle().ping);
        for (int i = 0; i < server.getHandle().players.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) server.getHandle().players.get(i);

            if (entityplayer.getBukkitEntity().canSee(this)) {
                entityplayer.netServerHandler.sendPacket(oldpacket);
                entityplayer.netServerHandler.sendPacket(packet);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getName() == null) || (other.getName() == null)) {
            return false;
        }
        return this.getName().equalsIgnoreCase(other.getName());
    }

    public void kickPlayer(String message) {
        if (getHandle().netServerHandler == null) return;

        getHandle().netServerHandler.disconnect(message == null ? "" : message);
    }

    public void setCompassTarget(Location loc) {
        if (getHandle().netServerHandler == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().netServerHandler.sendPacket(new Packet6SpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    public void chat(String msg) {
        if (getHandle().netServerHandler == null) return;

        getHandle().netServerHandler.chat(msg);
    }

    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().netServerHandler == null) return;

        getHandle().netServerHandler.sendPacket(new Packet54PlayNoteBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument, note));
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().netServerHandler == null) return;

        getHandle().netServerHandler.sendPacket(new Packet54PlayNoteBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument.getType(), note.getId()));
    }

    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().netServerHandler == null) return;

        int packetData = effect.getId();
        Packet61WorldEvent packet = new Packet61WorldEvent(packetData, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), data);
        getHandle().netServerHandler.sendPacket(packet);
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        if (getHandle().netServerHandler == null) return;

        Packet53BlockChange packet = new Packet53BlockChange(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), ((CraftWorld) loc.getWorld()).getHandle());

        packet.material = material;
        packet.data = data;
        getHandle().netServerHandler.sendPacket(packet);
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().netServerHandler == null) return false;

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
        if (getHandle().netServerHandler == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        for (int x = 0; x < 128; ++x) {
            byte[] bytes = new byte[131];
            bytes[1] = (byte) x;
            for (int y = 0; y < 128; ++y) {
                bytes[y + 3] = data.buffer[y * 128 + x];
            }
            Packet131ItemData packet = new Packet131ItemData((short) Material.MAP.getId(), map.getId(), bytes);
            getHandle().netServerHandler.sendPacket(packet);
        }
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHandle().netServerHandler == null) return false;

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent((Player) this, from, to, cause);
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
            // Close any foreign inventory
            if (getHandle().activeContainer != getHandle().defaultContainer){
                getHandle().closeInventory();
            }
            server.getHandle().moveToWorld(entity, toWorld.dimension, true, to);
        }
        return true;
    }

    public void setSneaking(boolean sneak) {
        getHandle().setSneak(sneak);
    }

    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    public void loadData() {
        server.getHandle().playerFileData.load(getHandle());
    }

    public void saveData() {
        server.getHandle().playerFileData.save(getHandle());
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
        if (getHandle().netServerHandler == null) return;

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
            server.getHandle().addUserBan(getName().toLowerCase());
        } else {
            server.getHandle().removeUserBan(getName().toLowerCase());
        }
    }

    public boolean isWhitelisted() {
        return server.getHandle().getWhitelisted().contains(getName().toLowerCase());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().addWhitelist(getName().toLowerCase());
        } else {
            server.getHandle().removeWhitelist(getName().toLowerCase());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().netServerHandler == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        if (mode != getGameMode()) {
            PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            getHandle().itemInWorldManager.setGameMode(mode.getValue());
            getHandle().netServerHandler.sendPacket(new Packet70Bed(3, mode.getValue()));
        }
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().itemInWorldManager.getGameMode());
    }

    public void giveExp(int exp) {
        getHandle().giveExp(exp);
    }

    public float getExp() {
        return getHandle().exp;
    }

    public void setExp(float exp) {
        getHandle().exp = exp;

        giveExp(0);
    }

    public int getExperience() {
        return (int) (getExp() * 100);
    }

    public void setExperience(int exp) {
        setExp(exp / 100);
    }

    public int getLevel() {
        return (int) getHandle().expLevel;
    }

    public void setLevel(int level) {
        getHandle().expLevel = level;
        getHandle().lastSentExp = -1;
    }

    public int getTotalExperience() {
        return getHandle().expTotal;
    }

    public void setTotalExperience(int exp) {
        getHandle().expTotal = exp;
        getHandle().lastSentExp = -1;

        if (getTotalExperience() > getExperience()) {
            getHandle().expTotal = getTotalExperience();
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

    public boolean getAllowFlight() {
        return getHandle().itemInWorldManager.player.abilities.canFly;
    }

    public void setAllowFlight(boolean flight) {
        getHandle().itemInWorldManager.player.abilities.canFly = flight;
    }

    public Location getBedSpawnLocation() {
        World world = getServer().getWorld(getHandle().spawnWorld);
        if ((world != null) && (getHandle().getBed() != null)) {
            return new Location(world, getHandle().getBed().x, getHandle().getBed().y, getHandle().getBed().z);
        }
        return null;
    }

    public void setBedSpawnLocation(Location location) {
        getHandle().a(new ChunkCoordinates(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        getHandle().spawnWorld = location.getWorld().getName();
    }

    public void hidePlayer(Player player) {
        if (hiddenPlayers.contains(player)) return;
        hiddenPlayers.add(player);

        //remove this player from the hidden player's EntityTrackerEntry
        EntityTracker tracker = ((WorldServer) entity.world).tracker;
        EntityPlayer other = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities.a(other.id);
        if (entry != null) {
            entry.c(getHandle());
        }

        //remove the hidden player from this player user list
        getHandle().netServerHandler.sendPacket(new Packet201PlayerInfo(player.getPlayerListName(), false, 9999));
    }

    public void showPlayer(Player player) {
        if (!hiddenPlayers.contains(player)) return;
        hiddenPlayers.remove(player);

        EntityTracker tracker = ((WorldServer) entity.world).tracker;
        EntityPlayer other = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntities.a(other.id);
        if (entry != null && !entry.trackedPlayers.contains(getHandle())) {
            entry.b(getHandle());
        }

        getHandle().netServerHandler.sendPacket(new Packet201PlayerInfo(player.getPlayerListName(), true, getHandle().ping));
    }

    public boolean canSee(Player player) {
        return !hiddenPlayers.contains(player);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    public Player getPlayer() {
        return this;
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getName() != null ? this.getName().hashCode() : 0);
        }
        return hash;
    }

    public long getFirstPlayed() {
        return firstPlayed;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(NBTTagCompound nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            NBTTagCompound data = nbttagcompound.getCompound("bukkit");

            if (data.hasKey("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }
        }
    }

    public void setExtraData(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.setCompound("bukkit", new NBTTagCompound());
        }

        NBTTagCompound data = nbttagcompound.getCompound("bukkit");
        data.setLong("firstPlayed", getFirstPlayed());
        data.setLong("lastPlayed", System.currentTimeMillis());
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        if (channels.contains(channel)) {
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.tag = channel;
            packet.length = message.length;
            packet.data = message;
            getHandle().netServerHandler.sendPacket(packet);
        }
    }

    public void addChannel(String channel) {
        channels.add(channel);
    }

    public void removeChannel(String channel) {
        channels.remove(channel);
    }

    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            Packet250CustomPayload packet = new Packet250CustomPayload();

            packet.tag = "REGISTER";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            packet.data = stream.toByteArray();
            packet.length = packet.data.length;

            getHandle().netServerHandler.sendPacket(packet);
        }
    }
}
