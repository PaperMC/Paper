package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.Collections;
import java.util.List;

/**
 * Called right before the scoreboard tags for an entity are changed.
 */
@NullMarked
public class ScoreboardTagsChangeEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<String> tags;
    private final Change change;

    @ApiStatus.Internal
    public ScoreboardTagsChangeEvent(final Entity entity, final List<String> tags, final Change change) {
        super(entity);
        this.tags = Collections.unmodifiableList(tags);
        this.change = change;
    }

    /**
     * Get the tags that are being added/removed/set.
     *
     * @return Tags being added/removed/set. Unmodifiable.
     */
    public List<String> getTags() {
        return this.tags;
    }

    /**
     * Gets the type of change happening to the entity's tags.
     *
     * @return Type of change happening.
     */
    public Change getChange() {
        return this.change;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents the possible changes made to the entity's tags.
     */
    public enum Change {
        /** The tags were added to the entity. */
        ADD,
        /** The tags were removed from the entity. */
        REMOVE,
        /** The tags replaced the entity's existing tags. */
        SET,
    }
}
