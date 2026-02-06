package io.papermc.paper.event.player;

import io.papermc.paper.event.packet.UncheckedSignChangeEvent;
import io.papermc.paper.math.BlockPosition;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

public class PaperUncheckedSignChangeEvent extends CraftPlayerEvent implements UncheckedSignChangeEvent {

    private final BlockPosition editedBlockPosition;
    private final Side side;
    private final List<Component> lines;

    private boolean cancelled;

    public PaperUncheckedSignChangeEvent(
        final Player editor,
        final BlockPosition editedBlockPosition,
        final Side side,
        final List<Component> lines
    ) {
        super(editor);
        this.editedBlockPosition = editedBlockPosition;
        this.side = side;
        this.lines = lines;
    }

    @Override
    public BlockPosition getEditedBlockPosition() {
        return this.editedBlockPosition;
    }

    @Override
    public Side getSide() {
        return this.side;
    }

    @Override
    public @Unmodifiable List<Component> lines() {
        return Collections.unmodifiableList(this.lines);
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
        return UncheckedSignChangeEvent.getHandlerList();
    }
}
