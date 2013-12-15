package org.bukkit.permissions;

/**
 * Represents a class which is to be notified when a {@link
 * PermissionAttachment} is removed from a {@link Permissible}
 */
public interface PermissionRemovedExecutor {

    /**
     * Called when a {@link PermissionAttachment} is removed from a {@link
     * Permissible}
     *
     * @param attachment Attachment which was removed
     */
    public void attachmentRemoved(PermissionAttachment attachment);
}
