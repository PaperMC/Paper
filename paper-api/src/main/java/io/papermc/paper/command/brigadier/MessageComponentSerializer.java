package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.Message;
import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * A component serializer for converting between {@link Message} and {@link Component}.
 */
@ApiStatus.NonExtendable
public interface MessageComponentSerializer extends ComponentSerializer<Component, Component, Message> {

    /**
     * A component serializer for converting between {@link Message} and {@link Component}.
     *
     * @return serializer instance
     */
    static MessageComponentSerializer message() {
        final class Holder {
            static final Optional<MessageComponentSerializer> PROVIDER = ServiceLoader.load(MessageComponentSerializer.class, MessageComponentSerializer.class.getClassLoader()).findFirst();
        }
        return Holder.PROVIDER.orElseThrow();
    }
}
