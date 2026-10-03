package io.papermc.paper.permissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Normal
class PaperPermissionsTest {

    @Test
    void packetLimiterBypassDefaultsToOp() {
        PaperPermissions.registerPermissions();

        final Permission permission = Bukkit.getPluginManager().getPermission(PaperPermissions.PACKET_LIMITER_BYPASS);
        assertNotNull(permission);
        assertEquals(PermissionDefault.OP, permission.getDefault());
    }

    @Test
    void packetLimiterBypassCanBeGrantedAndRevoked() {
        PaperPermissions.registerPermissions();
        final MutableOperator operator = new MutableOperator(false);
        final PermissibleBase permissible = new PermissibleBase(operator);
        final Plugin plugin = enabledPlugin();

        final PermissionAttachment attachment = permissible.addAttachment(plugin, PaperPermissions.PACKET_LIMITER_BYPASS, true);
        assertTrue(permissible.hasPermission(PaperPermissions.PACKET_LIMITER_BYPASS));

        attachment.unsetPermission(PaperPermissions.PACKET_LIMITER_BYPASS);
        assertFalse(permissible.hasPermission(PaperPermissions.PACKET_LIMITER_BYPASS));
    }

    @Test
    void explicitDenyOverridesOpDefault() {
        PaperPermissions.registerPermissions();
        final MutableOperator operator = new MutableOperator(true);
        final PermissibleBase permissible = new PermissibleBase(operator);

        assertTrue(permissible.hasPermission(PaperPermissions.PACKET_LIMITER_BYPASS));
        permissible.addAttachment(enabledPlugin(), PaperPermissions.PACKET_LIMITER_BYPASS, false);
        assertFalse(permissible.hasPermission(PaperPermissions.PACKET_LIMITER_BYPASS));
    }

    private static Plugin enabledPlugin() {
        final Plugin plugin = mock(Plugin.class);
        when(plugin.isEnabled()).thenReturn(true);
        return plugin;
    }

    private static final class MutableOperator implements ServerOperator {

        private boolean op;

        private MutableOperator(final boolean op) {
            this.op = op;
        }

        @Override
        public boolean isOp() {
            return this.op;
        }

        @Override
        public void setOp(final boolean value) {
            this.op = value;
        }
    }
}
