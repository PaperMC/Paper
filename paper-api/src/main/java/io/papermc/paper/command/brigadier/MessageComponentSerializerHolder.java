package io.papermc.paper.command.brigadier;

import java.util.Optional;
import java.util.ServiceLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
final class MessageComponentSerializerHolder {

    static final Optional<MessageComponentSerializer> PROVIDER = ServiceLoader.load(MessageComponentSerializer.class)
        .findFirst();
}
