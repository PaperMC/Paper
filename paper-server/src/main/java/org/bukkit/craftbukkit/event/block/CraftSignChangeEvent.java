package org.bukkit.craftbukkit.event.block;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.block.Block;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.UnmodifiableView;
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

    public CraftSignChangeEvent(
        final SignBlockEntity sign,
        final net.minecraft.world.entity.player.Player player,
        final List<FilteredText> lines,
        final SignText text,
        final boolean isFrontText
    ) {
        final List<Component> components = new ArrayList<>();
        for (int i = 0; i < lines.size(); ++i) {
            components.add(PaperAdventure.asAdventure(text.getMessage(i, player.isTextFilteringEnabled())));
        }
        this(
            CraftBlock.at(sign.getLevel(), sign.getBlockPos()),
            (Player) player.getBukkitEntity(),
            components,
            isFrontText ? Side.FRONT : Side.BACK
        );
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public @UnmodifiableView List<Component> lines() {
        return Collections.unmodifiableList(this.lines);
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
        return this.lines.stream().map(LegacyComponentSerializer.legacySection()::serializeOrNull).toArray(String[]::new);
    }

    @Override
    public @Nullable String getLine(final int index) throws IndexOutOfBoundsException {
        return LegacyComponentSerializer.legacySection().serializeOrNull(this.lines.get(index));
    }

    @Override
    public void setLine(final int index, final @Nullable String line) throws IndexOutOfBoundsException {
        this.lines.set(index, LegacyComponentSerializer.legacySection().deserializeOrNull(line));
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
