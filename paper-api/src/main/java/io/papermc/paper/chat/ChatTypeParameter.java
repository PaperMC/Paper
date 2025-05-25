package io.papermc.paper.chat;

import net.kyori.adventure.text.format.Style;
import java.util.List;

/**
 * The available parameters for vanilla chat formatting.
 * This used to determine the order of how chat content is rendered.
 *
 * @see io.papermc.paper.chat.vanilla.ChatTypeRenderer#vanillaChatType(String, String, Style, List)
 */
public enum ChatTypeParameter {
    SENDER,
    TARGET,
    CONTENT
}
