package io.papermc.paper.disguise;

import com.destroystokyo.paper.SkinParts;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.Objects;
import org.bukkit.Server;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PlayerDisguise(PlayerProfile playerProfile, boolean listed, boolean showHead,
                             @Nullable SkinParts skinParts) implements DisguiseData {

    @ApiStatus.Internal
    public PlayerDisguise {
        Objects.requireNonNull(playerProfile, "profile cannot be null");
    }

    public static Builder builder(PlayerProfile playerProfile) {
        return new Builder(playerProfile);
    }

    /**
     * Represents the builder to configure certain appearance settings.
     */
    public static class Builder {
        private final PlayerProfile playerProfile;
        private boolean listed;
        private boolean showHead;
        @Nullable
        private SkinParts skinParts;

        @ApiStatus.Internal
        public Builder(PlayerProfile playerProfile) {
            this.playerProfile = playerProfile;
        }

        /**
         * Defines if the fake player will be shown in player list.
         *
         * @param listed true, if the player should be listed else false
         * @return the builder instance
         */
        public Builder listed(boolean listed) {
            this.listed = listed;
            return this;
        }

        /**
         * Defines which skin parts should be enabled for the fake player.
         * <p>
         *
         * @param showHead defines if the fake players head should be shown in the player list.
         * @return the builder instance
         */
        public Builder showHead(boolean showHead) {
            this.showHead = showHead;
            return this;
        }

        /**
         * Defines which skin parts should be enabled for the fake player.
         * <p>
         * Use {@link Server#newSkinPartsBuilder()} to get a fresh builder instance for configuration.
         *
         * @param skinParts the skin parts that should be shown.
         * @return the builder instance
         */
        public Builder skinParts(SkinParts skinParts) {
            this.skinParts = skinParts;
            return this;
        }

        /**
         * Builds the disguise
         *
         * @return the built disguise
         */
        public PlayerDisguise build() {
            return new PlayerDisguise(playerProfile, listed, showHead, skinParts);
        }
    }
}
