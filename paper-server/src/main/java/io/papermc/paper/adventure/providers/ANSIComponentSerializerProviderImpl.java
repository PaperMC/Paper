package io.papermc.paper.adventure.providers;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import org.jspecify.annotations.NullMarked;
import java.util.function.Consumer;

@NullMarked
@SuppressWarnings("UnstableApiUsage") // permitted provider
public class ANSIComponentSerializerProviderImpl implements ANSIComponentSerializer.Provider {
    @Override
    public ANSIComponentSerializer ansi() {
        return ANSIComponentSerializer.builder().flattener(PaperAdventure.FLATTENER).build();
    }

    @Override
    public Consumer<ANSIComponentSerializer.Builder> builder() {
        return builder -> builder.flattener(PaperAdventure.FLATTENER);
    }
}
