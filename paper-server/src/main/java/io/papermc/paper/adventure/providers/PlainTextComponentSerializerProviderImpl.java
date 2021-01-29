package io.papermc.paper.adventure.providers;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class PlainTextComponentSerializerProviderImpl implements PlainTextComponentSerializer.Provider {

    @Override
    public @NotNull PlainTextComponentSerializer plainTextSimple() {
        return PlainTextComponentSerializer.builder()
            .flattener(PaperAdventure.FLATTENER)
            .build();
    }

    @Override
    public @NotNull Consumer<PlainTextComponentSerializer.Builder> plainText() {
        return builder -> builder.flattener(PaperAdventure.FLATTENER);
    }
}
