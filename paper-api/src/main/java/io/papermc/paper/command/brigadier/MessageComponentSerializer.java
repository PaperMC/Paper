package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A component serializer for converting between {@link Message} and {@link Component}.
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface MessageComponentSerializer extends ComponentSerializer<Component, Component, Message> {

    /**
     * A component serializer for converting between {@link Message} and {@link Component}.
     *
     * @return serializer instance
     */
    static MessageComponentSerializer message() {
        return MessageComponentSerializerHolder.PROVIDER.orElseThrow();
    }
}
