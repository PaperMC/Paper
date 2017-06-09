package io.papermc.paper.adventure.providers;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.logger.slf4j.ComponentLoggerProvider;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
public class ComponentLoggerProviderImpl implements ComponentLoggerProvider {
    @Override
    public @NotNull ComponentLogger logger(@NotNull LoggerHelper helper, @NotNull String name) {
        return helper.delegating(LoggerFactory.getLogger(name), this::serialize);
    }

    private String serialize(final Component message) {
        return PaperAdventure.ANSI_SERIALIZER.serialize(GlobalTranslator.render(message, Locale.getDefault()));
    }
}
