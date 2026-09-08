package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CraftPlayerCommandPreprocessEvent extends CraftPlayerEvent implements PlayerCommandPreprocessEvent {

    private Player mutablePlayer;
    private String command;
    private final Supplier<Set<Player>> recipients;

    private boolean cancelled;

    public CraftPlayerCommandPreprocessEvent(final Player player, final String command, final Supplier<Set<Player>> recipients) {
        super(player);
        this.mutablePlayer = player;
        this.recipients = recipients;
        this.command = command;
    }

    public CraftPlayerCommandPreprocessEvent(final ServerPlayer player, final String command, final MinecraftServer server) {
        this(player.getBukkitEntity(), command, Suppliers.memoize(() -> LazyPlayerSet.makePlayerSet(server)));
    }

    @Override
    public String getMessage() {
        return this.command;
    }

    @Override
    public void setMessage(final String command) {
        Preconditions.checkArgument(command != null, "Command cannot be null");
        Preconditions.checkArgument(!command.isEmpty(), "Command cannot be empty");
        this.command = command;
    }

    @Override
    @Deprecated(since = "1.3.1", forRemoval = true)
    public Set<Player> getRecipients() {
        return this.recipients.get();
    }

    @Override
    public Player getPlayer() {
        return this.mutablePlayer;
    }

    @Override
    @Deprecated(forRemoval = true)
    public void setPlayer(final Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        this.mutablePlayer = player;
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
        return PlayerCommandPreprocessEvent.getHandlerList();
    }
}
