package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.JukeboxSongKeys;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    static final TypedKey<JukeboxSong> NEW = JukeboxSongKeys.create(Key.key("test:test"));

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.JUKEBOX_SONG.freeze(), event -> {
            // Do something with the event
            event.registry().register(NEW, b -> {
                b.comparatorOutput(2)
                    .description(Component.text("EPIC CUSTOM SOUND SONG"))
                    .lengthInSeconds(2)
                    .soundEvent(sb -> sb.copyFrom(SoundEventKeys.AMBIENT_BASALT_DELTAS_ADDITIONS).location(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON));
            });
        });
    }

}
