package org.bukkit.craftbukkit.entity;

import java.util.Set;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftMinecartCommand extends CraftMinecart implements CommandMinecart {
    private final PermissibleBase perm = new PermissibleBase(this);

    public CraftMinecartCommand(CraftServer server, MinecartCommandBlock entity) {
        super(server, entity);
    }

    @Override
    public MinecartCommandBlock getHandle() {
        return (MinecartCommandBlock) this.entity;
    }

    @Override
    public String getCommand() {
        return this.getHandle().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(String command) {
        this.getHandle().getCommandBlock().setCommand(command != null ? command : "");
        this.getHandle().getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, this.getHandle().getCommandBlock().getCommand());
    }

    @Override
    public void setName(String name) {
        this.getHandle().getCommandBlock().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public String toString() {
        return "CraftMinecartCommand";
    }

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendMessage(String... messages) {
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(this.getHandle().getCommandBlock().getName());
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a minecart");
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return this.perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return this.perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }
}
