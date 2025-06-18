package org.bukkit.craftbukkit.command;

import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.pointer.PointersSupplier;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public abstract class ServerCommandSender implements CommandSender {
    private static final PointersSupplier<ServerCommandSender> POINTERS_SUPPLIER = PointersSupplier.<ServerCommandSender>builder()
        .resolving(net.kyori.adventure.identity.Identity.DISPLAY_NAME, ServerCommandSender::name)
        .resolving(net.kyori.adventure.permission.PermissionChecker.POINTER, serverCommandSender -> serverCommandSender::permissionValue)
        .build();

    public final PermissibleBase perm;

    protected ServerCommandSender() {
        this.perm = new PermissibleBase(this);
    }

    protected ServerCommandSender(PermissibleBase perm) {
        this.perm = perm;
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

    public boolean isPlayer() {
        return false;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        this.sendMessage(message); // ServerCommandSenders have no use for senders
    }

    @Override
    public void sendMessage(UUID uuid, String... messages) {
        this.sendMessage(messages); // ServerCommandSenders have no use for senders
    }

    private final org.bukkit.command.CommandSender.Spigot spigot = new org.bukkit.command.CommandSender.Spigot() {
        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(component));
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(components));
        }

        @Override
        public void sendMessage(UUID sender, net.md_5.bungee.api.chat.BaseComponent... components) {
            this.sendMessage(components);
        }

        @Override
        public void sendMessage(UUID sender, net.md_5.bungee.api.chat.BaseComponent component) {
            this.sendMessage(component);
        }
    };

    @Override
    public org.bukkit.command.CommandSender.Spigot spigot() {
        return this.spigot;
    }

    @Override
    public net.kyori.adventure.pointer.Pointers pointers() {
        return POINTERS_SUPPLIER.view(this);
    }
}
