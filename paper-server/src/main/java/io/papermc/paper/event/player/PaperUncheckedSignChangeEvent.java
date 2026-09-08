package io.papermc.paper.event.player;

import com.google.common.collect.Lists;
import io.papermc.paper.event.packet.UncheckedSignChangeEvent;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.util.MCUtil;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

public class PaperUncheckedSignChangeEvent extends CraftPlayerEvent implements UncheckedSignChangeEvent {

    private final BlockPosition editedPos;
    private final Side side;
    private final List<Component> lines;

    private boolean cancelled;

    public PaperUncheckedSignChangeEvent(final Player editor, final BlockPosition editedPos, final Side side, final List<Component> lines) {
        super(editor);
        this.editedPos = editedPos;
        this.side = side;
        this.lines = Collections.unmodifiableList(lines);
    }

    public PaperUncheckedSignChangeEvent(
        final ServerPlayer editor,
        final BlockPos editedPos,
        final boolean isFrontText,
        final List<FilteredText> lines
    ) {
        this(
            editor.getBukkitEntity(),
            MCUtil.toPosition(editedPos),
            isFrontText ? Side.FRONT : Side.BACK,
            Lists.transform(lines, line -> Component.text(line.raw()))
        );
    }

    @Override
    public BlockPosition getEditedBlockPosition() {
        return this.editedPos;
    }

    @Override
    public Side getSide() {
        return this.side;
    }

    @Override
    public @Unmodifiable List<Component> lines() {
        return this.lines;
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
