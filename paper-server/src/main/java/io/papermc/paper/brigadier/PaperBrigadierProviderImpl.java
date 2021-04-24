package io.papermc.paper.brigadier;

import com.mojang.brigadier.Message;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.network.chat.ComponentUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

public enum PaperBrigadierProviderImpl implements PaperBrigadierProvider {
    INSTANCE;

    PaperBrigadierProviderImpl() {
        PaperBrigadierProvider.initialize(this);
    }

    @Override
    public @NonNull Message message(final @NonNull ComponentLike componentLike) {
        requireNonNull(componentLike, "componentLike");
        return PaperAdventure.asVanilla(componentLike.asComponent());
    }

    @Override
    public @NonNull Component componentFromMessage(final @NonNull Message message) {
        requireNonNull(message, "message");
        return PaperAdventure.asAdventure(ComponentUtils.fromMessage(message));
    }
}
