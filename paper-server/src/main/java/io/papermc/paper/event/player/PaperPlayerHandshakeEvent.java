package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerHandshakeEvent;
import com.google.common.base.Preconditions;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerHandshakeEvent extends CraftEvent implements PlayerHandshakeEvent {

    private final String originalHandshake;
    private final String originalSocketAddressHostname;
    private @Nullable String serverHostname;
    private @Nullable String socketAddressHostname;
    private @Nullable UUID uniqueId;
    private @Nullable String propertiesJson;
    private boolean failed;
    private Component failMessage = Component.text("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!", NamedTextColor.YELLOW);

    private boolean cancelled;

    public PaperPlayerHandshakeEvent(final String originalHandshake, final String originalSocketAddressHostname, final boolean cancelled) {
        super(true);
        this.originalHandshake = originalHandshake;
        this.originalSocketAddressHostname = originalSocketAddressHostname;
        this.cancelled = cancelled;
    }

    @Override
    public String getOriginalHandshake() {
        return this.originalHandshake;
    }

    @Override
    public String getOriginalSocketAddressHostname() {
        return this.originalSocketAddressHostname;
    }

    @Override
    public @Nullable String getServerHostname() {
        return this.serverHostname;
    }

    @Override
    public void setServerHostname(final String serverHostname) {
        this.serverHostname = serverHostname;
    }

    @Override
    public @Nullable String getSocketAddressHostname() {
        return this.socketAddressHostname;
    }

    @Override
    public void setSocketAddressHostname(final String socketAddressHostname) {
        this.socketAddressHostname = socketAddressHostname;
    }

    @Override
    public @Nullable UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public @Nullable String getPropertiesJson() {
        return this.propertiesJson;
    }

    @Override
    public boolean isFailed() {
        return this.failed;
    }

    @Override
    public void setFailed(final boolean failed) {
        this.failed = failed;
    }

    @Override
    public void setPropertiesJson(final String propertiesJson) {
        this.propertiesJson = propertiesJson;
    }

    @Override
    public Component failMessage() {
        return this.failMessage;
    }

    @Override
    public void failMessage(final Component failMessage) {
        this.failMessage = failMessage;
    }

    @Override
    public String getFailMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.failMessage());
    }

    @Override
    public void setFailMessage(final String failMessage) {
        Preconditions.checkArgument(failMessage != null && !failMessage.isEmpty(), "fail message cannot be null or empty");
        this.failMessage(LegacyComponentSerializer.legacySection().deserialize(failMessage));
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerHandshakeEvent.getHandlerList();
    }
}
