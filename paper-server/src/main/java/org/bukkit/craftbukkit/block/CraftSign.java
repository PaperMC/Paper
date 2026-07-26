package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.block.sign.CraftSignSide;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSignOpenEvent;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;

public class CraftSign<T extends SignBlockEntity> extends CraftBlockEntityState<T> implements Sign {

    private @MonotonicNonNull CraftSignSide front;
    private @MonotonicNonNull CraftSignSide back;

    public CraftSign(World world, T blockEntity) {
        super(world, blockEntity);
    }

    protected CraftSign(CraftSign<T> state, Location location) {
        super(state, location);
    }

    @Override
    protected void load(T blockEntity) {
        super.load(blockEntity);
        this.front = new CraftSignSide(blockEntity.getFrontText());
        this.back = new CraftSignSide(blockEntity.getBackText());
    }

    // Paper start
    @Override
    public java.util.@NotNull List<net.kyori.adventure.text.Component> lines() {
        return this.front.lines();
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component line(int index) {
        return this.front.line(index);
    }

    @Override
    public void line(int index, net.kyori.adventure.text.@NotNull Component line) {
        this.front.line(index, line);
    }
    // Paper end

    @Override
    public String[] getLines() {
        return this.front.getLines();
    }

    @Override
    public String getLine(int index) throws IndexOutOfBoundsException {
        return this.front.getLine(index);
    }

    @Override
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        this.front.setLine(index, line);
    }

    @Override
    public boolean isEditable() {
        return !this.isWaxed();
    }

    @Override
    public void setEditable(boolean editable) {
        this.setWaxed(!editable);
    }

    @Override
    public boolean isWaxed() {
        return this.getSnapshot().isWaxed();
    }

    @Override
    public void setWaxed(boolean waxed) {
        this.getSnapshot().setWaxed(waxed);
    }

    @Override
    public boolean isGlowingText() {
        return this.front.isGlowingText();
    }

    @Override
    public void setGlowingText(boolean glowing) {
        this.front.setGlowingText(glowing);
    }

    @NotNull
    @Override
    public SignSide getSide(Side side) {
        Preconditions.checkArgument(side != null, "side == null");

        switch (side) {
            case FRONT:
                return this.front;
            case BACK:
                return this.back;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public SignSide getTargetSide(Player player) {
        this.ensureNoWorldGeneration();
        Preconditions.checkArgument(player != null, "player cannot be null");

        if (this.getSnapshot().isFacingFrontText(((CraftPlayer) player).getHandle())) {
            return this.front;
        }

        return this.back;
    }

    @Override
    public Player getAllowedEditor() {
        this.ensureNoWorldGeneration();

        // getPlayerWhoMayEdit is always null for the snapshot, so we use the wrapped BlockEntity
        UUID id = this.getBlockEntity().getPlayerWhoMayEdit();
        return (id == null) ? null : Bukkit.getPlayer(id);
    }

    @Override
    public DyeColor getColor() {
        return this.front.getColor();
    }

    @Override
    public void setColor(DyeColor color) {
        this.front.setColor(color);
    }

    @Override
    public void applyTo(T blockEntity) {
        this.getSnapshot().setText(this.front.applyLegacyStringToSignSide(), true);
        this.getSnapshot().setText(this.back.applyLegacyStringToSignSide(), false);

        super.applyTo(blockEntity);
    }

    @Override
    public CraftSign<T> copy() {
        return new CraftSign<T>(this, null);
    }

    @Override
    public CraftSign<T> copy(Location location) {
        return new CraftSign<T>(this, location);
    }

    public static void openSign(Sign sign, Player player, Side side) {
        Preconditions.checkArgument(sign != null, "sign == null");
        Preconditions.checkArgument(side != null, "side == null");
        Preconditions.checkArgument(sign.isPlaced(), "Sign must be placed");
        Preconditions.checkArgument(sign.getWorld() == player.getWorld(), "Sign must be in same world as Player");

        // Paper start - Add PlayerOpenSignEvent
        io.papermc.paper.event.player.PlayerOpenSignEvent event = new io.papermc.paper.event.player.PlayerOpenSignEvent((Player) player, sign, side, io.papermc.paper.event.player.PlayerOpenSignEvent.Cause.PLUGIN);
        if (!event.callEvent()) return;
        if (PlayerSignOpenEvent.getHandlerList().getRegisteredListeners().length > 0) {
            // Paper end - Add PlayerOpenSignEvent
        if (!CraftEventFactory.callPlayerSignOpenEvent(player, sign, side, PlayerSignOpenEvent.Cause.PLUGIN)) {
            return;
        }
        } // Paper - Add PlayerOpenSignEvent

        SignBlockEntity blockEntity = ((CraftSign<?>) sign).getBlockEntity();
        blockEntity.setAllowedPlayerEditor(player.getUniqueId());

        ((CraftPlayer) player).getHandle().openTextEdit(blockEntity, Side.FRONT == side);
    }

    // Paper start
    public static Component[] sanitizeLines(java.util.List<? extends net.kyori.adventure.text.Component> lines) {
        Component[] components = new Component[4];
        for (int i = 0; i < 4; i++) {
            if (i < lines.size() && lines.get(i) != null) {
                components[i] = io.papermc.paper.adventure.PaperAdventure.asVanilla(lines.get(i));
            } else {
                components[i] = net.minecraft.network.chat.Component.literal("");
            }
        }
        return components;
    }
    // Paper end

    // Paper start - More Sign Block API
    @Override
    public java.util.UUID getAllowedEditorUniqueId() {
        this.ensureNoWorldGeneration();
        return this.getBlockEntity().getPlayerWhoMayEdit();
    }

    @Override
    public void setAllowedEditorUniqueId(java.util.UUID uuid) {
        this.ensureNoWorldGeneration();
        this.getBlockEntity().setAllowedPlayerEditor(uuid);
    }

    @Override
    public Side getInteractableSideFor(final double x, final double z) {
        this.requirePlaced();
        return this.getSnapshot().isFacingFrontText(x, z) ? Side.FRONT : Side.BACK;
    }
    // Paper end - More Sign Block API

    public static Component[] sanitizeLines(String[] lines) {
        Component[] components = new Component[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = Component.empty();
            }
        }

        return components;
    }

    public static String[] revertComponents(Component[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = CraftSign.revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(Component component) {
        return CraftChatMessage.fromComponent(component);
    }
}
