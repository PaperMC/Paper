package io.papermc.paper.event.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the server temporarily updates a player's modifier. This is not called for any arbitrary modifier update
 * on the player.
 * <p>
 * Note: Currently only called when a player crouches making their waypoint transmit range attribute disappear.
 * </p>
 */
@NullMarked
public class PlayerTransientModifierUpdateEvent extends PlayerEvent implements Cancellable {

    private static final  HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;
    private final boolean removal;
    private final Attribute attribute;
    private AttributeModifier modifier;

    @ApiStatus.Internal
    public PlayerTransientModifierUpdateEvent(Player player, boolean removal, Attribute attribute, AttributeModifier modifier) {
        super(player);
        this.removal = removal;
        this.attribute = attribute;
        this.modifier = modifier;
    }

    /**
     * Returns whether this event involves a removal or addition of the transient modifier.
     * @return whether the modifier is being removed
     */
    public boolean isRemoval() {
        return this.removal;
    }
    /**
     * The attribute type this modifier is being applied on.
     * @return the attribute type
     */
    public Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Returns transient modifier to be updated.
     * @return the modifier
     */
    public AttributeModifier getModifier() {
        return this.modifier;
    }

    /**
     * Sets the transient modifier to be updated.
     * @param modifier the new modifier
     */
    public void setModifier(AttributeModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
