package io.papermc.paper;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Information about the current Mitra server build.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface ServerBuildInfo {
    /**
     * The brand id for Mitra.
     */
    Key BRAND_MITRA_ID = Key.key("mitra", "mitra");

    /**
     * The brand id for Paper (maintained for compatibility).
     */
    Key BRAND_PAPER_ID = Key.key("papermc", "paper");

    /**
     * Gets the {@code ServerBuildInfo}.
     *
     * @return the {@code ServerBuildInfo}
     */
    static ServerBuildInfo buildInfo() {
        //<editor-fold defaultstate="collapsed" desc="Holder">
        final class Holder {
            static final Optional<ServerBuildInfo> INSTANCE = Services.service(ServerBuildInfo.class);
        }
        //</editor-fold>
        return Holder.INSTANCE.orElseThrow();
    }

    /**
     * Gets the brand id of the server.
     *
     * @return the brand id of the server (e.g. "mitra:mitra")
     */
    Key brandId();

    /**
     * Checks if the current server supports the specified brand.
     *
     * @param brandId the brand to check (e.g. "mitra:mitra")
     * @return {@code true} if the server supports the specified brand
     */
    @ApiStatus.Experimental
    boolean isBrandCompatible(final Key brandId);

    /**
     * Gets the brand name of the server.
     *
     * @return the brand name of the server (e.g. "Mitra")
     */
    String brandName();

    /**
     * Gets the Minecraft version id.
     *
     * @return the Minecraft version id (e.g. "1.21.1")
     */
    String minecraftVersionId();

    /**
     * Gets the Minecraft version name.
     *
     * @return the Minecraft version name (e.g. "1.21.1")
     */
    String minecraftVersionName();

    /**
     * Gets the build number.
     *
     * @return the build number
     */
    OptionalInt buildNumber();

    /**
     * Gets the build time.
     *
     * @return the build time
     */
    Instant buildTime();

    /**
     * Gets the git commit branch.
     *
     * @return the git commit branch
     */
    Optional<String> gitBranch();

    /**
     * Gets the git commit hash.
     *
     * @return the git commit hash
     */
    Optional<String> gitCommit();

    /**
     * Creates a string representation of the server build information.
     *
     * @param representation the type of representation
     * @return a string
     */
    String asString(final StringRepresentation representation);

    /**
     * String representation types.
     */
    enum StringRepresentation {
        /**
         * A simple version string, in format {@code <minecraftVersionId>-<buildNumber>-<gitCommit> (Mitra)}.
         */
        VERSION_SIMPLE,
        /**
         * A simple version string, in format {@code Mitra version <minecraftVersionId>-<buildNumber>-<gitBranch>@<gitCommit> (<buildTime>)}.
         */
        VERSION_FULL,
    }
}
