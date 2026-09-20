package io.papermc.paper.entity;

import java.util.SequencedCollection;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the post-effects that can be applied to a player.
 * @see <a href="https://minecraft.wiki/w/Shader#Post-processing_effects">Post-Effects - Minecraft Wiki</a>
 */
@NullMarked
public interface PlayerPostEffects {

    /**
     * Gets the list of post-effects that are currently applied to the player.
     *
     * @return an unmodifiable list of post-effects
     */
    @Unmodifiable
    SequencedCollection<Key> values();

    /**
     * Sets the list of post-effects that are currently applied to the player.
     *
     * @param postEffects the list of post-effects to set
     * @return true if the post-effects were changed, false if they were the same
     */
    boolean set(SequencedCollection<Key> postEffects);

    /**
     * Adds a post-effect to the player.
     *
     * @param effect the key post-effect to add
     * @return true if the post-effect was added, false if it was already present
     */
    boolean add(Key effect);

    /**
     * Removes a post-effect from the player.
     *
     * @param effect the key post-effect to remove
     * @return true if the post-effect was removed, false if it was not present
     */
    boolean remove(Key effect);

    /**
     * Clears all post-effects from the player.
     *
     * @return true if any post-effects were cleared, false if there were none
     */
    boolean clear();

}
