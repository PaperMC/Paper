package io.papermc.paper.adventure;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public final class DisplayNames {
    private DisplayNames() {
    }

    public static String getLegacy(final CraftPlayer player) {
        return getLegacy(player.getHandle());
    }

    @SuppressWarnings("deprecation") // Valid suppress due to supporting legacy display name formatting
    public static String getLegacy(final ServerPlayer player) {
        final String legacy = player.displayName;
        if (legacy != null) {
            // thank you for being worse than wet socks, Bukkit
            return LegacyComponentSerializer.legacySection().serialize(player.adventure$displayName) + ChatColor.getLastColors(player.displayName);
        }
        return LegacyComponentSerializer.legacySection().serialize(player.adventure$displayName);
    }
}
