package cool.circuit.paper.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class to create gradient text components.
 */
public final class GradientComponent {
    private final TextColor startColor;
    private final TextColor endColor;
    private final String text;

    private Component component;

    /**
     * Constructs a GradientComponent with the given gradient and text.
     *
     * @param startColor The starting color of the gradient
     * @param endColor The ending color of the gradient
     * @param text The text to display with the gradient
     */
    public GradientComponent(final @NotNull TextColor startColor, final @NotNull TextColor endColor, final @NotNull String text) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.text = text;
        this.component = null;
    }

    /**
     * Returns the gradient text component.
     *
     * @return A Component representing the gradient text
     */
    public @NotNull Component getComponent() {
        if (component == null) {
            component = MiniMessage.miniMessage()
                .deserialize("<gradient:" + startColor.asHexString() + ":" + endColor.asHexString() + ">" + text + "</gradient>");
        }
        return component;
    }

    /**
     * Returns the gradient text
     *
     * @return A String representing the gradient text
     */
    public @NotNull String getText() {
        return LegacyComponentSerializer.legacySection().serialize(getComponent());
    }

    /**
     * Adds a click event to the component
     * @param event The event to be triggered on click
     */
    public void addClickEvent(final @NotNull ClickEvent event) {
        if (component == null) {
            component = MiniMessage.miniMessage()
                .deserialize("<gradient:" + startColor.asHexString() + ":" + endColor.asHexString() + ">" + text + "</gradient>");
        }
        component = component.clickEvent(event);
    }

    /**
     * Adds a hover event to the component
     * @param event The event to be triggered on hover
     */
    public void addHoverEvent(final @NotNull HoverEvent<?> event) {
        if (component == null) {
            component = MiniMessage.miniMessage()
                .deserialize("<gradient:" + startColor.asHexString() + ":" + endColor.asHexString() + ">" + text + "</gradient>");
        }
        component = component.hoverEvent(event);
    }

    /**
     * Appends the specified component to this component.
     *
     * @param whatToAppend The component to append.
     */
    public void append(final @NotNull Component whatToAppend) {
        component = component.append(whatToAppend);
    }
}
