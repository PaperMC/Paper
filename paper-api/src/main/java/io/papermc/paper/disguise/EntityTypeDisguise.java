package io.papermc.paper.disguise;

import java.util.Objects;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record EntityTypeDisguise(EntityType entityType) implements DisguiseData {
    @ApiStatus.Internal
    public EntityTypeDisguise {
        Objects.requireNonNull(entityType, "type cannot be null");
    }

    /**
     * Represents the builder to configure certain appearance settings.
     */
    public static class Builder {
        private final EntityType entityType;

        @ApiStatus.Internal
        public Builder(EntityType entityType) {
            this.entityType = entityType;
        }

        /**
         * Builds the disguise
         *
         * @return the built disguise
         */
        public EntityTypeDisguise build() {
            return new EntityTypeDisguise(entityType);
        }
    }
}
