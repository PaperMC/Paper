package org.bukkit.packs;

import java.util.Set;
import org.bukkit.FeatureFlag;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data pack.
 *
 * @see <a href="https://minecraft.wiki/w/Data_pack">Minecraft wiki</a>
 */
public interface DataPack extends Keyed {

    /**
     * Gets the title of the data pack.
     *
     * @return the title
     */
    @NotNull
    public String getTitle();

    /**
     * Gets the description of the data pack.
     *
     * @return the description
     */
    @NotNull
    public String getDescription();

    /**
     * Gets the pack format.
     * <br>
     * Pack formats are non-standard and unrelated to the version of Minecraft. For
     * a list of known pack versions, see the
     * <a href="https://minecraft.wiki/w/Data_pack#Pack_format">Minecraft Wiki</a>.
     *
     * @return the pack version
     * @see #getMinSupportedPackFormat()
     * @see #getMaxSupportedPackFormat()
     */
    public int getPackFormat();

    /**
     * Gets the minimum supported pack format. If the data pack does not specify a
     * minimum supported format, {@link #getPackFormat()} is returned.
     * <br>
     * Pack formats are non-standard and unrelated to the version of Minecraft. For
     * a list of known pack versions, see the
     * <a href="https://minecraft.wiki/w/Data_pack#Pack_format">Minecraft Wiki</a>.
     *
     * @return the min pack version supported
     */
    public int getMinSupportedPackFormat();

    /**
     * Gets the maximum supported pack format. If the data pack does not specify a
     * maximum supported format, {@link #getPackFormat()} is returned.
     * <br>
     * Pack formats are non-standard and unrelated to the version of Minecraft. For
     * a list of known pack versions, see the
     * <a href="https://minecraft.wiki/w/Data_pack#Pack_format">Minecraft Wiki</a>.
     *
     * @return the max pack version supported
     */
    public int getMaxSupportedPackFormat();

    /**
     * Gets if the data pack is enabled on the server.
     *
     * @return True if is enabled
     */
    public boolean isEnabled();

    /**
     * Gets if the data pack is required on the server.
     *
     * @return True if is required
     */
    public boolean isRequired();

    /**
     * Gets the compatibility of this data pack with the server.
     *
     * @return an enum
     */
    @NotNull
    public Compatibility getCompatibility();

    /**
     * Gets a set of features requested by this data pack.
     *
     * @return a set of features
     */
    @NotNull
    public Set<FeatureFlag> getRequestedFeatures();

    /**
     * Gets the source of this data pack.
     *
     * @return the source
     */
    @NotNull
    public Source getSource();

    /**
     * Show the compatibility of the data pack with the server.
     */
    public enum Compatibility {

        /**
         * It's newer than the server pack version.
         */
        NEW,
        /**
         * It's older than the server pack version.
         */
        OLD,
        /**
         * Its compatible with the server pack version.
         */
        COMPATIBLE;
    }

    /**
     * Represent the source of a data pack.
     */
    public enum Source {
        DEFAULT,
        BUILT_IN,
        FEATURE,
        WORLD,
        SERVER;
    }
}
