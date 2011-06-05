package org.bukkit.craftbukkit.entity;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet200Statistic;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.server.Packet54PlayNoteBlock;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.ServerConfigurationManager;
import net.minecraft.server.WorldServer;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.Packet9Respawn;
import net.minecraft.server.World;
import org.bukkit.Achievement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

public class CraftPlayer extends CraftHumanEntity implements Player {

    public CraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }

    public boolean isOp() {
        return server.getHandle().isOp(getName());
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
        getHandle().netServerHandler.sendPacket(
                new Packet54PlayNoteBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument, note));
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        Packet53BlockChange packet = new Packet53BlockChange(
                loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                ((CraftWorld) loc.getWorld()).getHandle());
        packet.d = material;
        packet.e = data;
        getHandle().netServerHandler.sendPacket(packet);
    }

    @Override
    public boolean teleport(Location location) {
        WorldServer oldWorld = ((CraftWorld)getWorld()).getHandle();
        WorldServer newWorld = ((CraftWorld)location.getWorld()).getHandle();
        ServerConfigurationManager manager = server.getHandle();
        EntityPlayer entity = getHandle();
        boolean successfulTeleport = entity.netServerHandler.teleport(location);

        if (oldWorld != newWorld && successfulTeleport) {
            this.entity = manager.a(entity, newWorld.dimension, false);
        }
        return successfulTeleport;
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
        getHandle().a(getHandle().activeContainer);
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
}
