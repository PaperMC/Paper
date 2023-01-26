package org.bukkit.advancement;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * Advancements are displayed in different ways depending on their display type.
 *
 * This enum contains information about these types and how they are
 * represented.
 */
public enum AdvancementDisplayType {

    /**
     * Task or normal icons have a square icon frame.
     */
    TASK(ChatColor.GREEN),
    /**
     * Challenge icons have a stylised icon frame.
     */
    CHALLENGE(ChatColor.DARK_PURPLE),
    /**
     * Goal icons have a rounded icon frame.
     */
    GOAL(ChatColor.GREEN);
    private final ChatColor color;

    private AdvancementDisplayType(ChatColor color) {
        this.color = color;
    }

    /**
     * The chat color used by Minecraft for this advancement.
     *
     * @return The chat color used by this advancement type.
     */
    @NotNull
    public ChatColor getColor() {
        return color;
    }
}
