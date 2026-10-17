package io.papermc.paper.connection;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class WorldListReconfiguration {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final MinecraftServer server;
    private boolean pending;
    private boolean warnedLegacyLogin;

    public WorldListReconfiguration(MinecraftServer server) {
        this.server = server;
    }

    public void requestRefresh() {
        this.pending = true;
    }

    public void tick() {
        if (!this.pending) {
            return;
        }
        this.pending = false;
        if (!GlobalConfiguration.get().misc.reconfigureOnWorldChanges) {
            return;
        }
        final var worlds = this.server.levelKeys();
        for (ServerPlayer player : List.copyOf(this.server.getPlayerList().getPlayers())) {
            if (player.connection == null) {
                continue;
            }
            if (player.connection.reconfigureForWorldList(worlds) == Result.LEGACY_LOGIN && !this.warnedLegacyLogin) {
                this.warnedLegacyLogin = true;
                LOGGER.warn("Automatic world-list reconfiguration is unavailable for connections handled through PlayerLoginEvent. These players must reconnect to refresh their world lists.");
            }
        }
    }

    public enum Result {
        UNCHANGED,
        BUSY,
        LEGACY_LOGIN,
        STARTED
    }
}
