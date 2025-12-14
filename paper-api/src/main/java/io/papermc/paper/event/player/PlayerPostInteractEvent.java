package io.papermc.paper.event.player;

import io.papermc.paper.interact.InteractionResult;
import io.papermc.paper.interact.InteractionType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an event that is called after an interaction is performed. This event can be called multiple times for a
 * single action, as the client may send multiple interaction packets for a single click. For example, with both the off
 * and main hands. This event may be called after the following events, if an interaction is performed:
 * <ul>
 *     <li>{@link org.bukkit.event.player.PlayerInteractEvent}</li>
 *     <li>{@link org.bukkit.event.player.PlayerInteractEntityEvent}</li>
 *     <li>{@link org.bukkit.event.player.PlayerInteractAtEntityEvent}</li>
 * </ul>
 *
 */
@NullMarked
public class PlayerPostInteractEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final InteractionResult interactionResult;
    private final InteractionType interactionType;
    private final EquipmentSlot hand;

    @ApiStatus.Internal
    public PlayerPostInteractEvent(Player player, EquipmentSlot hand, InteractionType interactionType, InteractionResult interactionResult) {
        super(player);

        this.hand = hand;
        this.interactionType = interactionType;
        this.interactionResult = interactionResult;
    }

    /**
     * Returns the hand the interaction was performed with.
     *
     * @return the interaction hand
     */
    public EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Returns the type of interaction performed, with additional context dependent on the type.
     *
     * @return the interaction type
     */
    public InteractionType getInteractionType() {
        return interactionType;
    }

    /**
     * Returns the result of the interaction.
     *
     * @return the interaction result
     */
    public InteractionResult getInteractionResult() {
        return interactionResult;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
