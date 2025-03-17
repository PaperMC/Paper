package io.papermc.paper.connection;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.connection.common.PlayerConnectionValidateLoginEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// TODO: DO we even need this? Who mutates the player here? it does nothing anyways.
// TODO: Make sure player settings are migrated too
public class HorriblePlayerLoginEventHack {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static boolean nagged = false;

    public static Component execute(final Connection connection, MinecraftServer minecraftServer, GameProfile profile, @NotNull PlayerList.LoginResult result) {
        if (PlayerLoginEvent.getHandlerList().getRegisteredListeners().length == 0) {
            return result.message();
        }

        if (!nagged) {
            LOGGER.warn("Legacy PlayerLoginEvent usage detected. It is encouraged that you do not use this event as it requires specially loading the player differently. This functionality will be removed in a future release.");
            nagged = true;
        }
        // We need to account for the fact that during the config stage we now call this event to mimic the old behavior.
        // However, we want to make sure that we still properly hold the same player reference
        ServerPlayer player;
        if (connection.savedPlayerForLoginEventLegacy != null) {
            player = connection.savedPlayerForLoginEventLegacy;
        } else {
            ServerPlayer serverPlayer = new ServerPlayer(minecraftServer, minecraftServer.getLevel(Level.OVERWORLD), profile, ClientInformation.createDefault());
            connection.savedPlayerForLoginEventLegacy = serverPlayer;
            player = serverPlayer;
        }

        CraftPlayer horribleBukkitPlayer = player.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(horribleBukkitPlayer, connection.hostname, ((java.net.InetSocketAddress) connection.getRemoteAddress()).getAddress(), ((java.net.InetSocketAddress) connection.channel.remoteAddress()).getAddress());
        event.disallow(fromModern(result.result()), PaperAdventure.asAdventure(result.message()));
        event.callEvent();
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            return null;
        } else {
            return PaperAdventure.asVanilla(event.kickMessage());
        }
    }

    private static PlayerConnectionValidateLoginEvent.Result toModern(@NotNull PlayerLoginEvent.Result result) {
        return switch (result) {
            case ALLOWED -> PlayerConnectionValidateLoginEvent.Result.ALLOWED;
            case KICK_OTHER -> PlayerConnectionValidateLoginEvent.Result.KICK_OTHER;
            case KICK_BANNED -> PlayerConnectionValidateLoginEvent.Result.KICK_BANNED;
            case KICK_WHITELIST -> PlayerConnectionValidateLoginEvent.Result.KICK_WHITELIST;
            case KICK_FULL -> PlayerConnectionValidateLoginEvent.Result.KICK_FULL;
        };
    }

    private static PlayerLoginEvent.Result fromModern(@NotNull PlayerConnectionValidateLoginEvent.Result result) {
        return switch (result) {
            case ALLOWED -> PlayerLoginEvent.Result.ALLOWED;
            case KICK_OTHER -> PlayerLoginEvent.Result.KICK_OTHER;
            case KICK_BANNED -> PlayerLoginEvent.Result.KICK_BANNED;
            case KICK_WHITELIST -> PlayerLoginEvent.Result.KICK_WHITELIST;
            case KICK_FULL -> PlayerLoginEvent.Result.KICK_FULL;
        };
    }
}
