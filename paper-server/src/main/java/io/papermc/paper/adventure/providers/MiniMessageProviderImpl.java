package io.papermc.paper.adventure.providers;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class MiniMessageProviderImpl implements MiniMessage.Provider {

    @Override
    public @NotNull MiniMessage miniMessage() {
        return MiniMessage.builder().emitVirtuals(false).build();
    }

    @Override
    public @NotNull Consumer<MiniMessage.Builder> builder() {
        return builder -> {};
    }
}
