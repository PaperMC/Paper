package org.bukkit.packs;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * Represents a resource pack.
 *
 * @see <a href="https://minecraft.wiki/w/Resource_pack">Minecraft wiki</a>
 */
public interface ResourcePack {

    /**
     * Gets the id of the resource pack.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Gets the url of the resource pack.
     *
     * @return the url
     */
    String getUrl();

    /**
     * Gets the hash of the resource pack.
     *
     * @return the hash
     */
    @Nullable String getHash();

    /**
     * Gets the prompt to show of the resource pack.
     *
     * @return the prompt
     */
    @Nullable String getPrompt();

    /**
     * Gets if the resource pack is required by the server.
     *
     * @return True if is required
     */
    boolean isRequired();
}
