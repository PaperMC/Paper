package org.bukkit.permissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds information on a permission and which {@link PermissionAttachment}
 * provides it
 */
public class PermissionAttachmentInfo {
    private final Permissible permissible;
    private final String permission;
    private final PermissionAttachment attachment;
    private final boolean value;

    public PermissionAttachmentInfo(@NotNull Permissible permissible, @NotNull String permission, @Nullable PermissionAttachment attachment, boolean value) {
        if (permissible == null) {
            throw new IllegalArgumentException("Permissible may not be null");
        } else if (permission == null) {
            throw new IllegalArgumentException("Permission may not be null");
        }

        this.permissible = permissible;
        this.permission = permission;
        this.attachment = attachment;
        this.value = value;
    }

    /**
     * Gets the permissible this is attached to
     *
     * @return Permissible this permission is for
     */
    @NotNull
    public Permissible getPermissible() {
        return permissible;
    }

    /**
     * Gets the permission being set
     *
     * @return Name of the permission
     */
    @NotNull
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the attachment providing this permission. This may be null for
     * default permissions (usually parent permissions).
     *
     * @return Attachment
     */
    @Nullable
    public PermissionAttachment getAttachment() {
        return attachment;
    }

    /**
     * Gets the value of this permission
     *
     * @return Value of the permission
     */
    public boolean getValue() {
        return value;
    }
}
