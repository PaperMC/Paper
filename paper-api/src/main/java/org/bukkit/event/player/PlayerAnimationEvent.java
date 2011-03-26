package org.bukkit.event.player;

import org.bukkit.entity.Player;

/*
 * Represents a player animation event
 */
public class PlayerAnimationEvent extends PlayerEvent {

	private PlayerAnimationType animationType;

	/*
	 * Construct a new event
	 *
	 * @param type The event type
	 * @param player The player instance
	 */
	public PlayerAnimationEvent(final Player player) {
        super(Type.PLAYER_ANIMATION, player);

        // Only supported animation type for now:
        animationType = PlayerAnimationType.ARM_SWING;
    }

    /*
     * Get the type of this animation event
     *
     * @returns the animation type
     */
    public PlayerAnimationType getAnimationType() {
        return animationType;
    }

}
