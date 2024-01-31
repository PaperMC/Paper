package org.bukkit.packs;

import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a resource pack.
 *
 * @see <a href="https://minecraft.wiki/w/Resource_pack">Minecraft wiki</a>
 */
@ApiStatus.Experimental
public interface ResourcePack {

    /**
     * Gets the id of the resource pack.
     *
     * @return the id
     */
    @NotNull
    public UUID getId();

    /**
     * Gets the url of the resource pack.
     *
     * @return the url
     */
    @NotNull
    public String getUrl();

    /**
     * Gets the hash of the resource pack.
     *
     * @return the hash
     */
    @Nullable
    public String getHash();

    /**
     * Gets the prompt to show of the resource pack.
     *
     * @return the prompt
     */
    @Nullable
    public String getPrompt();

    /**
     * Gets if the resource pack is required by the server.
     *
     * @return True if is required
     */
    public boolean isRequired();
}
