package org.bukkit.craftbukkit.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.connection.PlayerLoginConnection;
import java.net.InetAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class CraftAsyncPlayerPreLoginEvent extends CraftEvent implements AsyncPlayerPreLoginEvent {

    private final InetAddress ipAddress;
    private final InetAddress rawAddress;
    private final String hostname;
    private final boolean transferred;
    private AsyncPlayerPreLoginEvent.Result result;
    private Component message;
    private PlayerProfile profile;
    private final PlayerLoginConnection connection;

    public CraftAsyncPlayerPreLoginEvent(final String name, final InetAddress ipAddress, final InetAddress rawAddress, final UUID uniqueId, final boolean transferred, final PlayerProfile profile, final String hostname, final PlayerLoginConnection connection) {
        super(true);
        this.result = AsyncPlayerPreLoginEvent.Result.ALLOWED;
        this.message = Component.empty();
        this.profile = profile;
        this.ipAddress = ipAddress;
        this.rawAddress = rawAddress;
        this.hostname = hostname;
        this.transferred = transferred;
        this.connection = connection;
    }

    @Override
    public AsyncPlayerPreLoginEvent.Result getLoginResult() {
        return this.result;
    }

    @Override
    @Deprecated(since = "1.3.2")
    public PlayerPreLoginEvent.Result getResult() {
        return this.result == null ? null : PlayerPreLoginEvent.Result.valueOf(this.result.name()); // todo a lot of nullability issues in this class + player profile
    }

    @Override
    public void setLoginResult(final AsyncPlayerPreLoginEvent.Result result) {
        this.result = result;
    }

    @Override
    @Deprecated(since = "1.3.2")
    public void setResult(final PlayerPreLoginEvent.Result result) {
        this.result = result == null ? null : AsyncPlayerPreLoginEvent.Result.valueOf(result.name());
    }

    @Override
    public Component kickMessage() {
        return this.message;
    }

    @Override
    public void kickMessage(final Component message) {
        this.message = message;
    }

    @Override
    public void disallow(final AsyncPlayerPreLoginEvent.Result result, final Component message) {
        this.result = result;
        this.message = message;
    }

    @Override
    @Deprecated
    public void disallow(final PlayerPreLoginEvent.Result result, final Component message) {
        this.result = result == null ? null : AsyncPlayerPreLoginEvent.Result.valueOf(result.name());
        this.message = message;
    }

    @Override
    @Deprecated
    public String getKickMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    @Override
    @Deprecated
    public void setKickMessage(final String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @Override
    public void allow() {
        this.result = AsyncPlayerPreLoginEvent.Result.ALLOWED;
        this.message = Component.empty();
    }

    @Override
    @Deprecated
    public void disallow(final AsyncPlayerPreLoginEvent.Result result, final String message) {
        this.result = result;
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @Override
    @Deprecated(since = "1.3.2")
    public void disallow(final PlayerPreLoginEvent.Result result, final String message) {
        this.result = result == null ? null : AsyncPlayerPreLoginEvent.Result.valueOf(result.name());
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @Override
    public String getName() {
        return this.profile.getName();
    }

    @Override
    public InetAddress getAddress() {
        return this.ipAddress;
    }

    @Override
    public UUID getUniqueId() {
        return this.profile.getId();
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public void setPlayerProfile(final PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public InetAddress getRawAddress() {
        return this.rawAddress;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public boolean isTransferred() {
        return this.transferred;
    }

    @Override
    public PlayerLoginConnection getConnection() {
        return this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerPreLoginEvent.getHandlerList();
    }
}
