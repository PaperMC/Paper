package org.bukkit.inventory.meta;

import org.bukkit.DyeColor;
import org.jetbrains.annotations.Nullable;

public interface ShieldMeta extends BannerMeta {

    /**
     * Gets the base color for this shield.
     *
     * @return the base color or null
     */
    @Nullable
    DyeColor getBaseColor();

    /**
     * Sets the base color for this shield.
     * <br>
     * <b>Note:</b> If the shield contains a
     * {@link org.bukkit.block.banner.Pattern}, then a null base color will
     * retain the pattern but default the base color to {@link DyeColor#WHITE}.
     *
     * @param color the base color or null
     */
    void setBaseColor(@Nullable DyeColor color);

}
