package org.bukkit.entity;

/**
 * Illager beast.
 *
 * @since 1.14
 */
// Paper start - Missing Entity Behavior
public interface Ravager extends Raider {

    /**
     * Gets how many ticks this ravager is attacking for.
     * When attacking, the ravager cannot move.
     *
     * @return ticks attacking or -1 if they are currently not attacking
     * @since 1.19.2
     */
    int getAttackTicks();

    /**
     * Sets how many ticks this ravager is attacking for.
     * When attacking, the ravager cannot move.
     * This will tick down till it gets to -1, where this ravager will no longer be attacking.
     *
     * @param ticks ticks attacking or -1 if they should no longer be attacking
     * @since 1.19.2
     */
    void setAttackTicks(int ticks);

    /**
     * Gets how many ticks the ravager is stunned for.
     * The ravager cannot move or attack while stunned.
     * At 0, this will cause the ravager to roar.
     *
     * @return ticks stunned or -1 if they are currently not stunned
     * @since 1.19.2
     */
    int getStunnedTicks();

    /**
     * Sets how many ticks the ravager is stunned for.
     * The ravager cannot move or attack while stunned.
     * At 0, this will cause the ravager to roar.
     *
     * @param ticks ticks stunned or -1 if they should no longer be stunned
     * @since 1.19.2
     */
    void setStunnedTicks(int ticks);

    /**
     * Gets how many ticks the ravager is roaring for.
     * While roaring, the ravager cannot move
     *
     * @return ticks roaring or -1 if they are currently not roaring
     * @since 1.19.2
     */
    int getRoarTicks();

    /**
     * Sets how many ticks the ravager is roaring for.
     * While roaring, the ravager cannot move
     * This will tick down till it gets to -1, where it is no longer active.
     * If set to 11, this will play a sound and hurt nearby players.
     *
     * @param ticks ticks roaring or -1 if they should no longer be roaring
     * @since 1.19.2
     */
    void setRoarTicks(int ticks);

}
// Paper end
