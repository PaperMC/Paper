package io.papermc.paper.datacomponent.item.attribute;

import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
interface AttributeModifierDisplayBridge {

    Optional<AttributeModifierDisplayBridge> BRIDGE = ServiceLoader.load(AttributeModifierDisplayBridge.class, AttributeModifierDisplayBridge.class.getClassLoader()).findFirst();

    static AttributeModifierDisplayBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    AttributeModifierDisplay.Default reset();

    AttributeModifierDisplay.Hidden hidden();

    AttributeModifierDisplay.OverrideText override(ComponentLike text);
}
