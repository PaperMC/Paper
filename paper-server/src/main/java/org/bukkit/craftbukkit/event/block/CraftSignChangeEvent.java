package org.bukkit.craftbukkit.event.block;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;
import org.jspecify.annotations.Nullable;

public class CraftSignChangeEvent extends CraftBlockEvent implements SignChangeEvent {

    private final Player player;
    private final List<Component> lines;
    private final Side side;

    private boolean cancelled;

    public CraftSignChangeEvent(final Block sign, final Player player, final List<Component> lines, final Side side) {
        super(sign);
        this.player = player;
        this.lines = lines;
        this.side = side;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public List<Component> lines() {
        return this.lines;
    }

    @Override
    public @Nullable Component line(final int index) throws IndexOutOfBoundsException {
        return this.lines.get(index);
    }

    @Override
    public void line(final int index, final @Nullable Component line) throws IndexOutOfBoundsException {
        this.lines.set(index, line);
    }

    @Override
    public String[] getLines() {
        return this.lines.stream().map(LegacyComponentSerializer.legacySection()::serialize).toArray(String[]::new);
    }

    @Override
    public @Nullable String getLine(final int index) throws IndexOutOfBoundsException {
        return LegacyComponentSerializer.legacySection().serialize(this.lines.get(index));
    }

    @Override
    public void setLine(final int index, final @Nullable String line) throws IndexOutOfBoundsException {
        this.lines.set(index, line != null ? LegacyComponentSerializer.legacySection().deserialize(line) : null);
    }

    @Override
    public Side getSide() {
        return this.side;
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
        return SignChangeEvent.getHandlerList();
    }
}
