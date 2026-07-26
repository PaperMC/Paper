package org.bukkit;

/**
 * Represents a movement input applied to an entity.
 */
public interface Input {

    /**
     * Gets whether a forward input is applied.
     *
     * @return forward input
     */
    boolean isForward();

    /**
     * Gets whether a backward input is applied.
     *
     * @return backward input
     */
    boolean isBackward();

    /**
     * Gets whether a left input is applied.
     *
     * @return left input
     */
    boolean isLeft();

    /**
     * Gets whether a right input is applied.
     *
     * @return right input
     */
    boolean isRight();

    /**
     * Gets whether a jump input is applied.
     *
     * @return jump input
     */
    boolean isJump();

    /**
     * Gets whether a sneak input is applied.
     *
     * @return sneak input
     */
    boolean isSneak();

    /**
     * Gets whether a sprint input is applied.
     *
     * @return sprint input
     */
    boolean isSprint();
}
