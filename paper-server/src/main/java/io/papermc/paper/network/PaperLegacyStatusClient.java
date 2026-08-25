package io.papermc.paper.network;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.network.StatusClient;
import java.net.InetSocketAddress;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.ChatFormatting;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

public record PaperLegacyStatusClient(InetSocketAddress address, int protocolVersion, @Nullable InetSocketAddress virtualHost) implements StatusClient {

    @Override
    public boolean isLegacy() {
        return true;
    }

    @SuppressWarnings("deprecation") // Valid as this is the legacy status client
    public static String getMotd(final PaperServerListPingEvent event) {
        return getFirstLine(event.getMotd());
    }

    public static String getUnformattedMotd(final PaperServerListPingEvent event) {
        // Strip color codes and all other occurrences of the color char (because it's used as delimiter)
        return getFirstLine(StringUtils.remove(PlainTextComponentSerializer.plainText().serialize(event.motd()), ChatFormatting.PREFIX_CODE));
    }

    private static String getFirstLine(final String s) {
        final int pos = s.indexOf('\n');
        return pos >= 0 ? s.substring(0, pos) : s;
    }
}
