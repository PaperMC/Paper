package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import net.minecraft.Optionull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

public class PaperPlayerUseUnknownEntityEvent extends CraftPlayerEvent implements PlayerUseUnknownEntityEvent {

    private final int entityId;
    private final boolean attack;
    private final EquipmentSlot hand;
    private final @Nullable Vector clickedPosition;

    public PaperPlayerUseUnknownEntityEvent(final Player player, final int entityId, final boolean attack, final EquipmentSlot hand, final @Nullable Vector clickedPosition) {
        super(player);
        this.entityId = entityId;
        this.attack = attack;
        this.hand = hand;
        this.clickedPosition = clickedPosition;
    }

    public PaperPlayerUseUnknownEntityEvent(
        final ServerPlayer player, final int entityId, final boolean attack, final InteractionHand hand, final @Nullable Vec3 clickedPosition
    ) {
        this(player.getBukkitEntity(), entityId, attack, CraftEquipmentSlot.getHand(hand), Optionull.map(clickedPosition, CraftVector::toBukkit));
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public boolean isAttack() {
        return this.attack;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public @Nullable Vector getClickedRelativePosition() {
        return this.clickedPosition != null ? this.clickedPosition.clone() : null;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerUseUnknownEntityEvent.getHandlerList();
    }
}
