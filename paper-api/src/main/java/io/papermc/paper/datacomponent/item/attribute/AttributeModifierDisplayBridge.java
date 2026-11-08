package io.papermc.paper.datacomponent.item.attribute;

import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.util.Services;

interface AttributeModifierDisplayBridge {

    static AttributeModifierDisplayBridge bridge() {
        final class Holder {
            static final Optional<AttributeModifierDisplayBridge> BRIDGE = Services.service(ServiceLoader.load(AttributeModifierDisplayBridge.class), AttributeModifierDisplayBridge.class);
        }

        return Holder.BRIDGE.orElseThrow();
    }

    AttributeModifierDisplay.Default reset();

    AttributeModifierDisplay.Hidden hidden();

    AttributeModifierDisplay.OverrideText override(ComponentLike text);
}
