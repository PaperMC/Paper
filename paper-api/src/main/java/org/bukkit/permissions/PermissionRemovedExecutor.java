package org.bukkit.permissions;

import org.jetbrains.annotations.NotNull;

/// Represents a class which is to be notified when a
/// [PermissionAttachment] is removed from a [Permissible]
public interface PermissionRemovedExecutor {

    /// Called when a [PermissionAttachment] is removed from a
    /// [Permissible]
    ///
    /// @param attachment Attachment which was removed
    public void attachmentRemoved(@NotNull PermissionAttachment attachment);
}
