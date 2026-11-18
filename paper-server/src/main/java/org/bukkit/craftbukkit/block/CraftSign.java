package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.bukkit.Bukkit;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftSign<T extends SignBlockEntity> extends CraftBlockEntityState<T> implements Sign {

    private @MonotonicNonNull CraftSignSide front;
    private @MonotonicNonNull CraftSignSide back;

    public CraftSign(final World world, final T blockEntity, final boolean useSnapshot) {
        super(world, blockEntity, useSnapshot);
    }

    protected CraftSign(final CraftSign<T> state, final @Nullable Location location) {
        super(state, location);
    }

    @Override
    protected void load(final T blockEntity) {
        super.load(blockEntity);
        this.front = new CraftSignSide(blockEntity.getText(SignTextSlot.FRONT));
        this.back = new CraftSignSide(blockEntity.getText(SignTextSlot.BACK));
    }

    @Override
    public boolean isWaxed() {
        return this.getSnapshot().isWaxed();
    }

    @Override
    public void setWaxed(final boolean waxed) {
        this.getSnapshot().setWaxed(waxed);
    }

    @Override
    public SignSide getSide(final Side side) {
        Preconditions.checkArgument(side != null, "side == null");

        return switch (side) {
            case FRONT -> this.front;
            case BACK -> this.back;
            default -> throw new IllegalArgumentException("Unknown side: " + side);
        };
    }

    @Override
    public SignSide getTargetSide(final Player player) {
        this.ensureNoWorldGeneration();
        Preconditions.checkArgument(player != null, "player cannot be null");

        if (this.getSnapshot().getSlotPlayerIsFacing(((CraftPlayer) player).getHandle()) == SignTextSlot.FRONT) {
            return this.front;
        }

        return this.back;
    }

    @Override
    public @Nullable Player getAllowedEditor() {
        this.ensureNoWorldGeneration();

        // getPlayerWhoMayEdit is always null for the snapshot, so we use the wrapped BlockEntity
        final UUID id = this.getBlockEntity().getPlayerWhoMayEdit();
        return (id == null) ? null : Bukkit.getPlayer(id);
    }

    @Override
    public void applyTo(final T blockEntity) {
        this.getSnapshot().setText(this.front.applyLegacyStringToSignSide(), SignTextSlot.FRONT);
        this.getSnapshot().setText(this.back.applyLegacyStringToSignSide(), SignTextSlot.BACK);

        super.applyTo(blockEntity);
    }

    @Override
    public CraftSign<T> copy() {
        return new CraftSign<>(this, null);
    }

    @Override
    public CraftSign<T> copy(final Location location) {
        return new CraftSign<>(this, location);
    }

    public static void openSign(final Sign sign, final Player player, final Side side) {
        Preconditions.checkArgument(sign != null, "sign == null");
        Preconditions.checkArgument(side != null, "side == null");
        Preconditions.checkArgument(sign.isPlaced(), "Sign must be placed");
        Preconditions.checkArgument(sign.getWorld() == player.getWorld(), "Sign must be in same world as Player");

        final PlayerOpenSignEvent event = new PlayerOpenSignEvent(player, sign, side, PlayerOpenSignEvent.Cause.PLUGIN);;
        if (!event.callEvent()) return;
        if (PlayerSignOpenEvent.getHandlerList().hasListeners()) {
            if (!CraftEventFactory.callPlayerSignOpenEvent(player, sign, side, PlayerSignOpenEvent.Cause.PLUGIN)) {
                return;
            }
        }

        final SignBlockEntity blockEntity = ((CraftSign<?>) sign).getBlockEntity();
        blockEntity.setAllowedPlayerEditor(player.getUniqueId());

        ((CraftPlayer) player).getHandle().openTextEdit(blockEntity, CraftSignSide.toVanilla(side));
    }

    public static Component[] sanitizeLines(final List<? extends net.kyori.adventure.text.Component> lines) {
        Component[] components = new Component[SignText.LINES];
        for (int i = 0; i < SignText.LINES; i++) {
            if (i < lines.size() && lines.get(i) != null) {
                components[i] = io.papermc.paper.adventure.PaperAdventure.asVanilla(lines.get(i));
            } else {
                components[i] = net.minecraft.network.chat.Component.literal("");
            }
        }
        return components;
    }

    @Override
    public @Nullable UUID getAllowedEditorUniqueId() {
        this.ensureNoWorldGeneration();
        return this.getBlockEntity().getPlayerWhoMayEdit();
    }

    @Override
    public void setAllowedEditorUniqueId(final @Nullable UUID uuid) {
        this.ensureNoWorldGeneration();
        this.getBlockEntity().setAllowedPlayerEditor(uuid);
    }

    @Override
    public Side getInteractableSideFor(final double x, final double z) {
        this.requirePlaced();
        return CraftSignSide.fromVanilla(this.getSnapshot().getSlotPlayerIsFacing(x, z));
    }

    @Override
    public void setOperatorFeaturesAllowed(final boolean allowed) {
        this.getSnapshot().allowOpFeatures = allowed;
    }

    @Override
    public boolean isOperatorFeaturesAllowed() {
        return this.getSnapshot().allowOpFeatures;
    }

    public static Component[] sanitizeLines(final String[] lines) {
        final Component[] components = new Component[SignText.LINES];

        for (int i = 0; i < SignText.LINES; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = Component.empty();
            }
        }

        return components;
    }

    public static String[] revertComponents(final Component[] components) {
        final String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = CraftSign.revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(final Component component) {
        return CraftChatMessage.fromComponent(component);
    }
}
