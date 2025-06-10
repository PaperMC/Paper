package io.papermc.paper.datacomponent.item.attribute;

import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.ServiceLoader;

@NullMarked
@ApiStatus.Internal
public interface AttributeModifierDisplayBridge {

    Optional<AttributeModifierDisplayBridge> BRIDGE = ServiceLoader.load(AttributeModifierDisplayBridge.class).findFirst();

    static AttributeModifierDisplayBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    AttributeModifierDisplay.Default reset();

    AttributeModifierDisplay.Hidden hidden();

    AttributeModifierDisplay.OverrideText override(ComponentLike text);
}
