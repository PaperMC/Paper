package cool.circuit.paper.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
    public GradientComponent(final TextColor startColor, final TextColor endColor, final String text) {
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
    public Component getComponent() {
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
    public String getText() {
        return LegacyComponentSerializer.legacySection().serialize(getComponent());
    }

    /**
     * Adds a click event to the component
     * @param event The event to be triggered on click
     */
    public void addClickEvent(ClickEvent event) {
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
    public void addHoverEvent(HoverEvent<?> event) {
        if (component == null) {
            component = MiniMessage.miniMessage()
                .deserialize("<gradient:" + startColor.asHexString() + ":" + endColor.asHexString() + ">" + text + "</gradient>");
        }
        component = component.hoverEvent(event);
    }
}
