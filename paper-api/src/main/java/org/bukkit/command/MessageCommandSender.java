package org.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * For when all you care about is just messaging
 *
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
public interface MessageCommandSender extends CommandSender {

    @Override
    default void sendMessage(@NotNull String @NotNull [] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    default void sendMessage(@Nullable UUID sender, @NotNull String message) {
        sendMessage(message);
    }

    @Override
    default void sendMessage(@Nullable UUID sender, @NotNull String @NotNull [] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @NotNull
    @Override
    default Server getServer() {
        return Bukkit.getServer();
    }

    // Paper start
    @Override
    default net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component name() {
        throw new UnsupportedOperationException();
    }
    // Paper end

    @NotNull
    @Override
    default String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isOp() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isPermissionSet(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isPermissionSet(@NotNull Permission perm) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean hasPermission(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean hasPermission(@NotNull Permission perm) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void removeAttachment(@NotNull PermissionAttachment attachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default Spigot spigot() {
        throw new UnsupportedOperationException();
    }

}
