package io.papermc.paper.adventure.providers;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class GsonComponentSerializerProviderImpl implements GsonComponentSerializer.Provider {

    @Override
    public @NotNull GsonComponentSerializer gson() {
        return GsonComponentSerializer.builder()
            .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.INSTANCE)
            .build();
    }

    @Override
    public @NotNull GsonComponentSerializer gsonLegacy() {
        return GsonComponentSerializer.builder()
            .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.INSTANCE)
            .editOptions(editor -> editor.value(JSONOptions.EMIT_RGB, false))
            .build();
    }

    @Override
    public @NotNull Consumer<GsonComponentSerializer.Builder> builder() {
        return builder -> builder.legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.INSTANCE);
    }
}
