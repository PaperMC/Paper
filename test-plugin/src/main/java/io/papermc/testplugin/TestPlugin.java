package io.papermc.testplugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.text.MessageFormat;
import java.util.Locale;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        GlobalTranslator.translator().addSource(new Translator() {
            @Override
            public @NotNull Key name() {
                return Key.key("testplugin");
            }

            @Override
            public @Nullable MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
                return new MessageFormat(key + " " + locale.toLanguageTag());
            }
        });
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        var message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (message.equalsIgnoreCase("null"))
            event.getPlayer().setEffectiveLocale(null);
        else
            event.getPlayer().setEffectiveLocale(Locale.forLanguageTag(message));
        event.getPlayer().sendMessage(Component.text("Effective locale: " + event.getPlayer().getEffectiveLocale()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(Component.translatable("test.string"));
    }
}
