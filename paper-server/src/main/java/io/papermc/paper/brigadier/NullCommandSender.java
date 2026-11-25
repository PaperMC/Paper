package io.papermc.paper.brigadier;

import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DefaultQualifier(NonNull.class)
public final class NullCommandSender implements CommandSender {

    public static final CommandSender INSTANCE = new NullCommandSender();

    private static final Pointers POINTERS = Pointers.builder()
        .withStatic(PermissionChecker.POINTER, PermissionChecker.always(TriState.TRUE))
        .build();

    private NullCommandSender() {
    }

    @Override
    public void sendMessage(final String message) {
    }

    @Override
    public void sendMessage(final String... messages) {
    }

    @Override
    public void sendMessage(@Nullable final UUID sender, final String message) {
    }

    @Override
    public void sendMessage(@Nullable final UUID sender, final String... messages) {
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public Server getServer() {
        final @Nullable Server server = Bukkit.getServer();
        if (server == null) {
            throw new UnsupportedOperationException("The server has not been created yet, you cannot access it at this time from the 'null' CommandSender");
        }
        return server;
    }

    @Override
    public String getName() {
        return "";
    }

    private final Spigot spigot = new Spigot();
    @Override
    public Spigot spigot() {
        return this.spigot;
    }

    public final class Spigot extends CommandSender.Spigot {

        @Override
        public void sendMessage(@NotNull final BaseComponent component) {
        }

        @Override
        public void sendMessage(@NonNull final @NotNull BaseComponent... components) {
        }

        @Override
        public void sendMessage(@Nullable final UUID sender, @NotNull final BaseComponent component) {
        }

        @Override
        public void sendMessage(@Nullable final UUID sender, @NonNull final @NotNull BaseComponent... components) {
        }
    }

    @Override
    public Component name() {
        return Component.empty();
    }

    @Override
    public boolean isPermissionSet(final String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(final Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(final String name) {
        return true;
    }

    @Override
    public boolean hasPermission(final Permission perm) {
        return true;
    }

    @Override
    public TriState permissionValue(final String permission) {
        return TriState.TRUE;
    }

    @Override
    public TriState permissionValue(final Permission permission) {
        return TriState.TRUE;
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        throw new UnsupportedOperationException("Cannot add attachments to the 'null' CommandSender");
    }

    @Override
    public PermissionAttachment addAttachment(final Plugin plugin) {
        throw new UnsupportedOperationException("Cannot add attachments to the 'null' CommandSender");
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        throw new UnsupportedOperationException("Cannot add attachments to the 'null' CommandSender");
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        throw new UnsupportedOperationException("Cannot add attachments to the 'null' CommandSender");
    }

    @Override
    public void removeAttachment(final PermissionAttachment attachment) {
        throw new UnsupportedOperationException("Cannot add attachments to the 'null' CommandSender");
    }

    @Override
    public void recalculatePermissions() {
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Cannot remove attachments from the 'null' CommandSender");
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(final boolean value) {
    }

    @Override
    public Pointers pointers() {
        return POINTERS;
    }
}
