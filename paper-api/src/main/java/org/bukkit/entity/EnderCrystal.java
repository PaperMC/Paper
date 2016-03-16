package org.bukkit.entity;

/**
 * A crystal that heals nearby EnderDragons
 */
public interface EnderCrystal extends Entity {

    /**
     * Return whether or not this end crystal is showing the
     * bedrock slate underneath it.
     *
     * @return true if the bottom is being shown
     */
    boolean isShowingBottom();

    /**
     * Sets whether or not this end crystal is showing the
     * bedrock slate underneath it.
     *
     * @param showing whether the bedrock slate should be shown
     */
    void setShowingBottom(boolean showing);
}
