package io.papermc.paper.event.profile;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public class PaperProfileWhitelistVerifyEvent extends CraftEvent implements ProfileWhitelistVerifyEvent {

    private final PlayerProfile profile;
    private final boolean whitelistEnabled;
    private final boolean isOp;
    private boolean whitelisted;
    private @Nullable Component kickMessage;

    public PaperProfileWhitelistVerifyEvent(final PlayerProfile profile, final boolean whitelistEnabled, final boolean whitelisted, final boolean isOp, final @Nullable Component kickMessage) {
        this.profile = profile;
        this.whitelistEnabled = whitelistEnabled;
        this.whitelisted = whitelisted;
        this.isOp = isOp;
        this.kickMessage = kickMessage;
    }

    @Override
    @Deprecated
    public @Nullable String getKickMessage() {
        return this.kickMessage == null ? null : LegacyComponentSerializer.legacySection().serialize(this.kickMessage);
    }

    @Override
    @Deprecated
    public void setKickMessage(final @Nullable String kickMessage) {
        this.kickMessage(kickMessage == null ? null : LegacyComponentSerializer.legacySection().deserialize(kickMessage));
    }

    @Override
    @Contract(pure = true)
    public @Nullable Component kickMessage() {
        return this.kickMessage;
    }

    @Override
    public void kickMessage(final @Nullable Component kickMessage) {
        this.kickMessage = kickMessage;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public boolean isWhitelisted() {
        return this.whitelisted;
    }

    @Override
    public void setWhitelisted(final boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    @Override
    public boolean isOp() {
        return this.isOp;
    }

    @Override
    public boolean isWhitelistEnabled() {
        return this.whitelistEnabled;
    }

    @Override
    public HandlerList getHandlers() {
        return ProfileWhitelistVerifyEvent.getHandlerList();
    }
}
